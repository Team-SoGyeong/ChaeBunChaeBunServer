package com.sogyeong.cbcb.community.request;

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
    @NotNull
    private long post_id;

    private long cmt_id;
    @NotNull
    private long author_id;
    @NotNull
    private int reason_num;

    private String reason;
}
