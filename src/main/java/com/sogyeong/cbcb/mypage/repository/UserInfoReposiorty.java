package com.sogyeong.cbcb.mypage.repository;

import com.sogyeong.cbcb.mypage.entity.UserInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserInfoReposiorty  extends JpaRepository<UserInfo,Long> {
}
