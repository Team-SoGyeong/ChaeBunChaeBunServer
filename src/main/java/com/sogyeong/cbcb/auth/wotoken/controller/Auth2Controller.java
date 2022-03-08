package com.sogyeong.cbcb.auth.wotoken.controller;

import com.sogyeong.cbcb.auth.wotoken.model.dto.UserInfoDTO;
import com.sogyeong.cbcb.auth.wotoken.model.dto.UserLoginDTO;
import com.sogyeong.cbcb.auth.wotoken.model.vo.CheckSigninVO;
import com.sogyeong.cbcb.auth.wotoken.model.vo.SigninVO;
import com.sogyeong.cbcb.auth.wotoken.service.AuthService;
import com.sogyeong.cbcb.defaults.entity.response.BasicResponse;
import com.sogyeong.cbcb.defaults.entity.response.CommonResponse;
import com.sogyeong.cbcb.defaults.entity.response.ErrorResponse;
import com.sogyeong.cbcb.defaults.entity.response.ResultMessage;
import com.sogyeong.cbcb.mypage.repository.UserInfoRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

@RestController
@RequestMapping("/auth2")
@AllArgsConstructor
public class Auth2Controller {

    UserInfoRepository userInfoRepository;
    private AuthService authService;

    @PersistenceContext
    private EntityManager em;

    //카카오 로그인
    @PostMapping("/signin/kakao")
    public ResponseEntity<? extends BasicResponse> kakaoSignin(@RequestBody SigninVO SVO){
        UserLoginDTO userLoginDTO = new UserLoginDTO();
        UserInfoDTO userInfoDTO = new UserInfoDTO();

        userLoginDTO.setLoginType(SVO.getLogin_type());
        userLoginDTO.setKakaoId(SVO.getKakao_id());
        userInfoDTO.setNickname(SVO.getNickname());
        userInfoDTO.setAddress(SVO.getAddress_seq());
        userInfoDTO.setProfile(SVO.getProfile());
        userInfoDTO.setSet_image(SVO.getSet_image());
        userInfoDTO.setEmail(SVO.getEmail());

        if(SVO.getAge_range().contains("1~9")) userInfoDTO.setAgeRange(0);
        else if(SVO.getAge_range().contains("10~14")) userInfoDTO.setAgeRange(1);
        else if(SVO.getAge_range().contains("15~19")) userInfoDTO.setAgeRange(2);
        else if(SVO.getAge_range().contains("20~29")) userInfoDTO.setAgeRange(3);
        else if(SVO.getAge_range().contains("30~39")) userInfoDTO.setAgeRange(4);
        else if(SVO.getAge_range().contains("40~49")) userInfoDTO.setAgeRange(5);
        else if(SVO.getAge_range().contains("50~59")) userInfoDTO.setAgeRange(6);
        else if(SVO.getAge_range().contains("60~69")) userInfoDTO.setAgeRange(7);
        else if(SVO.getAge_range().contains("70~79")) userInfoDTO.setAgeRange(8);
        else if(SVO.getAge_range().contains("80~89")) userInfoDTO.setAgeRange(9);
        else if(SVO.getAge_range().contains("90~")) userInfoDTO.setAgeRange(10);

        if(SVO.getSex().equals("male")) userInfoDTO.setSex(0);
        else userInfoDTO.setSex(1);

        long isSave = authService.kakaoSignin(userLoginDTO, userInfoDTO);
        if(isSave!=-1){
            List list = new ArrayList();
            LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
            map.put("user_id", isSave);
            list.add(map);

            return ResponseEntity.ok().body(new CommonResponse<>(list, ResultMessage.SUCCESS_KAKAO.getVal()));
        }
        else
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).
                    body(new ErrorResponse(ResultMessage.FAILED_KAKAO.getVal()));
    }

    //카카오 로그인 내역 확인
    @PostMapping("/signin/kakao/checkLogin")
    public ResponseEntity<? extends BasicResponse> checkLoginStatus(@RequestBody CheckSigninVO CVO){
        int isLogin = userInfoRepository.checkLoginStatus(CVO.getLogin_type(), CVO.getKakao_id());

        if (isLogin==0) {
            List res = new ArrayList();
            LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
            map.put("isLogin", false);
            map.put("userId", null);
            res.add(map);

            return ResponseEntity.ok().
                    body(new CommonResponse(res, ResultMessage.NOT_FOUND_LOG.getVal()));
        }
        else{
            long userId = userInfoRepository.findIdByKakaoId(CVO.getKakao_id());

            List res = new ArrayList();
            LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
            map.put("isLogin", true);
            map.put("userId", userId);
            res.add(map);

            return ResponseEntity.ok().
                    body(new CommonResponse(res, ResultMessage.FOUND_LOG.getVal()));
        }

    }

    //로그인 시 닉네임 중복확인
    @GetMapping("/signin/kakao/{nickname}")
    public ResponseEntity<? extends BasicResponse> checkNicknameAtSignin(@PathVariable("nickname") String nickname){
        boolean isNickname = userInfoRepository.existsByNickname(nickname);

        LinkedHashMap<String, Object> res = new LinkedHashMap<String, Object>();
        if(isNickname){
            res.put("isUsable", false);
            return ResponseEntity.ok().body(new CommonResponse(res, ResultMessage.CAN_USE_NIK.getVal()));
        }
        else{
            res.put("isUsable", true);
            return ResponseEntity.ok().body(new CommonResponse(res, ResultMessage.CANT_USE_NIK.getVal()));
        }
    }

    //로그아웃 - 최종 접속 시간 PUT
    @PutMapping("/signout/{userId}")
    public ResponseEntity<? extends BasicResponse> signout(@PathVariable("userId") long userId){
        boolean isUser = userInfoRepository.existsById(userId);
        if (!isUser) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ErrorResponse(ResultMessage.UNDEFINED_USER.getVal()));
        }
        else{
            Boolean isChange = authService.updateSignoutDate(userId);
            if(isChange)
                return  ResponseEntity.ok().body( new CommonResponse(ResultMessage.SUCCESS_LOGOUT.getVal()));
            else
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(new ErrorResponse(ResultMessage.FAILED_LOGOUT.getVal()));
        }
    }
}
