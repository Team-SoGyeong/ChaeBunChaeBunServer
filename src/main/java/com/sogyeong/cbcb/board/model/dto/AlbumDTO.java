package com.sogyeong.cbcb.board.model.dto;

import com.sogyeong.cbcb.board.entity.Album;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter
@Setter
@NoArgsConstructor
public class AlbumDTO {
    private long post_id;
    private String img1;
    private String img2;
    private String img3;
    private String img4;
    private String img5;
    private String bill1;
    private String bill2;
    private int isAuth;
    private String reg_date;
    private String update_date;

    public AlbumDTO(Album album){
        this.post_id = album.getAlbumId();
        this.img1 = album.getImg1();
        this.img2 = album.getImg2();
        this.img3 = album.getImg3();
        this.img4 = album.getImg4();
        this.img5 = album.getImg5();
        this.bill1 = album.getBill1();
        this.bill2 = album.getBill2();
        this.isAuth = album.getIsAuth();
        this.reg_date = album.getRegDate().format(DateTimeFormatter.ofPattern("MM-dd HH:mm"));
        this.update_date = album.getUpdateDate().format(DateTimeFormatter.ofPattern("MM-dd HH:mm"));
    }

    public Album toEntity(){
        return new Album().builder()
                .albumId(post_id)
                .img1(img1)
                .img2(img2)
                .img3(img3)
                .img4(img4)
                .img5(img5)
                .bill1(bill1)
                .bill2(bill2)
                .isAuth(isAuth)
                .regDate(LocalDateTime.now())
                .updateDate(LocalDateTime.now())
                .build();
    }

}
