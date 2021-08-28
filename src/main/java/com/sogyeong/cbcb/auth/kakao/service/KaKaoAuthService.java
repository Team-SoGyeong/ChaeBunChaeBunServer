package com.sogyeong.cbcb.auth.kakao.service;

import com.sogyeong.cbcb.auth.common.exception.ResourceAlreadyExistsException;
import com.sogyeong.cbcb.auth.common.exception.UserNotFoundException;
import com.sogyeong.cbcb.auth.common.model.response.AuthResponse;
import com.sogyeong.cbcb.auth.common.model.response.SignUpResponse;
import com.sogyeong.cbcb.auth.common.security.service.JwtTokenProvider;
import com.sogyeong.cbcb.auth.common.service.AuthService;
import com.sogyeong.cbcb.auth.kakao.client.KaKaoClient;
import com.sogyeong.cbcb.auth.kakao.model.KaKaoUser;

import com.sogyeong.cbcb.mypage.entity.UserInfo;
import com.sogyeong.cbcb.mypage.entity.UserLogin;
import com.sogyeong.cbcb.mypage.repository.UserInfoRepository;
import com.sogyeong.cbcb.mypage.repository.UserLoginRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class KaKaoAuthService implements AuthService {
    @Autowired
    private KaKaoClient kakaoClient;

    @Autowired
    private UserInfoRepository userRepository;

    @Autowired
    private UserLoginRepository loginRepository;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Transactional
    public SignUpResponse signup(String accessToken) {
        KaKaoUser kaKaoUser = kakaoClient.profile(accessToken);

        try {
            System.out.println(kaKaoUser.toString());
            this.validateDuplicateUser(kaKaoUser.getId());

            userRepository.save(
                    UserInfo.builder()
                            .seq(kaKaoUser.getId())
                            .nickname(kaKaoUser.getProperties().getNickname())
                            .url(kaKaoUser.getProperties().getProfile_image())
                            .joinDate(LocalDateTime.now())
                            .build());

            return new SignUpResponse(
                    jwtTokenProvider.createToken(kaKaoUser.getId()),
                    kaKaoUser.getId(),
                    kaKaoUser.getProperties().getNickname(),
                    kaKaoUser.getKakao_account().getEmail(),
                    kaKaoUser.getProperties().getProfile_image()
            );
        } catch (DataIntegrityViolationException e) {
            throw new ResourceAlreadyExistsException("Already use exists " + accessToken);
        }
    }

    public AuthResponse signin(String accessToken) {
        KaKaoUser kaKaoUser = kakaoClient.profile(accessToken);

        UserInfo appUserInfo = userRepository.findById(kaKaoUser.getId()).get();

        if (appUserInfo == null) {
            throw new UserNotFoundException("user not found");
        }

        return new AuthResponse(
                jwtTokenProvider.createToken(appUserInfo.getSeq()),
                appUserInfo.getSeq(),
                appUserInfo.getNickname()
        );
    }

    public AuthResponse refresh(Long userNo) {
        return new AuthResponse(
                jwtTokenProvider.createToken(userNo),
                null,
                null
        );
    }

    @Override
    public void validateDuplicateUser(Long userNo) {
        Optional<UserInfo> userInfo = Optional.ofNullable(userRepository.findById(userNo).get());
        userInfo.ifPresent(findUser -> {
            throw new ResourceAlreadyExistsException("Already user exists");
        });
    }
}
