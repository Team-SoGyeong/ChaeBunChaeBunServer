package com.sogyeong.cbcb.auth.wotoken.model.vo;

import lombok.Data;

@Data
public class CheckSigninVO {
    private String login_type;
    private long kakao_id;
}
