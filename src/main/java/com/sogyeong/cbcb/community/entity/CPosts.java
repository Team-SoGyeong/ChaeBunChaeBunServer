package com.sogyeong.cbcb.community.entity;

import com.sogyeong.cbcb.community.response.CPostsDTO;
import com.sogyeong.cbcb.community.response.MypageCPostDTO;
import com.sogyeong.cbcb.defaults.entity.Address;
import com.sogyeong.cbcb.mypage.entity.UserInfo;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@SqlResultSetMapping(
        name = "CPostsDTOMapping",
        classes = @ConstructorResult(
                targetClass = CPostsDTO.class,
                columns = {
                        @ColumnResult(name = "seq", type = Long.class),
                        @ColumnResult(name = "profile", type = String.class),
                        @ColumnResult(name = "nickname", type = String.class),
                        @ColumnResult(name = "neighborhood", type = String.class),
                        @ColumnResult(name = "contents", type = String.class),
                        @ColumnResult(name = "create_date", type = String.class),
                        @ColumnResult(name = "img1", type = String.class),
                        @ColumnResult(name = "img2", type = String.class),
                        @ColumnResult(name = "img3", type = String.class),
                        @ColumnResult(name = "img4", type = String.class),
                        @ColumnResult(name = "img5", type = String.class),
                        @ColumnResult(name = "like_count", type = Integer.class),
                        @ColumnResult(name = "is_like", type = Boolean.class),
                        @ColumnResult(name = "comm_count", type = Integer.class)
                }
        )
)
@SqlResultSetMapping(
        name = "MypageCPostsDTOMapping",
        classes = @ConstructorResult(
                targetClass = MypageCPostDTO.class,
                columns = {
                        @ColumnResult(name = "postId", type = Long.class),
                        @ColumnResult(name = "userId", type = Long.class),
                        @ColumnResult(name = "contents", type = String.class),
                        @ColumnResult(name = "like_count", type = Integer.class),
                        @ColumnResult(name = "comm_count", type = Integer.class),
                }
        )
)

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@Table(name = "community_posts")
public class CPosts {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long seq;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private UserInfo user;

    @ManyToOne
    @JoinColumn(name = "address_id", nullable = false)
    private Address address;

    @Column(name = "contents", nullable = false)
    private String contents;

    @Embedded
    private CImages cImages;

    @Column(name = "create_date", nullable = false)
    private LocalDateTime create_date;

    @Column(name = "update_date", nullable = false)
    private LocalDateTime update_date;

    @Builder
    public CPosts(long seq, UserInfo user, Address address, String contents, CImages cImages, LocalDateTime create_date, LocalDateTime update_date) {
        this.seq = seq;
        this.user = user;
        this.address = address;
        this.contents = contents;
        this.cImages = cImages;
        this.create_date = create_date;
        this.update_date = update_date;
    }

}
