package com.sogyeong.cbcb.community.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MypageCPostDTO {
    private Long postId;
    private Long userId;
    private String contents;
    private Integer like_count;
    private Integer comm_count;
    private Integer isLike;
}
