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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name ="info_id")
    private long seq;

    @Column(name ="name")
    private String name;

    @Column(name ="nickname")
    private String nickname;

    @Column(name ="email")
    private String email;

    @Column(name ="password")
    private String password;

    @Column(name ="address")
    private long addr;

    @Column(name ="profile")
    private String url;

    @Column(name = "isDonated")
    private int isDonated;

    @Column(name = "join_date")
    private LocalDateTime joinDate;


    @Builder
    UserInfo(long seq,String name,String nickname, String email, String password,long addr,String url,LocalDateTime joinDate){
        this.seq=seq;
        this.name=name;
        this.nickname = nickname;
        this.email = email;
        this.password = password;
        this.addr = addr;
        this.url = url;
        this.joinDate = joinDate;
    }
}
