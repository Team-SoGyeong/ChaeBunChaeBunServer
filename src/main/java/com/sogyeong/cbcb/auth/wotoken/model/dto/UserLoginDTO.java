package com.sogyeong.cbcb.auth.wotoken.model.dto;

import com.sogyeong.cbcb.mypage.entity.UserLogin;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter
@Setter
@NoArgsConstructor
public class UserLoginDTO {
    private long loginId;
    private String loginType;
    private String accessToken;
    private String refreshToken;
    private String deviceToken;
    private String joinDate;

    public UserLoginDTO(UserLogin userLogin){
        this.loginId = userLogin.getSeq();
        this.loginType = userLogin.getType();
        this.accessToken = userLogin.getAccessToken();
        this.refreshToken = userLogin.getRefreshToken();
        this.deviceToken = userLogin.getDeviceToken();
        this.joinDate = userLogin.getJoinDate().format(DateTimeFormatter.ofPattern("MM-dd HH:mm"));
    }

    public UserLogin toEntity(){
        return new UserLogin().builder()
                .seq(loginId)
                .type(loginType)
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .deviceToken(deviceToken)
                .joinDate(LocalDateTime.now())
                .build();
    }
}
