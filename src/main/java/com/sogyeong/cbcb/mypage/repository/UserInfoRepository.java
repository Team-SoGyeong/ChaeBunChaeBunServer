package com.sogyeong.cbcb.mypage.repository;

import com.sogyeong.cbcb.mypage.entity.UserInfo;
import com.sogyeong.cbcb.mypage.entity.UserLogin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

public interface UserInfoRepository extends JpaRepository<UserInfo,Long> {

    Boolean existsByNickname(String nickname);

    UserInfo findOneBySeq(long authorId);
}
