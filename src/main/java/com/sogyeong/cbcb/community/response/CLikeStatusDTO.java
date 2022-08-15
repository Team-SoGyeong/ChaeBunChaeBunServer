package com.sogyeong.cbcb.community.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CLikeStatusDTO {
    private int status;
    private int like_cnts;
}
