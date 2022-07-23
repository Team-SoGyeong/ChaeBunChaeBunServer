package com.sogyeong.cbcb.community.entity;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@Table(name = "c_board_community")
public class Posts {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name ="seq" , unique = true, nullable = false)
    private long seq;

    @NonNull
    @Column(name = "user_id")
    private long user_id;

    @NonNull
    @Column(name = "address_id")
    private long address_id;

    @NonNull
    @Column(name = "contents")
    private String contents;

    @Column(name ="img1")
    private String img1;

    @Column(name = "img2")
    private String img2;

    @Column(name = "img3")
    private String img3;

    @Column(name = "img4")
    private String img4;

    @Column(name = "img5")
    private String img5;

    @NonNull
    @Column(name = "create_date")
    private LocalDateTime create_date;

    @NonNull
    @Column(name = "update_date")
    private LocalDateTime update_date;

    @Builder
    Posts(long seq,long user_id,long address_id ,String contents,String img1,String img2,String img3,String img4,String img5,LocalDateTime create_date,LocalDateTime update_date) {

        this.seq=seq;
        this.user_id=user_id;
        this.address_id=address_id;
        this.contents=contents;
        this.img1=img1;
        this.img2=img2;
        this.img3=img3;
        this.img4=img4;
        this.img5=img5;
        this.create_date=create_date;
        this.update_date=update_date;
    }

}
