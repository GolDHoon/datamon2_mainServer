package com.datamon.datamon2.servcie.logic;

import com.datamon.datamon2.common.CommonCodeCache;
import com.datamon.datamon2.dto.repository.*;
import com.datamon.datamon2.servcie.repository.CustomerBasicConsultationService;
import com.datamon.datamon2.servcie.repository.CustomerInformationService;
import com.datamon.datamon2.servcie.repository.UserBaseService;
import com.datamon.datamon2.servcie.repository.UserCdbtMappingService;
import com.datamon.datamon2.util.HttpSessionUtil;
import com.datamon.datamon2.util.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class CommonService {
    private JwtUtil jwtUtil;
    private UserCdbtMappingService userCdbtMappingService;
    private UserBaseService userBaseService;
    private CustomerInformationService customerInformationService;
    private CustomerBasicConsultationService customerBasicConsultationService;

    public CommonService(JwtUtil jwtUtil, UserCdbtMappingService userCdbtMappingService, UserBaseService userBaseService, CustomerInformationService customerInformationService, CustomerBasicConsultationService customerBasicConsultationService) {
        this.jwtUtil = jwtUtil;
        this.userCdbtMappingService = userCdbtMappingService;
        this.userBaseService = userBaseService;
        this.customerInformationService = customerInformationService;
        this.customerBasicConsultationService = customerBasicConsultationService;
    }

    @Transactional
    public List<Map<String, String>> getDBList(HttpServletRequest request) throws Exception{
        List<Map<String, String>> result = new ArrayList<>();
        HttpSessionUtil httpSessionUtil = new HttpSessionUtil(request.getSession(false));

        int userId = jwtUtil.getUserId(httpSessionUtil.getAttribute("jwt").toString());

        userCdbtMappingService.getUserCdbtListByUserId(userId).forEach(dto -> {
            Map<String, String> map = new HashMap<>();
            switch (dto.getCdbtCode()){
                case "LPGE":
                    List<LpgeCodeDto> lpgecodes = CommonCodeCache.getLpgeCodes().stream()
                            .filter(lpgeCode -> lpgeCode.getCodeFullName().equals(dto.getCdbtLowCode()))
                            .collect(Collectors.toList());
                    if(lpgecodes.size() > 0){
                        map.put("DBName", lpgecodes.get(0).getCodeValue());
                        map.put("code", lpgecodes.get(0).getCodeFullName());
                        map.put("Type", dto.getCdbtCode());
                    }
                    result.add(map);
                    break;
                default:
                    break;
            }
        });

        return result;
    }

    @Transactional
    public Map<String, String> getUserType (HttpServletRequest request) throws Exception{
        Map<String, String> result = new HashMap<>();
        HttpSessionUtil httpSessionUtil = new HttpSessionUtil(request.getSession(false));
        Object jwt = httpSessionUtil.getAttribute("jwt");

        if(jwt != null){
            int userId = jwtUtil.getUserId(jwt.toString());

            UserBaseDto userBaseById = userBaseService.getUserBaseById(userId);

            result.put("userType", userBaseById.getUserType());
        }

        return result;
    }

    @Transactional
    public List<String> getColumnList (String cdbtLowCode) throws Exception{
        List<String> customerIdList = customerInformationService.getCustomerInformationByCdbtLowCode(cdbtLowCode).stream()
                .filter(CustomerInformationDto::getUseYn)
                .filter(dto -> !dto.getDelYn())
                .map(dto -> {
                    return dto.getIdx();
                })
                .collect(Collectors.toList());

        List<String> columnList = customerBasicConsultationService.getCustomerBasicConsultationByCustIdList(customerIdList).stream()
                .filter(dto -> dto.getDeleteId() == null)
                .map(dto -> dto.getKey())
                .distinct()
                .collect(Collectors.toList());

        return columnList;
    }

    @Transactional
    public List<Map<String, Object>> getCdbsCode (String mode) throws Exception {
        List<Map<String, Object>> result = new ArrayList<>();
        List<CdbsCodeDto> cdbsCodes = CommonCodeCache.getCdbsCodes();
        List<String> excludedItems = new ArrayList<>();
        List<String> includedItems = new ArrayList<>();

        switch (mode) {
            case "outboundListInit":
                excludedItems.add("CDBS_PNDG");
                excludedItems.add("CDBS_ICST");
                excludedItems.add("CDBS_ACPL");
                result = cdbsCodes.stream()
                        .filter(code -> !excludedItems.contains(code.getCodeFullName()))
                        .map(code -> {
                            Map<String, Object> resultMap = new HashMap<>();

                            resultMap.put("key", code.getCodeValue());
                            resultMap.put("value", code.getCodeFullName());
                            return resultMap;
                        })
                        .collect(Collectors.toList());
                break;
            case "outboundListNextNotAllow":
                includedItems.add("CDBS_EXCS");
                includedItems.add("CDBS_MNTG");
                includedItems.add("CDBS_FLSI");
                result = cdbsCodes.stream()
                        .filter(code -> includedItems.contains(code.getCodeFullName()))
                        .map(code -> {
                            Map<String, Object> resultMap = new HashMap<>();

                            resultMap.put("key", code.getCodeValue());
                            resultMap.put("value", code.getCodeFullName());
                            return resultMap;
                        })
                        .collect(Collectors.toList());
                break;
            default:
                break;
        }

        return result;
    }

    @Transactional
    public List<Map<String, Object>> getCdbqCode () throws Exception {
        return CommonCodeCache.getCdbqCodes().stream()
                .map(code -> {
                    Map<String, Object> resultMap = new HashMap<>();

                    resultMap.put("key", code.getCodeValue());
                    resultMap.put("value", code.getCodeFullName());
                    return resultMap;
                })
                .collect(Collectors.toList());
    }
}
