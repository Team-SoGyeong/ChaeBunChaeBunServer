package com.sogyeong.cbcb.community.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CPostsDTO {

    private Long postId;
    private String profile;
    private String name;
    private String address;
    private String content;
    private String createdAt;
    private String img1;
    private String img2;
    private String img3;
    private String img4;
    private String img5;
    private Integer likeCount;
    private Integer isLike;
    private Integer commCount;

}
