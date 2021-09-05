package com.sogyeong.cbcb.mypage.entity;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@Table(name = "user_login")
//@Inheritance(strategy = InheritanceType.JOINED)
public class UserLogin {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name ="login_id" , unique = true, nullable = false)
    private long seq;

    @Column(name ="login_type", nullable = false)
    private String type;

    @Column(name = "access_token")
    private String accessToken;

    @Column(name = "refresh_token")
    private String refreshToken;

    @Column(name = "device_token")
    private String deviceToken;

    @Column(name = "join_date")
    private LocalDateTime joinDate;

    @Column(name = "update_date")
    private LocalDateTime updateDate;

    @Column(name = "quit_date")
    private LocalDateTime quitDate;

    @Builder
    UserLogin(long seq, String type, String accessToken, String refreshToken, String deviceToken,
              LocalDateTime joinDate, LocalDateTime updateDate, LocalDateTime quitDate){
        this.seq = seq;
        this.type = type;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.deviceToken = deviceToken;
        this.joinDate = joinDate;
        this.updateDate = updateDate;
        this.quitDate = quitDate;
    }
}
