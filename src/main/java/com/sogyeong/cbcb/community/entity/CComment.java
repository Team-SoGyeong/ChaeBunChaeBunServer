package com.sogyeong.cbcb.community.entity;

import com.sogyeong.cbcb.community.response.CCommentDTO;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@SqlResultSetMapping(
        name = "CCommentDTOMapping",
        classes = @ConstructorResult(
                targetClass = CCommentDTO.class,
                columns = {
                        @ColumnResult(name = "postId", type = Long.class),
                        @ColumnResult(name = "commId", type = Long.class),
                        @ColumnResult(name = "profile", type = String.class),
                        @ColumnResult(name = "nickname", type = String.class),
                        @ColumnResult(name = "contents", type = String.class),
                        @ColumnResult(name = "create_date", type = String.class),
                }
        )
)

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@Table(name = "community_comment")
public class CComment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long seq;

    @ManyToOne
    @JoinColumn(name = "post_id", nullable = false)
    private CPosts post;

    @Column(name = "member", nullable = false)
    private long memberId;

    @Column(name = "contents", nullable = false)
    private String content;

    @Column(name = "host_chk")
    private String host_chk;

    @Column(name = "reg_date", nullable = false)
    private LocalDateTime regDate;

    @Builder
    public CComment(long seq, CPosts post, long member, String content, String host_chk, LocalDateTime regDate){
        this.seq = seq;
        this.post = post;
        this.memberId = member;
        this.content = content;
        this.host_chk =host_chk;
        this.regDate = regDate;
    }
}
