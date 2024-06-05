package com.datamon.datamon2.servcie.logic;

import com.datamon.datamon2.common.CommonCodeCache;
import com.datamon.datamon2.dto.input.landingPage.CreateDto;
import com.datamon.datamon2.dto.input.landingPage.CustDataDto;
import com.datamon.datamon2.dto.output.landingPage.InitalDataDto;
import com.datamon.datamon2.dto.repository.*;
import com.datamon.datamon2.servcie.repository.*;
import com.datamon.datamon2.util.EncryptionUtil;
import com.datamon.datamon2.util.InstantUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;

@Service("LandingPageLogicService")
public class LandingPageService {
    private UserBaseService userBaseService;
    private LpgeCodeService lpgeCodeService;
    private com.datamon.datamon2.servcie.repository.LandingPageService landingPageRepositoryService;
    private UserLpgeMappingService userLpgeMappingService;
    private LandingPageBlockedIpService landingPageBlockedIpService;
    private LandingPageBlockedKeywordService landingPageBlockedKeywordService;
    private CustomerInformationService customerInformationService;
    private CustomerBasicConsultationService customerBasicConsultationService;
    private InstantUtil instantUtil = new InstantUtil();

    public LandingPageService(UserBaseService userBaseService, LpgeCodeService lpgeCodeService, com.datamon.datamon2.servcie.repository.LandingPageService landingPageRepositoryService, UserLpgeMappingService userLpgeMappingService, LandingPageBlockedIpService landingPageBlockedIpService, LandingPageBlockedKeywordService landingPageBlockedKeywordService, CustomerInformationService customerInformationService, CustomerBasicConsultationService customerBasicConsultationService) {
        this.userBaseService = userBaseService;
        this.lpgeCodeService = lpgeCodeService;
        this.landingPageRepositoryService = landingPageRepositoryService;
        this.userLpgeMappingService = userLpgeMappingService;
        this.landingPageBlockedIpService = landingPageBlockedIpService;
        this.landingPageBlockedKeywordService = landingPageBlockedKeywordService;
        this.customerInformationService = customerInformationService;
        this.customerBasicConsultationService = customerBasicConsultationService;
    }

    @Transactional
    public InitalDataDto getInitialData(String domain) throws Exception{
        InitalDataDto result = new InitalDataDto();
        LpgeCodeDto lpgeCodeDto = CommonCodeCache.getLpgeCodes().stream().filter(dto -> domain.contains(dto.getCodeValue())).findFirst().orElse(null);
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
            newLpgeCodeDto.setCodeFullName("LPGE_" + String.format("%010d", newLpgeCodeDto.getCodeName()));
            newLpgeCodeDto.setCodeValue(createDto.getDomain());
            newLpgeCodeDto.setCodeDescript(createDto.getPageDescription());
            newLpgeCodeDto.setUseYn(true);
            newLpgeCodeDto.setDelYn(false);
            newLpgeCodeDto.setCreateId(createUser.getIdx());
            newLpgeCodeDto.setCreateDate(instantUtil.getNow());
            newLpgeCodeDto.setModifyId(createUser.getIdx());
            newLpgeCodeDto.setModifyDate(instantUtil.getNow());
            if(!"local".equals(createDto.getInputMode())){
                lpgeCodeDto = lpgeCodeService.saveLpgeCode(newLpgeCodeDto);

                LandingPageDto landingPageDto = new LandingPageDto();
                landingPageDto.setLpgeCode(lpgeCodeDto.getCodeFullName());
                landingPageDto.setDomain(createDto.getDomain());
                landingPageDto.setUseYn(true);
                landingPageDto.setDelYn(false);
                landingPageDto.setCreateId(createUser.getIdx());
                landingPageDto.setCreateDate(instantUtil.getNow());
                landingPageDto.setModifyId(createUser.getIdx());
                landingPageDto.setModifyDate(instantUtil.getNow());
                landingPageRepositoryService.saveLandingPage(landingPageDto);
            }

        }

        if(!"local".equals(createDto.getInputMode())){
            UserLpgeMappingDto userLpgeMappingDto = new UserLpgeMappingDto();
            userLpgeMappingDto.setLpgeCode(lpgeCodeDto.getCodeFullName());
            userLpgeMappingDto.setUserId(companyUser.getIdx());

            try {
                userLpgeMappingService.saveUserLpgeMapping(userLpgeMappingDto);
            } catch (Exception e) {
                return "fail - CustUserMapping fail";
            }
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

        if(!"local".equals(custDataDto.getInputMode())){
            CustomerInformationDto customerInformationDto = new CustomerInformationDto();
            customerInformationDto.setLpgeCode(custDataDto.getLpgeCode());
            customerInformationDto.setUtmSourse(custDataDto.getUtmSource());
            customerInformationDto.setUtmMedium(custDataDto.getUtmMedium());
            customerInformationDto.setUtmCampaign(custDataDto.getUtmCampaign());
            customerInformationDto.setUtmTerm(custDataDto.getUtmTerm());
            customerInformationDto.setUtmContent(custDataDto.getUtmContent());
            customerInformationDto.setIp(ip);
            customerInformationDto.setUseYn(true);
            customerInformationDto.setDelYn(false);
            customerInformationDto.setCreateId(CommonCodeCache.getSystemIdIdx());
            customerInformationDto.setCreateDate(instantUtil.getNow());
            customerInformationDto.setModifyId(CommonCodeCache.getSystemIdIdx());
            customerInformationDto.setModifyDate(instantUtil.getNow());

            CustomerInformationDto newCustomerInformationDto = customerInformationService.saveCustomerInformation(customerInformationDto);

            custDataDto.getData().forEach(map->{
                CustomerBasicConsultationDto customerBasicConsultationDto = new CustomerBasicConsultationDto();

                customerBasicConsultationDto.setCustId(newCustomerInformationDto.getIdx());
                customerBasicConsultationDto.setKey(map.get("key"));
                customerBasicConsultationDto.setValue(map.get("value"));
                customerBasicConsultationDto.setCreateId(CommonCodeCache.getSystemIdIdx());
                customerBasicConsultationDto.setCreateDate(instantUtil.getNow());
                customerBasicConsultationDto.setModifyId(CommonCodeCache.getSystemIdIdx());
                customerBasicConsultationDto.setModiftyDate(instantUtil.getNow());

                customerBasicConsultationService.saveCustomerBasicConsultation(customerBasicConsultationDto);
            });
        }

        return "success";
    }

}
