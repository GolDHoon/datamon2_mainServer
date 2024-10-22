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

import java.util.*;
import java.util.stream.Collectors;

@Service("LandingPageLogicService")
public class LandingPageService {
    private UserBaseService userBaseService;
    private LpgeCodeService lpgeCodeService;
    private UserCdbtMappingService userCdbtMappingService;
    private LandingPageBlockedIpService landingPageBlockedIpService;
    private LandingPageBlockedKeywordService landingPageBlockedKeywordService;
    private CustomerInformationService customerInformationService;
    private CustomerBasicConsultationService customerBasicConsultationService;
    private TableIndexService tableIndexService;
    private DbDuplicationDataProcessingService dbDuplicationDataProcessingService;

    public LandingPageService(UserBaseService userBaseService, LpgeCodeService lpgeCodeService, UserCdbtMappingService userCdbtMappingService, LandingPageBlockedIpService landingPageBlockedIpService, LandingPageBlockedKeywordService landingPageBlockedKeywordService, CustomerInformationService customerInformationService, CustomerBasicConsultationService customerBasicConsultationService, TableIndexService tableIndexService, DbDuplicationDataProcessingService dbDuplicationDataProcessingService) {
        this.userBaseService = userBaseService;
        this.lpgeCodeService = lpgeCodeService;
        this.userCdbtMappingService = userCdbtMappingService;
        this.landingPageBlockedIpService = landingPageBlockedIpService;
        this.landingPageBlockedKeywordService = landingPageBlockedKeywordService;
        this.customerInformationService = customerInformationService;
        this.customerBasicConsultationService = customerBasicConsultationService;
        this.tableIndexService = tableIndexService;
        this.dbDuplicationDataProcessingService = dbDuplicationDataProcessingService;
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
            regIp = regIp + String.valueOf(landingPageBlockedIpByLpgeCode.get(i).getIp1())+".";
            regIp = regIp + String.valueOf(landingPageBlockedIpByLpgeCode.get(i).getIp2())+".";
            regIp = regIp + String.valueOf(landingPageBlockedIpByLpgeCode.get(i).getIp3())+".";
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
        TableIndexDto tableIndexByOptionName = tableIndexService.getTableIndexByOptionName(custDataDto.getLpgeCode());
        customerInformationDto.setIdx(tableIndexByOptionName.getOptionName()+"-"+String.format("%020d", tableIndexByOptionName.getIndex()));
        customerInformationDto.setCdbsCode("CDBS_PNDG");
        customerInformationDto.setCdbqCode("CDBQ_UVDA");
        customerInformationDto.setUtmSource(custDataDto.getUtmSource());
        customerInformationDto.setUtmMedium(custDataDto.getUtmMedium());
        customerInformationDto.setUtmCampaign(custDataDto.getUtmCampaign());
        customerInformationDto.setUtmTerm(custDataDto.getUtmTerm());
        customerInformationDto.setUtmContent(custDataDto.getUtmContent());
        customerInformationDto.setIp(ip);
        customerInformationDto.create(CommonCodeCache.getSystemIdIdx());

        CustomerInformationDto newCustomerInformationDto = customerInformationService.save(customerInformationDto);

        tableIndexByOptionName.setIndex(tableIndexByOptionName.getIndex()+1L);
        tableIndexService.save(tableIndexByOptionName);

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

    public List<Object> getDuplicateRemovalPreprocessingList(String dbCode){
        Map<Integer, List<DbDuplicateDataProcessingDto>> groupedByKeyGroupNoDtoList = dbDuplicationDataProcessingService.getByDbCode(dbCode).stream()
                .collect(Collectors.groupingBy(DbDuplicateDataProcessingDto::getKeyGroupNo));
        List<String> custIdxList = customerInformationService.getCustomerInformationByCdbtLowCode(dbCode).stream()
                .map(CustomerInformationDto::getIdx)
                .collect(Collectors.toList());
        Map<String, List<CustomerBasicConsultationDto>> keyList = customerBasicConsultationService.getCustomerBasicConsultationByCustIdList(custIdxList).stream()
                .collect(Collectors.groupingBy(CustomerBasicConsultationDto::getCustId));
        EncryptionUtil encryptionUtil = new EncryptionUtil();

        List<Object> result = new ArrayList<>();

        for (Map.Entry<Integer, List<DbDuplicateDataProcessingDto>> entry : groupedByKeyGroupNoDtoList.entrySet()) {
            Integer key = entry.getKey();
            List<String> columns = new ArrayList<>();
            List<Map<String, String>> rows = new ArrayList<>();

            entry.getValue().forEach(value -> {
                if(value.getPreprocessingYn()){
                    columns.add(value.getKey());

                    for (Map.Entry<String, List<CustomerBasicConsultationDto>> entry2 : keyList.entrySet()) {
                        Map<String, String> row = rows.stream()
                                .filter(r -> r.get("custId").equals(entry2.getKey()))
                                .findFirst()
                                .orElse(new HashMap<>());

                        if (!row.containsKey("custId")) {
                            row.put("custId", entry2.getKey());
                        }

                        CustomerBasicConsultationDto valueDto = entry2.getValue().stream()
                                .filter(dto -> dto.getCustId().equals(entry2.getKey()) && dto.getKey().equals(value.getKey()))
                                .findFirst()
                                .orElse(new CustomerBasicConsultationDto());

                        if (valueDto.getIdx() != null) {
                            row.put(value.getKey(), valueDto.getValue());
                        }

                        if (!rows.contains(row)) {
                            rows.add(row);
                        }
                    }
                }
            });

            List<Map<String, String>> resultRows = rows.stream()
                    .map(row -> {
                        Map<String, String> resultRow = new HashMap<>();
                        Set<String> uniqueValues = new HashSet<>();
                        row.forEach((k, v) -> {
                            if (!k.equals("custId") && uniqueValues.add(encryptionUtil.AES256decrypt(v))) {
                                resultRow.put(k, encryptionUtil.AES256decrypt(v));
                            }
                        });
                        return resultRow;
                    })
                    .collect(Collectors.toList());

            Map<String, Object> tempResult = new HashMap<>();
            tempResult.put("row", resultRows);
            tempResult.put("columns", columns);
            result.add(tempResult);
        }
        return result;
    }
}
