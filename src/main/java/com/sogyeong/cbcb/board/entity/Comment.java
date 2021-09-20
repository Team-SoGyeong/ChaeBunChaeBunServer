package com.sogyeong.cbcb.board.entity;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@Table(name = "board_comment")
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name ="seq")
    private long seq;

    @Column(name = "post_id")
    private long postId;

    @Column(name = "member")
    private long member;

    @Column(name = "contents")
    private String content;

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "host_chk")
    private String host_chk;

    @Column(name = "reg_date")
    private LocalDateTime regDate;

    @Builder
    Comment(long seq,long postId,long member,String content, String host_chk,LocalDateTime regDate){
        this.seq = seq;
        this.postId = postId;
        this.member = member;
        this.content = content;
        this.host_chk =host_chk;
        this.regDate = regDate;
    }
}
