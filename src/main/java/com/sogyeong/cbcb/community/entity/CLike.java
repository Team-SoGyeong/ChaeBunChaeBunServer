package com.sogyeong.cbcb.community.entity;

import com.sogyeong.cbcb.community.response.CLikeStatusDTO;
import com.sogyeong.cbcb.community.response.MypageCPostDTO;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
@SqlResultSetMapping(
        name = "CLikeStatusDTOMapping",
        classes = @ConstructorResult(
                targetClass = CLikeStatusDTO.class,
                columns = {
                        @ColumnResult(name = "status", type = Integer.class),
                        @ColumnResult(name = "like_cnts", type = Integer.class),
                }
        )
)

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@Table(name = "community_like")
public class CLike {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long seq;

    @ManyToOne
    @JoinColumn(name = "post_id", nullable = false)
    private CPosts post;

    @Column(name = "author_id")
    private long authorId;

    @Column(name = "member")
    private long member;

    @Column(name = "host_chk")
    private String host_chk;

    @Column(name = "reg_date")
    private LocalDateTime regDate;

    @Builder
    public CLike(long seq, CPosts post, long authorId, long member, String host_chk, LocalDateTime regDate){
        this.seq = seq;
        this.post = post;
        this.authorId = authorId;
        this.member = member;
        this.host_chk =host_chk;
        this.regDate = regDate;
    }
}
