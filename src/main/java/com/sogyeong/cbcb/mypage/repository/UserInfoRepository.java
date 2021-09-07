package com.sogyeong.cbcb.mypage.repository;

import com.sogyeong.cbcb.mypage.entity.UserInfo;
import com.sogyeong.cbcb.mypage.entity.UserLogin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

public interface UserInfoRepository extends JpaRepository<UserInfo,Long> {
    @Query(value = "select EXISTS (select * from user_info where nickname = :nickname and info_id != :userId)", nativeQuery = true)
    Integer existsByNicknameExceptMe(long userId, String nickname);

    Boolean existsByNickname(String nickname);

    @Query(value = "select info_id from user_info where email = :email", nativeQuery = true)
    Long findIdByEmail(String email);

    @Query(value = "select EXISTS (select * from user_info ui join user_login ul on ui.`info_id`=ul.`login_id`  where email = :email and ul.`login_type`= :loginType limit 1)", nativeQuery = true)
    Integer checkLoginStatus(String loginType, String email);

}
