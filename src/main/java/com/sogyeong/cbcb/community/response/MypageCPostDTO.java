package com.sogyeong.cbcb.community.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@ApiModel(value = "내가 쓴 글/댓글 리스트 게시글")
public class MypageCPostDTO {
    @ApiModelProperty(value = "포스트 ID")
    private Long postId;

    @ApiModelProperty(value = "사용자 ID")
    private Long userId;

    @ApiModelProperty(value = "내용")
    private String contents;

    @ApiModelProperty(value = "좋아요 수")
    private Integer like_count;

    @ApiModelProperty(value = "댓글 수")
    private Integer comm_count;
}
