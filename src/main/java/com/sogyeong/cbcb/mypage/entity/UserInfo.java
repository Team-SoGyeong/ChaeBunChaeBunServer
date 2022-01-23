package com.sogyeong.cbcb.mypage.entity;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@Table(name = "user_info")
public class UserInfo { //extends UserLogin
    @Id
    @Column(name ="info_id")
    private long seq;

    @Column(name ="nickname", nullable = false)
    private String nickname;

    @Column(name ="email", nullable = false)
    private String email;

    @Column(name ="password")
    private String password;

    @Column(name ="address", nullable = false)
    private long addr;

    @Column(name ="profile")
    private String url;

    @Column(name ="set_image")
    private String set_image;

    @Column(name = "isDonated")
    private int isDonated;

    @Column(name = "join_date")
    private LocalDateTime joinDate;

    @Column(name = "sex")
    private int sex;

    @Column(name = "age_range")
    private int ageRange;

    @Builder
    UserInfo(long seq, String nickname, String email, String password, long addr, String url, String set_image,
             int isDonated, LocalDateTime joinDate, int sex, int ageRange){
        this.seq = seq;
        this.nickname = nickname;
        this.email = email;
        this.password = password;
        this.addr = addr;
        this.url = url;
        this.set_image = set_image;
        this.isDonated = isDonated;
        this.joinDate = joinDate;
        this.sex = sex;
        this.ageRange = ageRange;
    }
}
