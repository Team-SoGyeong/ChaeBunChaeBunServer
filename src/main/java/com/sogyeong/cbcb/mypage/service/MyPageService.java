package com.sogyeong.cbcb.mypage.service;

import com.sogyeong.cbcb.mypage.entity.UserInfo;
import com.sogyeong.cbcb.mypage.repository.UserInfoReposiorty;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Optional;

@Service
@AllArgsConstructor
public class MyPageService {
    @PersistenceContext
    private EntityManager em;

    UserInfoReposiorty userInfoReposiorty;

    @Transactional
    public Boolean updateProfile(long userId, String image, String nickname){
        Optional<UserInfo> info = userInfoReposiorty.findById(userId);
        info.ifPresent(profileInfo ->{
            profileInfo.setUrl(image);
            profileInfo.setNickname(nickname);
            userInfoReposiorty.save(profileInfo);
        });
        return info.stream().count() > 0 ? true : false;
    }
}
