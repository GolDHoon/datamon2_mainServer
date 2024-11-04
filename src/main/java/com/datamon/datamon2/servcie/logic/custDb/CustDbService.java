package com.datamon.datamon2.servcie.logic.custDb;

import com.datamon.datamon2.dto.input.custDb.BlockedIpCopyDto;
import com.datamon.datamon2.dto.input.custDb.BlockedIpInfoDto;
import com.datamon.datamon2.dto.input.custDb.LpgeCodeCreateDto;
import com.datamon.datamon2.dto.output.common.ColumnInfo;
import com.datamon.datamon2.dto.output.common.ErrorOutputDto;
import com.datamon.datamon2.dto.output.common.SuccessOutputDto;
import com.datamon.datamon2.dto.output.custDb.BlockedIpCopyOutputDto;
import com.datamon.datamon2.dto.output.custDb.GetCustDbCodeListOutputDto;
import com.datamon.datamon2.dto.output.custDb.GetLpgeCodeInfoOutputDto;
import com.datamon.datamon2.dto.output.custDb.GetLpgeDbListOutputDto;
import com.datamon.datamon2.dto.repository.*;
import com.datamon.datamon2.servcie.logic.member.MemberService;
import com.datamon.datamon2.servcie.repository.*;
import com.datamon.datamon2.util.HttpSessionUtil;
import com.datamon.datamon2.util.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class CustDbService {
    private MemberInfomationService memberInfomationService;
    private UserBaseService userBaseService;
    private JwtUtil jwtUtil;
    private UserCdbtMappingService userCdbtMappingService;
    private LpgeCodeService lpgeCodeService;
    private LandingPageBlockedIpService landingPageBlockedIpService;
    private LandingPageBlockedKeywordService landingPageBlockedKeywordService;
    private DbDuplicationDataProcessingService dbDuplicationDataProcessingService;
    private CustomerInformationService customerInformationService;
    private CustomerBasicConsultationService customerBasicConsultationService;
    private LandingPageInfomationService landingPageInfomationService;

    public CustDbService(JwtUtil jwtUtil, UserCdbtMappingService userCdbtMappingService, LpgeCodeService lpgeCodeService, LandingPageBlockedIpService landingPageBlockedIpService, LandingPageBlockedKeywordService landingPageBlockedKeywordService, DbDuplicationDataProcessingService dbDuplicationDataProcessingService, CustomerInformationService customerInformationService, CustomerBasicConsultationService customerBasicConsultationService, LandingPageInfomationService landingPageInfomationService, MemberInfomationService memberInfomationService, UserBaseService userBaseService) {
        this.jwtUtil = jwtUtil;
        this.userCdbtMappingService = userCdbtMappingService;
        this.lpgeCodeService = lpgeCodeService;
        this.landingPageBlockedIpService = landingPageBlockedIpService;
        this.landingPageBlockedKeywordService = landingPageBlockedKeywordService;
        this.dbDuplicationDataProcessingService = dbDuplicationDataProcessingService;
        this.customerInformationService = customerInformationService;
        this.customerBasicConsultationService = customerBasicConsultationService;
        this.landingPageInfomationService = landingPageInfomationService;
        this.memberInfomationService = memberInfomationService;
        this.userBaseService = userBaseService;
    }

    @Transactional
    public Map<String, Object> copyBlockedIp(HttpServletRequest request, BlockedIpCopyDto blockedIpCopyDto) throws Exception{
        HttpSessionUtil httpSessionUtil = new HttpSessionUtil(request.getSession(false));
        BlockedIpCopyOutputDto blockedIpCopyOutputDto = new BlockedIpCopyOutputDto();
        ErrorOutputDto errorOutputDto = new ErrorOutputDto();
        Map<String, Object> result = new HashMap<>();
        result.put("result", "E");

        int userId = jwtUtil.getUserId(httpSessionUtil.getAttribute("jwt").toString());

        List<LandingPageBlockedIpDto> targetIpList = landingPageBlockedIpService.getLandingPageBlockedIpByLpgeCode(blockedIpCopyDto.getTargetDbCode());
        List<LandingPageBlockedIpDto> applyIpList = landingPageBlockedIpService.getLandingPageBlockedIpByLpgeCode(blockedIpCopyDto.getApplyDbCode());

        targetIpList.forEach(target -> {
            List<LandingPageBlockedIpDto> applyIpCheck = applyIpList.stream()
                .filter(apply -> Objects.equals(apply.getIp1(), target.getIp1()))
                .filter(apply -> Objects.equals(apply.getIp2(), target.getIp2()))
                .filter(apply -> Objects.equals(apply.getIp3(), target.getIp3()))
                .filter(apply -> Objects.equals(apply.getIp4(), target.getIp4()))
                .toList();

            if(applyIpCheck.size() > 0){
                blockedIpCopyOutputDto.setDuplCnt(blockedIpCopyOutputDto.getDuplCnt()+1);
                return;
            }

            LandingPageBlockedIpDto landingPageBlockedIpDto = new LandingPageBlockedIpDto();
            landingPageBlockedIpDto.setLpgeCode(blockedIpCopyDto.getApplyDbCode());
            landingPageBlockedIpDto.setIp1(target.getIp1());
            landingPageBlockedIpDto.setIp2(target.getIp2());
            landingPageBlockedIpDto.setIp3(target.getIp3());
            landingPageBlockedIpDto.setIp4(target.getIp4());
            landingPageBlockedIpDto.create(userId);

            landingPageBlockedIpService.saveLandingPageBlockedIp(landingPageBlockedIpDto);
            blockedIpCopyOutputDto.setCopyCnt(blockedIpCopyOutputDto.getCopyCnt()+1);
        });

        result.put("result", "S");
        result.put("output", blockedIpCopyOutputDto);
        return result;
    }

    @Transactional
    public Map<String, Object> createBlockedIp(HttpServletRequest request, BlockedIpInfoDto blockedIpInfoDto) throws Exception{
        HttpSessionUtil httpSessionUtil = new HttpSessionUtil(request.getSession(false));
        SuccessOutputDto successOutputDto = new SuccessOutputDto();
        ErrorOutputDto errorOutputDto = new ErrorOutputDto();
        Map<String, Object> result = new HashMap<>();
        result.put("result", "E");

        int userId = jwtUtil.getUserId(httpSessionUtil.getAttribute("jwt").toString());

        String[] ipSegments = blockedIpInfoDto.getIp().split("\\.");
        short[] ipShortArray = new short[ipSegments.length];
        for (int i = 0; i < ipSegments.length; i++) {
            ipShortArray[i] = Short.parseShort(ipSegments[i]);
        }

        LandingPageBlockedIpDto landingPageBlockedIpDto = new LandingPageBlockedIpDto();
        landingPageBlockedIpDto.setLpgeCode(blockedIpInfoDto.getDbCode());
        landingPageBlockedIpDto.setIp1(ipShortArray[0]);
        landingPageBlockedIpDto.setIp2(ipShortArray[1]);
        landingPageBlockedIpDto.setIp3(ipShortArray[2]);
        landingPageBlockedIpDto.setIp4(ipShortArray[3]);

        landingPageBlockedIpDto.create(userId);
        landingPageBlockedIpService.saveLandingPageBlockedIp(landingPageBlockedIpDto);

        result.put("result", "S");
        result.put("output", successOutputDto);
        return result;
    }

    @Transactional
    public Map<String, Object> deleteBlockedIp(BlockedIpInfoDto blockedIpInfoDto) throws Exception{
        SuccessOutputDto successOutputDto = new SuccessOutputDto();
        ErrorOutputDto errorOutputDto = new ErrorOutputDto();
        Map<String, Object> result = new HashMap<>();
        result.put("result", "E");

        String[] ipSegments = blockedIpInfoDto.getIp().split("\\.");
        short[] ipShortArray = new short[ipSegments.length];
        for (int i = 0; i < ipSegments.length; i++) {
            ipShortArray[i] = Short.parseShort(ipSegments[i]);
        }

        List<LandingPageBlockedIpDto> ipList = landingPageBlockedIpService.getLandingPageBlockedIpByLpgeCode(blockedIpInfoDto.getDbCode()).stream()
                .filter(ip -> Objects.equals(ip.getIp1(),ipShortArray[0]))
                .filter(ip -> Objects.equals(ip.getIp2(),ipShortArray[1]))
                .filter(ip -> Objects.equals(ip.getIp3(),ipShortArray[2]))
                .filter(ip -> Objects.equals(ip.getIp4(),ipShortArray[3]))
                .toList();

        ipList.forEach(ip -> {
            landingPageBlockedIpService.deleteLandingPageBlockedIpById(ip.getIdx());
        });

        result.put("result", "S");
        result.put("output", successOutputDto);
        return result;
    }

    @Transactional
    public Map<String, Object> getLpgeCodeInfo(HttpServletRequest request, int idx, int companyId) throws Exception{
        HttpSessionUtil httpSessionUtil = new HttpSessionUtil(request.getSession(false));
        GetLpgeCodeInfoOutputDto getLpgeCodeInfoOutputDto = new GetLpgeCodeInfoOutputDto();
        ErrorOutputDto errorOutputDto = new ErrorOutputDto();
        Map<String, Object> result = new HashMap<>();
        result.put("result", "E");

        getLpgeCodeInfoOutputDto.setDuplColumnInfo(new HashMap<>());
        getLpgeCodeInfoOutputDto.setBlockIpList(new ArrayList<>());
        getLpgeCodeInfoOutputDto.setBlockKeywordList(new ArrayList<>());
        getLpgeCodeInfoOutputDto.setLandingInfo(new HashMap<>());
        getLpgeCodeInfoOutputDto.setLandingMappingInfoList(new ArrayList<>());

        LpgeCodeDto lpgeCodeDto = lpgeCodeService.getLpgeCodeFindById(idx);
        getLpgeCodeInfoOutputDto.setCode(lpgeCodeDto.getCodeFullName());

        //중복제거 정보
        Map<String, Object> duplRemoverData = new HashMap<>();

        Map<Integer, List<DbDuplicateDataProcessingDto>> groupedByKeyGroupNo = dbDuplicationDataProcessingService.getByDbCode(lpgeCodeDto.getCodeFullName()).stream()
                .collect(Collectors.groupingBy(DbDuplicateDataProcessingDto::getKeyGroupNo));

        List<Map<String, Object>> duplRemoverList = new ArrayList<>();
        groupedByKeyGroupNo.forEach((key, value) -> {
            Map<String, Object> duplRemover = new HashMap<>();
            duplRemover.put("ketGroup", key);
            List<DbDuplicateDataProcessingDto> DbDuplicateDataProcessingDtoList =  value.stream()
                .filter(dupl -> Objects.equals(dupl.getKeyGroupNo(), key))
                .toList();

            List<Map<String, Object>> duplRemoverInfoList = new ArrayList<>();
            DbDuplicateDataProcessingDtoList.forEach(Info -> {
                Map<String, Object> duplRemoverInfo = new HashMap<>();
                duplRemoverInfo.put("column", Info.getKey());
                duplRemoverInfo.put("postPrc", Info.getPostprocessingYn());
                duplRemoverInfo.put("prePrc", Info.getPreprocessingYn());
                duplRemoverInfoList.add(duplRemoverInfo);
            });
            duplRemover.put("infoList", duplRemoverInfoList);
            duplRemoverList.add(duplRemover);
        });
        getLpgeCodeInfoOutputDto.getDuplColumnInfo().put("duplRemover", duplRemoverList);

        Set<String> keySet = new HashSet<>();
        customerInformationService.getCustomerInformationByCdbtLowCode(lpgeCodeDto.getCodeFullName()).forEach(custInfo -> {
            customerBasicConsultationService.getCustomerBasicConsultationByCustId(custInfo.getIdx()).forEach(customCustInfo -> {
                keySet.add(customCustInfo.getKey());
            });
        });

        // 필요하다면 다시 List로 변환
        List<String> keyList = new ArrayList<>(keySet);
        getLpgeCodeInfoOutputDto.getDuplColumnInfo().put("keyList", keyList);


        //ip 차단 리스트
        landingPageBlockedIpService.getLandingPageBlockedIpByLpgeCode(lpgeCodeDto.getCodeFullName()).stream()
            .filter(LandingPageBlockedIpDto::getUseYn)
            .filter(ip -> !ip.getDelYn())
            .toList()
            .forEach(ip -> {
                String ipStr = String.valueOf(ip.getIp1());
                ipStr = ipStr + ".";
                ipStr = ipStr + String.valueOf(ip.getIp2());
                ipStr = ipStr + ".";
                ipStr = ipStr + String.valueOf(ip.getIp3());
                ipStr = ipStr + ".";
                ipStr = ipStr + String.valueOf(ip.getIp4());
                getLpgeCodeInfoOutputDto.getBlockIpList().add(ipStr);
            });

        //키워드 차단 리스트
        landingPageBlockedKeywordService.getLandingPageBlockedKeywordByLpgeCode(lpgeCodeDto.getCodeFullName()).stream()
            .filter(LandingPageBlockedKeywordDto::getUseYn)
            .filter(keyword -> !keyword.getDelYn())
            .toList()
            .forEach(keyword -> {
                getLpgeCodeInfoOutputDto.getBlockKeywordList().add(keyword.getKeyword());
            });

        //랜딩페이지 정보
        LandingPageInfomationDto landingPageInfomationDto = landingPageInfomationService.getLandingPageInfomationByLpgeCode(lpgeCodeDto.getCodeFullName());
        getLpgeCodeInfoOutputDto.getLandingInfo().put("head", landingPageInfomationDto.getHead());
        getLpgeCodeInfoOutputDto.getLandingInfo().put("body", landingPageInfomationDto.getBody());
        getLpgeCodeInfoOutputDto.getLandingInfo().put("title", landingPageInfomationDto.getTitle());
        getLpgeCodeInfoOutputDto.getLandingInfo().put("description", landingPageInfomationDto.getDescription());

        //랜딩페이지 매핑 정보
        List<MemberInfomationDto> memberInfomationDtoList = memberInfomationService.getMemberInfomationDtoListByCompanyId(companyId);
        List<UserCdbtMappingDto> userCdbtMappingDtoList = userCdbtMappingService.getUserCdbtListByCdbtLowCode(lpgeCodeDto.getCodeFullName()).stream()
                        .filter(mapping -> memberInfomationDtoList.stream().map(MemberInfomationDto::getUserId).toList().contains(mapping.getUserId()))
                        .toList();

        memberInfomationDtoList.stream()
                .filter(member -> userCdbtMappingDtoList.stream().map(UserCdbtMappingDto::getUserId).toList().contains(member.getUserId()))
                .forEach(member -> {
            Map<String, Object> memberInfo = new HashMap<>();
            memberInfo.put("name", member.getName());
            memberInfo.put("role", member.getRole());
            getLpgeCodeInfoOutputDto.getLandingMappingInfoList().add(memberInfo);
        });

        result.put("result", "S");
        result.put("output", getLpgeCodeInfoOutputDto);
        return result;
    }
    
    @Transactional
    public Map<String, Object> createLpgeCode(HttpServletRequest request, LpgeCodeCreateDto lpgeCodeCreateDto) throws Exception{
        HttpSessionUtil httpSessionUtil = new HttpSessionUtil(request.getSession(false));
        SuccessOutputDto successOutputDto = new SuccessOutputDto();
        ErrorOutputDto errorOutputDto = new ErrorOutputDto();
        Map<String, Object> result = new HashMap<>();
        result.put("result", "E");

        int userId = jwtUtil.getUserId(httpSessionUtil.getAttribute("jwt").toString());

        LpgeCodeDto lpgeCodeDto = new LpgeCodeDto();
        lpgeCodeDto.setCodeValue(lpgeCodeCreateDto.getUrl());
        lpgeCodeDto.setCodeDescript(lpgeCodeCreateDto.getDescription());

        LpgeCodeDto maxLpgeCodeDto = lpgeCodeService.getLpgeCodeAll().stream()
                .max(Comparator.comparingInt(LpgeCodeDto::getCodeName))
                .orElse(new LpgeCodeDto());


        if(maxLpgeCodeDto.getIdx() == null){
            lpgeCodeDto.setCodeName(1);
        }else{
            lpgeCodeDto.setCodeName(maxLpgeCodeDto.getCodeName()+1);
        }

        lpgeCodeDto.create(userId);
        LpgeCodeDto saveCode = null;

        try{
            saveCode = lpgeCodeService.save(lpgeCodeDto);
        } catch (Exception ex) {
            errorOutputDto.setCode(500);
            errorOutputDto.setDetailReason("이미 등록되어 있는 도메인입니다.");
            result.put("output", errorOutputDto);
            return result;
        }

        UserCdbtMappingDto userCdbtMappingDto = new UserCdbtMappingDto();
        userCdbtMappingDto.setCdbtLowCode(saveCode.getCodeFullName());
        userCdbtMappingDto.setUserId(userId);
        userCdbtMappingDto.setCdbtCode("LPGE");
        userCdbtMappingService.save(userCdbtMappingDto);

        successOutputDto.setCode(200);
        successOutputDto.setMessage("랜딩페이지 정보가 생성되었습니다.");
        result.put("result", "S");
        result.put("output", successOutputDto);
        return result;
    }

    @Transactional
    public Map<String, Object> getLpgeDbList(HttpServletRequest request) throws Exception{
        HttpSessionUtil httpSessionUtil = new HttpSessionUtil(request.getSession(false));
        Map<String, Object> result = new HashMap<>();
        GetLpgeDbListOutputDto getLpgeDbListOutputDto = new GetLpgeDbListOutputDto();
        getLpgeDbListOutputDto.setDataList(new ArrayList<>());
        getLpgeDbListOutputDto.setColumnInfoList(new ArrayList<>());

        int userId = jwtUtil.getUserId(httpSessionUtil.getAttribute("jwt").toString());

        List<String> userAndDbCodeMapping = userCdbtMappingService.getUserCdbtListByUserId(userId).stream()
                .filter(mapping -> mapping.getCdbtCode().equals("LPGE"))
                .map(UserCdbtMappingDto::getCdbtLowCode)
                .toList();

        ColumnInfo columnInfo = new ColumnInfo();

        columnInfo.setColumnType("basic");
        columnInfo.setFilterType("text");
        columnInfo.setName("url");
        columnInfo.setKey("url");
        getLpgeDbListOutputDto.getColumnInfoList().add(columnInfo);

        columnInfo = new ColumnInfo();
        columnInfo.setColumnType("basic");
        columnInfo.setFilterType("text");
        columnInfo.setName("설명");
        columnInfo.setKey("description");
        getLpgeDbListOutputDto.getColumnInfoList().add(columnInfo);

        columnInfo = new ColumnInfo();
        columnInfo.setColumnType("basic");
        columnInfo.setFilterType("select");
        columnInfo.setName("사용여부");
        columnInfo.setKey("useYn");
        getLpgeDbListOutputDto.getColumnInfoList().add(columnInfo);

        columnInfo = new ColumnInfo();
        columnInfo.setColumnType("basic");
        columnInfo.setFilterType("date");
        columnInfo.setName("생성일");
        columnInfo.setKey("createDate");
        getLpgeDbListOutputDto.getColumnInfoList().add(columnInfo);

        columnInfo = new ColumnInfo();
        columnInfo.setColumnType("basic");
        columnInfo.setFilterType("date");
        columnInfo.setName("수정일");
        columnInfo.setKey("modifyDate");
        getLpgeDbListOutputDto.getColumnInfoList().add(columnInfo);

        List<LpgeCodeDto> lpgeCodeDtoList = lpgeCodeService.getLpgeCodeAll().stream()
                .filter(code -> userAndDbCodeMapping.contains(code.getCodeFullName()))
                .sorted(Comparator.comparing(LpgeCodeDto::getModifyDate).reversed())
                .toList();

        lpgeCodeDtoList.forEach(code -> {
            Map<String, Object> data = new HashMap<>();
            data.put("idx", code.getIdx());
            data.put("dbCode", code.getCodeFullName());
            data.put("url", code.getCodeValue());
            data.put("description", code.getCodeDescript());
            data.put("useYn", code.getUseYn()?"사용중":"미사용");
            data.put("createDate", code.getCreateDate());
            data.put("modifyDate", code.getModifyDate());

            getLpgeDbListOutputDto.getDataList().add(data);
        });

        result.put("result", "S");
        result.put("output", getLpgeDbListOutputDto);

        return result;
    }

    @Transactional
    public Map<String, Object> getCustDBCodeList(HttpServletRequest request) throws Exception{
        HttpSessionUtil httpSessionUtil = new HttpSessionUtil(request.getSession(false));
        Map<String, Object> result = new HashMap<>();
        List<GetCustDbCodeListOutputDto> outputDtoList = new ArrayList<>();

        int userId = jwtUtil.getUserId(httpSessionUtil.getAttribute("jwt").toString());

        List<String> userAndDbCodeMapping = userCdbtMappingService.getUserCdbtListByUserId(userId).stream()
                .map(UserCdbtMappingDto::getCdbtLowCode)
                .toList();

        List<LpgeCodeDto> lpgeDbCodeList = lpgeCodeService.getLpgeCodeAll().stream()
                .filter(code -> userAndDbCodeMapping.contains(code.getCodeFullName()))
                .toList();

        outputDtoList.add(new GetCustDbCodeListOutputDto());
        outputDtoList.get(0).setCustDbType("CDBT_LPGE");
        outputDtoList.get(0).setCustDbCodeList(new ArrayList<>());
        lpgeDbCodeList.forEach(code -> {
            Map<String, String> dbCode = new HashMap<>();
            dbCode.put(code.getCodeFullName(), code.getCodeValue());
            dbCode.put("key", code.getCodeFullName());
            outputDtoList.get(0).getCustDbCodeList().add(dbCode);
        });

        result.put("result", "S");
        result.put("output", outputDtoList);

        return result;
    }
}
