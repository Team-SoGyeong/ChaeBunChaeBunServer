package com.sogyeong.cbcb.mypage.repository;

import com.sogyeong.cbcb.mypage.entity.UserLogin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserLoginRepository extends JpaRepository<UserLogin,Long> {

}
