package com.sogyeong.cbcb.community.entity;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;


@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@Table(name = "c_board_wish")
public class Wish {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name ="seq")
    private long seq;

    @NonNull
    @Column(name = "post_id")
    private long postId;

    @Column(name = "author_id")
    private long authorId;

    @Column(name = "member")
    private long member;

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "host_chk")
    private String host_chk;

    @Column(name = "reg_date")
    private LocalDateTime regDate;

    @Builder
    Wish(long seq,long postId,long authorId,long member, String host_chk,LocalDateTime regDate){
        this.seq = seq;
        this.postId = postId;
        this.authorId = authorId;
        this.member = member;
        this.host_chk =host_chk;
        this.regDate = regDate;
    }
}
