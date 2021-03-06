package com.sogyeong.cbcb.defaults.entity;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@Table(name = "default_opinion")
public class Opinion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name ="seq" , unique = true, nullable = false)
    private long seq;

    @Column(name ="types")
    private String type;

    @Column(name = "post_id")
    private long postId;

    @Column(name = "cmt_id")
    private long cmtId;

    @Column(name = "author_id")
    private long authorId;

    @Column(name = "reason_num")
    private int reason_num;

    @Column(name = "reason")
    private String reason;

    @Column(name = "reg_date")
    private LocalDateTime regDate;

    @Builder
    Opinion(long seq, String type,long postId,long cmtId, long authorId, int reason_num,String reason,LocalDateTime regDate){
        this.seq = seq;
        this.type = type;
        this.postId =postId;
        this.cmtId=cmtId;
        this.authorId=authorId;
        this.reason_num=reason_num;
        this.reason= reason;
        this.regDate=regDate;
    }
}
