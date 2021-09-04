package com.sogyeong.cbcb.mypage.repository;

import com.sogyeong.cbcb.mypage.entity.UserLogin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserLoginRepository extends JpaRepository<UserLogin,Long> {
    @Query(value = "select case when count(login_id) = 0 then 0 else login_id end as lastSeq from user_login order by login_id desc ", nativeQuery = true)
    int getLastSeq();
}
