package com.datamon.datamon2.servcie.logic.test;

import com.datamon.datamon2.dto.repository.CustomerBasicConsultationDto;
import com.datamon.datamon2.dto.repository.CustomerInformationDto;
import com.datamon.datamon2.dto.repository.DbDuplicateDataProcessingDto;
import com.datamon.datamon2.servcie.repository.CustomerBasicConsultationService;
import com.datamon.datamon2.servcie.repository.CustomerInformationService;
import com.datamon.datamon2.servcie.repository.DbDuplicationDataProcessingService;
import com.datamon.datamon2.util.EncryptionUtil;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class TestService {
    private DbDuplicationDataProcessingService dbDuplicationDataProcessingService;
    private CustomerInformationService customerInformationService;
    private CustomerBasicConsultationService customerBasicConsultationService;

    public TestService(DbDuplicationDataProcessingService dbDuplicationDataProcessingService, CustomerInformationService customerInformationService, CustomerBasicConsultationService customerBasicConsultationService) {
        this.dbDuplicationDataProcessingService = dbDuplicationDataProcessingService;
        this.customerInformationService = customerInformationService;
        this.customerBasicConsultationService = customerBasicConsultationService;
    }

    public List<Object> uuidTest(String dbCode){
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
