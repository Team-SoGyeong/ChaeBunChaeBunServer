package com.sogyeong.cbcb.auth.wotoken.model.dto;

import com.sogyeong.cbcb.mypage.entity.UserInfo;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter
@Setter
@NoArgsConstructor
public class UserInfoDTO {
    private long infoId;
    private String nickname;
    private String email;
    private String password;
    private long address;
    private String profile;
    private int isDonated;
    private String joinDate;
    private int sex;
    private int ageRange;

    public UserInfoDTO(UserInfo userInfo){
        this.infoId = userInfo.getSeq();
        this.nickname = userInfo.getNickname();
        this.email = userInfo.getEmail();
        this.password = userInfo.getPassword();
        this.address = userInfo.getAddr();
        this.profile = userInfo.getUrl();
        this.isDonated = userInfo.getIsDonated();
        this.joinDate = userInfo.getJoinDate().format(DateTimeFormatter.ofPattern("MM-dd HH:mm"));
        this.sex = userInfo.getSex();
        this.ageRange = userInfo.getAgeRange();
    }

    public UserInfo toEntity(){
        return new UserInfo().builder()
                .seq(infoId)
                .nickname(nickname)
                .email(email)
                .password(password)
                .addr(address)
                .url(profile)
                .isDonated(isDonated)
                .joinDate(LocalDateTime.now())
                .sex(sex)
                .ageRange(ageRange)
                .build();
    }
}
