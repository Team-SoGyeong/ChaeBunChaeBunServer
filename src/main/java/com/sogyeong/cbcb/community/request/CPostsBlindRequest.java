package com.sogyeong.cbcb.community.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotNull;

@NoArgsConstructor
@Getter
@Setter
@ToString
public class CPostsBlindRequest {
    @NotNull @ApiModelProperty(value = "글 ID", required = true)
    private long post_id;

    @ApiModelProperty(value = "댓글 ID (댓글 신고인 경우 추가)")
    private long cmt_id;

    @NotNull @ApiModelProperty(value = "신고자 ID", required = true)
    private long author_id;

    @NotNull @ApiModelProperty(value = "신고 사유 - 1: 광고성 글, 2: 부적절한 내용, 3: 기타", allowableValues = "1, 2, 3", required = true)
    private int reason_num;

    @ApiModelProperty(value = "신고 사유 기타(3)인 경우 입력")
    private String reason;
}
