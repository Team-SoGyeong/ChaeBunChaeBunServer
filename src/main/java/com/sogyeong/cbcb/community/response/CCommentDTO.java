package com.sogyeong.cbcb.community.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CCommentDTO {
    private Long postId;
    private Long commId;
    private Long comm_user;
    private String profile;
    private String name;
    private String content;
    private String createdAt;
}
