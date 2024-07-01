package com.datamon.datamon2.servcie.logic;

import com.datamon.datamon2.common.CommonCodeCache;
import com.datamon.datamon2.dto.input.landingPageManage.BlockIpDto;
import com.datamon.datamon2.dto.input.landingPageManage.BlockKeywordDto;
import com.datamon.datamon2.dto.input.landingPageManage.LandingPageCreateDto;
import com.datamon.datamon2.dto.repository.*;
import com.datamon.datamon2.servcie.repository.*;
import com.datamon.datamon2.util.HttpSessionUtil;
import com.datamon.datamon2.util.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class LandingPageManageService {
    private JwtUtil jwtUtil;
    private UserBaseService userBaseService;
    private CompanyInfomationService companyInfomationService;
    private MemberInfomationService memberInfomationService;
    private UserCdbtMappingService userCdbtMappingService;
    private LpgeCodeService lpgeCodeService;
    private LandingPageBlockedIpService landingPageBlockedIpService;
    private LandingPageBlockedKeywordService landingPageBlockedKeywordService;
    private UserPermissionInfomationService userPermissionInfomationService;

    public LandingPageManageService(JwtUtil jwtUtil, UserBaseService userBaseService, CompanyInfomationService companyInfomationService, MemberInfomationService memberInfomationService, UserCdbtMappingService userCdbtMappingService, LpgeCodeService lpgeCodeService, LandingPageBlockedIpService landingPageBlockedIpService, LandingPageBlockedKeywordService landingPageBlockedKeywordService, UserPermissionInfomationService userPermissionInfomationService) {
        this.jwtUtil = jwtUtil;
        this.userBaseService = userBaseService;
        this.companyInfomationService = companyInfomationService;
        this.memberInfomationService = memberInfomationService;
        this.userCdbtMappingService = userCdbtMappingService;
        this.lpgeCodeService = lpgeCodeService;
        this.landingPageBlockedIpService = landingPageBlockedIpService;
        this.landingPageBlockedKeywordService = landingPageBlockedKeywordService;
        this.userPermissionInfomationService = userPermissionInfomationService;
    }

    @Transactional
    public Map<String, List> getList(HttpServletRequest request) throws Exception{
        Map<String, List> result = new HashMap<>();
        HttpSessionUtil httpSessionUtil = new HttpSessionUtil(request.getSession(false));

        int userId = jwtUtil.getUserId(httpSessionUtil.getAttribute("jwt").toString());

        List<String> lpgeCodeList = userCdbtMappingService.getUserCdbtListByUserId(userId).stream()
                .filter(dto-> dto.getCdbtCode().equals("LPGE"))
                .map(UserCdbtMappingDto::getCdbtLowCode)
                .collect(Collectors.toList());

        List<Map<String, String>> rows = CommonCodeCache.getLpgeCodes().stream()
                .filter(dto -> lpgeCodeList.contains(dto.getCodeFullName()))
                .map(dto -> {
                    Map<String, String> map = new HashMap<>();
                    map.put("도메인", dto.getCodeValue());
                    map.put("도메인 설명", dto.getCodeDescript());
                    map.put("사용유무", dto.getUseYn().toString());
                    map.put("code", dto.getCodeFullName());
                    return map;
                })
                .collect(Collectors.toList());

        List<String> keyList = new ArrayList<>();

        keyList.add("도메인");
        keyList.add("도메인 설명");
        keyList.add("사용유무");

        result.put("rows",rows);
        result.put("keyList", keyList);

        return result;
    }

    @Transactional
    public String createLpge(HttpServletRequest request, LandingPageCreateDto landingPageCreateDto) throws Exception{
        HttpSessionUtil httpSessionUtil = new HttpSessionUtil(request.getSession(false));

        int userId = jwtUtil.getUserId(httpSessionUtil.getAttribute("jwt").toString());


        List<String> masterCodes = CommonCodeCache.getMasterCodes().stream()
                .map(dto -> {
                    return dto.getCodeFullName();
                })
                .collect(Collectors.toList());

        List<String> companyCodes = CommonCodeCache.getCompanyCode().stream()
                .map(dto -> {
                    return dto.getCodeFullName();
                })
                .collect(Collectors.toList());

        List<String> memeberCodes = CommonCodeCache.getMemberCodes().stream()
                .map(dto -> {
                    return dto.getCodeFullName();
                })
                .collect(Collectors.toList());

        LpgeCodeDto lpgeCodeDto = new LpgeCodeDto();
        lpgeCodeDto.setCodeName(CommonCodeCache.getLpgeCodes().size()+1);
        lpgeCodeDto.setCodeValue(landingPageCreateDto.getDomain());
        lpgeCodeDto.setCodeDescript(landingPageCreateDto.getDescription());
        lpgeCodeDto.create(userId);

        LpgeCodeDto save = lpgeCodeService.save(lpgeCodeDto);

        UserCdbtMappingDto userCdbtMappingDto = new UserCdbtMappingDto();
        userCdbtMappingDto.setCdbtLowCode(save.getCodeFullName());
        userCdbtMappingDto.setCdbtCode("LPGE");

        List<UsatCodeDto> usatCodes = CommonCodeCache.getUsatCodes().stream()
                .filter(dto -> !CommonCodeCache.getCommonPermissionCodes().stream().map(dto2 -> {
                    return dto2.getCodeFullName();
                }).collect(Collectors.toList()).contains(dto.getCodeFullName()))
                .collect(Collectors.toList());

        UserPermissionInfomationDto userPermissionInfomationDto = new UserPermissionInfomationDto();
        userPermissionInfomationDto.setCdbtLowCode(save.getCodeFullName());
        userPermissionInfomationDto.setCdbtCode("LPGE");
        userPermissionInfomationDto.create(userId);

        UserBaseDto userBaseById = userBaseService.getUserBaseById(userId);

        List<UserBaseDto> masterIds = userBaseService.getUserBaseByUserTypeList(masterCodes);

        if(memeberCodes.contains(userBaseById.getUserType())){
            userCdbtMappingDto.setUserId(userId);
            userCdbtMappingService.save(userCdbtMappingDto);

            userPermissionInfomationDto.setUserId(userId);
            userPermissionInfomationDto.setUsatCode("AUTH_USAT_0000000002");
            userPermissionInfomationService.save(userPermissionInfomationDto);

            usatCodes.forEach(dto -> {
                userPermissionInfomationDto.setUsatCode(dto.getCodeFullName());
                userPermissionInfomationService.save(userPermissionInfomationDto);
            });

            MemberInfomationDto memberInfomationByUserId = memberInfomationService.getMemberInfomationByUserId(userId);
            CompanyInfomationDto companyInfomationById = companyInfomationService.getCompanyInfomationById(memberInfomationByUserId.getCompanyId());
            userCdbtMappingDto.setUserId(companyInfomationById.getUserId());
            userCdbtMappingService.save(userCdbtMappingDto);

            userPermissionInfomationDto.setUserId(companyInfomationById.getUserId());
            userPermissionInfomationDto.setUsatCode("AUTH_USAT_0000000001");
            userPermissionInfomationService.save(userPermissionInfomationDto);

            usatCodes.forEach(dto -> {
                userPermissionInfomationDto.setUsatCode(dto.getCodeFullName());
                userPermissionInfomationService.save(userPermissionInfomationDto);
            });

            masterIds.forEach(dto -> {
                userCdbtMappingDto.setUserId(dto.getIdx());
                userCdbtMappingService.save(userCdbtMappingDto);

                userPermissionInfomationDto.setUserId(dto.getIdx());
                userPermissionInfomationDto.setUsatCode("AUTH_USAT_0000000001");
                userPermissionInfomationService.save(userPermissionInfomationDto);

                usatCodes.forEach(dto2 -> {
                    userPermissionInfomationDto.setUsatCode(dto2.getCodeFullName());
                    userPermissionInfomationService.save(userPermissionInfomationDto);
                });
            });
        }else if(companyCodes.contains(userBaseById.getUserType())){
            userCdbtMappingDto.setUserId(userId);
            userCdbtMappingService.save(userCdbtMappingDto);

            userPermissionInfomationDto.setUserId(userId);
            userPermissionInfomationDto.setUsatCode("AUTH_USAT_0000000001");
            userPermissionInfomationService.save(userPermissionInfomationDto);

            masterIds.forEach(dto -> {
                userCdbtMappingDto.setUserId(dto.getIdx());
                userCdbtMappingService.save(userCdbtMappingDto);

                userPermissionInfomationDto.setUserId(dto.getIdx());
                userPermissionInfomationDto.setUsatCode("AUTH_USAT_0000000001");
                userPermissionInfomationService.save(userPermissionInfomationDto);

                usatCodes.forEach(dto2 -> {
                    userPermissionInfomationDto.setUsatCode(dto2.getCodeFullName());
                    userPermissionInfomationService.save(userPermissionInfomationDto);
                });
            });
        }else {
            masterIds.forEach(dto -> {
                userCdbtMappingDto.setUserId(dto.getIdx());
                userCdbtMappingService.save(userCdbtMappingDto);

                userPermissionInfomationDto.setUserId(dto.getIdx());
                userPermissionInfomationDto.setUsatCode("AUTH_USAT_0000000001");
                userPermissionInfomationService.save(userPermissionInfomationDto);

                usatCodes.forEach(dto2 -> {
                    userPermissionInfomationDto.setUsatCode(dto2.getCodeFullName());
                    userPermissionInfomationService.save(userPermissionInfomationDto);
                });
            });
        }

        return "success";
    }

    @Transactional
    public List<String> getBlockedIpList(String lpgeCode) throws Exception{
        List<String> result = new ArrayList<>();
        landingPageBlockedIpService.getLandingPageBlockedIpByLpgeCode(lpgeCode).stream()
                .filter(LandingPageBlockedIpDto::getUseYn)
                .filter(dto -> !dto.getDelYn())
                .collect(Collectors.toList())
                .forEach(dto -> {
                    String blokedIp = "";
                    blokedIp = blokedIp + String.valueOf(dto.getIp1());
                    blokedIp = blokedIp + "." + String.valueOf(dto.getIp2());
                    blokedIp = blokedIp + "." + String.valueOf(dto.getIp3());
                    blokedIp = blokedIp + "." + String.valueOf(dto.getIp4());

                    result.add(blokedIp);
                });
        return result;
    }

    @Transactional
    public String deleteBlockedIp(BlockIpDto blockIpDto, HttpServletRequest request) throws Exception{
        HttpSessionUtil httpSessionUtil = new HttpSessionUtil(request.getSession(false));

        int userId = jwtUtil.getUserId(httpSessionUtil.getAttribute("jwt").toString());

        String[] split = blockIpDto.getBlockIp().split("\\.");
        List<Short> ips = new ArrayList<>();

        for(int i = 0; i < split.length; i++){
            ips.add((short) Integer.parseInt(split[i]));
        }

        List<LandingPageBlockedIpDto> blockedIpList = landingPageBlockedIpService.getLandingPageBlockedIpByLpgeCode(blockIpDto.getLpgeCode()).stream()
                .filter(LandingPageBlockedIpDto::getUseYn)
                .filter(dto -> Objects.equals(ips.get(0), dto.getIp1()))
                .filter(dto -> Objects.equals(ips.get(1), dto.getIp2()))
                .filter(dto -> Objects.equals(ips.get(2), dto.getIp3()))
                .filter(dto -> Objects.equals(ips.get(3), dto.getIp4()))
                .collect(Collectors.toList());

        if(blockedIpList.size() == 0) {
            return "not found Ip";
        }

        blockedIpList.forEach(dto -> {
            if(!dto.getDelYn()){
                dto.setDelYn(true);
                dto.delete(userId);
                landingPageBlockedIpService.saveLandingPageBlockedIp(dto);
            }
        });

        return "success";
    }

    @Transactional
    public String registerBlockedIp(BlockIpDto blockIpDto, HttpServletRequest request) throws Exception{
        HttpSessionUtil httpSessionUtil = new HttpSessionUtil(request.getSession(false));

        int userId = jwtUtil.getUserId(httpSessionUtil.getAttribute("jwt").toString());

        String[] split = blockIpDto.getBlockIp().split("\\.");
        List<Short> ips = new ArrayList<>();

        for(int i = 0; i < split.length; i++){
            ips.add((short) Integer.parseInt(split[i]));
        }

        List<LandingPageBlockedIpDto> blockedIpList = landingPageBlockedIpService.getLandingPageBlockedIpByLpgeCode(blockIpDto.getLpgeCode()).stream()
                .filter(LandingPageBlockedIpDto::getUseYn)
                .filter(dto -> Objects.equals(ips.get(0), dto.getIp1()))
                .filter(dto -> Objects.equals(ips.get(1), dto.getIp2()))
                .filter(dto -> Objects.equals(ips.get(2), dto.getIp3()))
                .filter(dto -> Objects.equals(ips.get(3), dto.getIp4()))
                .collect(Collectors.toList());

        if (blockedIpList.size() > 0){
            blockedIpList.forEach(dto -> {
                if(dto.getDelYn()){
                    dto.setDelYn(false);
                    dto.modify(userId);
                    landingPageBlockedIpService.saveLandingPageBlockedIp(dto);
                }
            });
        }else{
            LandingPageBlockedIpDto landingPageBlockedIpDto = new LandingPageBlockedIpDto();
            landingPageBlockedIpDto.setLpgeCode(blockIpDto.getLpgeCode());
            landingPageBlockedIpDto.setIp1(ips.get(0));
            landingPageBlockedIpDto.setIp2(ips.get(1));
            landingPageBlockedIpDto.setIp3(ips.get(2));
            landingPageBlockedIpDto.setIp4(ips.get(3));
            landingPageBlockedIpDto.create(userId);
            landingPageBlockedIpService.saveLandingPageBlockedIp(landingPageBlockedIpDto);
        }

        return "success";
    }

    @Transactional
    public List<String> getBlockedKeywordList(String lpgeCode) throws Exception{
        List<String> result = new ArrayList<>();
        landingPageBlockedKeywordService.getLandingPageBlockedKeywordByLpgeCode(lpgeCode).stream()
                .filter(LandingPageBlockedKeywordDto::getUseYn)
                .filter(dto -> !dto.getDelYn())
                .collect(Collectors.toList())
                .forEach(dto -> {
                    result.add(dto.getKeyword());
                });
        return result;
    }

    @Transactional
    public String deleteBlockedKeyword(BlockKeywordDto blockKeywordDto, HttpServletRequest request) throws Exception{
        HttpSessionUtil httpSessionUtil = new HttpSessionUtil(request.getSession(false));

        int userId = jwtUtil.getUserId(httpSessionUtil.getAttribute("jwt").toString());

        List<LandingPageBlockedKeywordDto> blockedKeywordList = landingPageBlockedKeywordService.getLandingPageBlockedKeywordByLpgeCode(blockKeywordDto.getLpgeCode()).stream()
                .filter(LandingPageBlockedKeywordDto::getUseYn)
                .filter(dto -> !dto.getDelYn())
                .filter(dto -> dto.getKeyword().equals(blockKeywordDto.getKeyword()))
                .collect(Collectors.toList());

        if(blockedKeywordList.size() == 0){
            return "not found Ip";
        }

        blockedKeywordList.forEach(dto -> {
            if(!dto.getDelYn()){
                dto.setDelYn(true);
                dto.delete(userId);
            }
            landingPageBlockedKeywordService.saveLandingPageBlockedKeyword(dto);
        });

        return "success";
    }

    @Transactional
    public String registerBlockedKeyword(BlockKeywordDto blockKeywordDto, HttpServletRequest request) throws Exception{
        HttpSessionUtil httpSessionUtil = new HttpSessionUtil(request.getSession(false));

        int userId = jwtUtil.getUserId(httpSessionUtil.getAttribute("jwt").toString());

        List<LandingPageBlockedKeywordDto> blockedKeywordList = landingPageBlockedKeywordService.getLandingPageBlockedKeywordByLpgeCode(blockKeywordDto.getLpgeCode()).stream()
                .filter(LandingPageBlockedKeywordDto::getUseYn)
                .filter(dto -> !dto.getDelYn())
                .filter(dto -> dto.getKeyword().equals(blockKeywordDto.getKeyword()))
                .collect(Collectors.toList());

        if(blockedKeywordList.size() > 0){
            blockedKeywordList.forEach(dto -> {
                dto.setDelYn(false);
                dto.delete(userId);
                landingPageBlockedKeywordService.saveLandingPageBlockedKeyword(dto);
            });
        }else{
            LandingPageBlockedKeywordDto landingPageBlockedKeywordDto = new LandingPageBlockedKeywordDto();
            landingPageBlockedKeywordDto.setLpgeCode(blockKeywordDto.getLpgeCode());
            landingPageBlockedKeywordDto.setKeyword(blockKeywordDto.getKeyword());
            landingPageBlockedKeywordDto.create(userId);
            landingPageBlockedKeywordService.saveLandingPageBlockedKeyword(landingPageBlockedKeywordDto);
        }

        return "success";
    }
}
