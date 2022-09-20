package com.sogyeong.cbcb.community.request;

import com.sogyeong.cbcb.community.entity.CImages;
import lombok.Data;

@Data
public class CAlbum {
    private String img1;
    private String img2;
    private String img3;
    private String img4;
    private String img5;

    public CImages toEntity(){
        return CImages.builder()
                .img1(img1)
                .img2(img2)
                .img3(img3)
                .img4(img4)
                .img5(img5)
                .build();
    }
}
