package com.sogyeong.cbcb.community.entity;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@Table(name = "community_opinion")
public class COpinion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long seq;

    @Column(name ="types", nullable = false)
    @Enumerated(EnumType.STRING)
    private Otype type; //BLIND = 숨김, REPORT = 신고

    @ManyToOne
    @JoinColumn(name = "post_id")
    private CPosts post;

    @Column(name = "cmt_id")
    private long cmtId;

    @Column(name = "author_id", nullable = false)
    private long authorId;

    private int reason_num;

    private String reason;

    @Column(name = "reg_date", nullable = false)
    private LocalDateTime regDate;

    @Builder
    public COpinion(long seq, Otype type, CPosts post, long cmtId, long authorId, int reason_num, String reason, LocalDateTime regDate){
        this.seq = seq;
        this.type = type;
        this.post = post;
        this.cmtId = cmtId;
        this.authorId = authorId;
        this.reason_num = reason_num;
        this.reason = reason;
        this.regDate = regDate;
    }

    public enum Otype{
        BLIND, REPORT
    }

}
