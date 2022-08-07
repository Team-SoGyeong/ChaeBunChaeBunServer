package com.sogyeong.cbcb.community.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;

@Embeddable
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CImages {
    private String img1;
    private String img2;
    private String img3;
    private String img4;
    private String img5;
}
