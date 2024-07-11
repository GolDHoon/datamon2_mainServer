package com.datamon.datamon2.servcie.logic;

import com.datamon.datamon2.common.CommonCodeCache;
import com.datamon.datamon2.dto.input.landingPage.CreateDto;
import com.datamon.datamon2.dto.input.landingPage.CustDataDto;
import com.datamon.datamon2.dto.output.landingPage.InitalDataDto;
import com.datamon.datamon2.dto.repository.*;
import com.datamon.datamon2.servcie.repository.*;
import com.datamon.datamon2.util.EncryptionUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service("LandingPageLogicService")
public class LandingPageService {
    private UserBaseService userBaseService;
    private LpgeCodeService lpgeCodeService;
    private com.datamon.datamon2.servcie.repository.LandingPageService landingPageRepositoryService;
    private UserCdbtMappingService userCdbtMappingService;
    private LandingPageBlockedIpService landingPageBlockedIpService;
    private LandingPageBlockedKeywordService landingPageBlockedKeywordService;
    private CustomerInformationService customerInformationService;
    private CustomerBasicConsultationService customerBasicConsultationService;

    public LandingPageService(UserBaseService userBaseService, LpgeCodeService lpgeCodeService, com.datamon.datamon2.servcie.repository.LandingPageService landingPageRepositoryService, UserCdbtMappingService userCdbtMappingService, LandingPageBlockedIpService landingPageBlockedIpService, LandingPageBlockedKeywordService landingPageBlockedKeywordService, CustomerInformationService customerInformationService, CustomerBasicConsultationService customerBasicConsultationService) {
        this.userBaseService = userBaseService;
        this.lpgeCodeService = lpgeCodeService;
        this.landingPageRepositoryService = landingPageRepositoryService;
        this.userCdbtMappingService = userCdbtMappingService;
        this.landingPageBlockedIpService = landingPageBlockedIpService;
        this.landingPageBlockedKeywordService = landingPageBlockedKeywordService;
        this.customerInformationService = customerInformationService;
        this.customerBasicConsultationService = customerBasicConsultationService;
    }

    @Transactional
    public InitalDataDto getInitialData(String domain) throws Exception{
        InitalDataDto result = new InitalDataDto();

        if (domain.endsWith("/")) {
            domain = domain.substring(0, domain.length() - 1);
        }

        if (domain.startsWith("www.")) {
            domain = domain.substring(4);
        }

        String finalDomain = domain;
        LpgeCodeDto lpgeCodeDto = CommonCodeCache.getLpgeCodes().stream().filter(dto -> finalDomain.equals(dto.getCodeValue())).findFirst().orElse(null);
        if(lpgeCodeDto == null) result.setLpgeCode("Registration required");
        else result.setLpgeCode(lpgeCodeDto.getCodeFullName());

        return result;
    }

    @Transactional
    public String registerLandingPage(CreateDto createDto) throws Exception {
        UserBaseDto createUser = userBaseService.getUserBaseById(createDto.getCreateId());
        UserBaseDto companyUser = userBaseService.getUserBaseById(createDto.getUserId());
        List<LpgeCodeDto> lpgeCodes = CommonCodeCache.getLpgeCodes();

        LpgeCodeDto lpgeCodeDto = lpgeCodes.stream().filter(dto -> createDto.getDomain().equals(dto.getCodeValue())).findFirst().orElse(null);

        EncryptionUtil encryptionUtil = new EncryptionUtil();
        String encriptPw = encryptionUtil.getSHA256WithSalt(createDto.getPassword(), createUser.getSalt());
        if(!encriptPw.equals(createUser.getUserPw())){
            return "fail - Password is different";
        }

        if("C".equals(createDto.getInputMode())){
            if(lpgeCodeDto != null){
                return "fail - Domain duplicate";
            }

            LpgeCodeDto newLpgeCodeDto = new LpgeCodeDto();
            newLpgeCodeDto.setCodeName(lpgeCodes.stream().mapToInt(LpgeCodeDto::getCodeName).max().orElse(0)+1);
            newLpgeCodeDto.setCodeValue(createDto.getDomain());
            newLpgeCodeDto.setCodeDescript(createDto.getPageDescription());
            newLpgeCodeDto.create(createUser.getIdx());

            lpgeCodeDto = lpgeCodeService.save(newLpgeCodeDto);

            LandingPageDto landingPageDto = new LandingPageDto();
            landingPageDto.setLpgeCode(lpgeCodeDto.getCodeFullName());
            landingPageDto.setDomain(createDto.getDomain());
            landingPageDto.create(createUser.getIdx());
            landingPageRepositoryService.saveLandingPage(landingPageDto);

        }

        UserCdbtMappingDto userLpgeMappingDto = new UserCdbtMappingDto();
        userLpgeMappingDto.setCdbtLowCode(lpgeCodeDto.getCodeFullName());
        userLpgeMappingDto.setUserId(companyUser.getIdx());
        userLpgeMappingDto.setCdbtCode("LPGE");

        try {
            userCdbtMappingService.save(userLpgeMappingDto);
        } catch (Exception e) {
            return "fail - CustUserMapping fail";
        }


        return "success";
    }

    @Transactional
    public List<String> getBlockedKeyword(String lpgeCode) throws Exception{
        return landingPageBlockedKeywordService.getLandingPageBlockedKeywordByLpgeCode(lpgeCode).stream()
                .filter(LandingPageBlockedKeywordDto::getUseYn)
                .filter(dto -> !dto.getDelYn())
                .map(LandingPageBlockedKeywordDto::getKeyword)
                .toList();
    }

    @Transactional
    public String registerCustData(String ip, CustDataDto custDataDto) throws Exception{
        boolean ipChecker = false;
        if(custDataDto.getInputMode().equals("local")){
            ip = "127.0.0.1";
        }

        List<LandingPageBlockedIpDto> landingPageBlockedIpByLpgeCode = landingPageBlockedIpService.getLandingPageBlockedIpByLpgeCode(custDataDto.getLpgeCode()).stream()
                .filter(LandingPageBlockedIpDto::getUseYn)
                .filter(dto -> !dto.getDelYn())
                .toList();

        for(int i = 0; i < landingPageBlockedIpByLpgeCode.size(); i++){
            String regIp = "";
            regIp = regIp + String.valueOf(landingPageBlockedIpByLpgeCode.get(i).getIp1());
            regIp = regIp + String.valueOf(landingPageBlockedIpByLpgeCode.get(i).getIp2());
            regIp = regIp + String.valueOf(landingPageBlockedIpByLpgeCode.get(i).getIp3());
            regIp = regIp + String.valueOf(landingPageBlockedIpByLpgeCode.get(i).getIp4());
            if(ip.equals(regIp)){
                ipChecker = true;
            }
        }

        if(ipChecker){
            return "fail - Blocked IP";
        }

        CustomerInformationDto customerInformationDto = new CustomerInformationDto();
        customerInformationDto.setCdbtLowCode(custDataDto.getLpgeCode());
        customerInformationDto.setUtmSourse(custDataDto.getUtmSource());
        customerInformationDto.setUtmMedium(custDataDto.getUtmMedium());
        customerInformationDto.setUtmCampaign(custDataDto.getUtmCampaign());
        customerInformationDto.setUtmTerm(custDataDto.getUtmTerm());
        customerInformationDto.setUtmContent(custDataDto.getUtmContent());
        customerInformationDto.setIp(ip);
        customerInformationDto.create(CommonCodeCache.getSystemIdIdx());

        CustomerInformationDto newCustomerInformationDto = customerInformationService.save(customerInformationDto);

        EncryptionUtil encryptionUtil = new EncryptionUtil();
        custDataDto.getData().forEach(map->{
            CustomerBasicConsultationDto customerBasicConsultationDto = new CustomerBasicConsultationDto();

            customerBasicConsultationDto.setCustId(newCustomerInformationDto.getIdx());
            customerBasicConsultationDto.setKey(map.get("key"));
            customerBasicConsultationDto.setValue(encryptionUtil.AES256encrypt(map.get("value")));
            customerBasicConsultationDto.create(CommonCodeCache.getSystemIdIdx());
            customerBasicConsultationService.save(customerBasicConsultationDto);
        });

        return "success";
    }

}
