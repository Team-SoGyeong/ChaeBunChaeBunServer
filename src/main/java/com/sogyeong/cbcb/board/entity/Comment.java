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

    @Column(name = "reg_date")
    private LocalDateTime regDate;

    @Builder
    Comment(long seq,long postId,long member,String content, LocalDateTime regDate){
        this.seq = seq;
        this.postId = postId;
        this.member = member;
        this.content = content;
        this.regDate = regDate;
    }
}
