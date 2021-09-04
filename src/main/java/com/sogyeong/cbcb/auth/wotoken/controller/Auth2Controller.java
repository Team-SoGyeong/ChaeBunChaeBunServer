package com.sogyeong.cbcb.auth.wotoken.controller;

import com.sogyeong.cbcb.auth.wotoken.model.dto.UserInfoDTO;
import com.sogyeong.cbcb.auth.wotoken.model.dto.UserLoginDTO;
import com.sogyeong.cbcb.auth.wotoken.model.vo.SigninVO;
import com.sogyeong.cbcb.auth.wotoken.service.AuthService;
import com.sogyeong.cbcb.defaults.entity.response.BasicResponse;
import com.sogyeong.cbcb.defaults.entity.response.CommonResponse;
import com.sogyeong.cbcb.defaults.entity.response.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

@RestController
@RequestMapping("/auth2")
public class Auth2Controller {
    private AuthService authService;

    @PostMapping("/signin/kakao")
    public ResponseEntity<? extends BasicResponse> kakaoSignin(@RequestBody SigninVO SVO){
        UserLoginDTO userLoginDTO = new UserLoginDTO();
        UserInfoDTO userInfoDTO = new UserInfoDTO();

        userLoginDTO.setLoginType(SVO.getLogin_type());
        userInfoDTO.setNickname(SVO.getNickname());
        userInfoDTO.setAddress(SVO.getAddress_seq());
        userInfoDTO.setProfile(SVO.getProfile());
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

            return ResponseEntity.ok().body(new CommonResponse<>(list, "카카오 로그인 성공"));
        }
        else
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).
                    body(new ErrorResponse("카카오 로그인 실패"));
    }
}
