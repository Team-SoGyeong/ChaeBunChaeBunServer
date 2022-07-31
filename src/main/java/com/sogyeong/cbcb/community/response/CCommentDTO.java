package com.sogyeong.cbcb.community.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@ApiModel(value = "댓글")
public class CCommentDTO {
    @ApiModelProperty(value = "포스트 ID")
    private Long postId;

    @ApiModelProperty(value = "댓글 ID")
    private Long commId;

    @ApiModelProperty(value = "프로필 이미지")
    private String profile;

    @ApiModelProperty(value = "댓글쓴이 이름")
    private String name;

    @ApiModelProperty(value = "댓글 내용")
    private String content;

    @ApiModelProperty(value = "댓글 작성일자")
    private String createdAt;

}
