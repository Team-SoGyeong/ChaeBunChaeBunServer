package com.sogyeong.cbcb.auth.wotoken.service;

import com.sogyeong.cbcb.auth.wotoken.model.dto.UserInfoDTO;
import com.sogyeong.cbcb.auth.wotoken.model.dto.UserLoginDTO;
import com.sogyeong.cbcb.mypage.entity.UserInfo;
import com.sogyeong.cbcb.mypage.entity.UserLogin;
import com.sogyeong.cbcb.mypage.repository.UserInfoRepository;
import com.sogyeong.cbcb.mypage.repository.UserLoginRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
@AllArgsConstructor
public class AuthService {
    private UserLoginRepository userLoginRepository;
    private UserInfoRepository userInfoRepository;

    @PersistenceContext
    EntityManager em;

    @Transactional(readOnly = false)
    public long kakaoSignin(UserLoginDTO userLoginDTO, UserInfoDTO userInfoDTO){
        int lastSeq = userLoginRepository.getLastSeq();
        UserLogin userLogin = userLoginRepository.save(userLoginDTO.toEntity());
        userInfoDTO.setInfoId(userLogin.getSeq());
        UserInfo userInfo = userInfoRepository.save(userInfoDTO.toEntity());

        //유저 아이디 반환
        if(userLogin.getSeq()>lastSeq && userLogin.getSeq()==userInfo.getSeq())
            return userLogin.getSeq();
        else return -1;
    }

    @Transactional
    public Boolean updateSignoutDate(long userId){
        Optional<UserLogin> info = userLoginRepository.findById(userId);
        info.ifPresent(loginInfo ->{
            loginInfo.setUpdateDate(LocalDateTime.now());
            userLoginRepository.save(loginInfo);
        });
        return info.stream().count() > 0 ? true : false;
    }
}
