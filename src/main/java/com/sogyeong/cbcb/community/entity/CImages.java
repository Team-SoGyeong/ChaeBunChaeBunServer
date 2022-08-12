package com.sogyeong.cbcb.community.entity;

import lombok.*;

import javax.persistence.Embeddable;
import java.util.Iterator;
import java.util.List;

@Embeddable
@Getter
@ToString
@NoArgsConstructor
public class CImages {
    private String img1;
    private String img2;
    private String img3;
    private String img4;
    private String img5;

    public void setCImages(List<String> fileNames){
        Iterator<String> it = fileNames.iterator();
        this.img1 = it.hasNext() ? it.next() : null;
        this.img2 = it.hasNext() ? it.next() : null;
        this.img3 = it.hasNext() ? it.next() : null;
        this.img4 = it.hasNext() ? it.next() : null;
        this.img5 = it.hasNext() ? it.next() : null;
    }
}
