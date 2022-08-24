package com.sogyeong.cbcb.community.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.validation.constraints.NotNull;

@AllArgsConstructor
@Getter
@Setter
@ToString
public class CCommentRequest {
    @NotNull @ApiModelProperty(required = true, example = "0")
    private Long postId;

    @NotNull @ApiModelProperty(required = true, example = "0")
    private Long userId;

    @NotNull @ApiModelProperty(required = true)
    private String content;
}
