package com.sogyeong.cbcb.board.entity;


import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@Table(name = "board_album")
public class Album {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name ="post_id")
    private long albumId;

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

    @Column(name = "bill1")
    private String bill1;

    @Column(name = "bill2")
    private String bill2;

    @Column(name = "isAuth")
    private int isAuth;

    @Column(name = "reg_date")
    private LocalDateTime regDate;

    @Column(name = "update_date")
    private LocalDateTime updateDate;

    @Builder
    Album(long albumId,String img1,String img2,String img3,String img4,String img5,String bill1,String bill2,int isAuth,LocalDateTime regDate,LocalDateTime updateDate){
        this.albumId = albumId ;
        this.img1=img1;
        this.img2=img2;
        this.img3=img3;
        this.img4=img4;
        this.img5=img5;
        this.bill1=bill1;
        this.bill2=bill2;
        this.isAuth =isAuth;
        this.regDate=regDate;
        this.updateDate=updateDate;
    }

}
