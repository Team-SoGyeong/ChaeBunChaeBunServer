package com.sogyeong.cbcb.community.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@ApiModel(value = "커뮤니티 포스트")
public class CPostsDTO {

    @ApiModelProperty(value = "포스트 ID")
    private Long postId;

    @ApiModelProperty(value = "프로필 이미지")
    private String profile;

    @ApiModelProperty(value = "이름")
    private String name;

    @ApiModelProperty(value = "주소")
    private String address;

    @ApiModelProperty(value = "내용")
    private String content;

    @ApiModelProperty(value = "생성일자")
    private String createdAt;

    @ApiModelProperty(value = "이미지 1")
    private String img1;
    @ApiModelProperty(value = "이미지 2")
    private String img2;
    @ApiModelProperty(value = "이미지 3")
    private String img3;
    @ApiModelProperty(value = "이미지 4")
    private String img4;
    @ApiModelProperty(value = "이미지 5")
    private String img5;

    @ApiModelProperty(value = "좋아요 수")
    private Integer likeCount;

    @ApiModelProperty(value = "내가 좋아요 했는지 여")
    private Boolean isLike;

    @ApiModelProperty(value = "댓글 수")
    private Integer commCount;

}
