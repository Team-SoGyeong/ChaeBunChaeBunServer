package com.sogyeong.cbcb.auth.wotoken.model.vo;

import lombok.Data;

@Data
public class SigninVO {
    private String login_type;
    private String nickname;
    private long address_seq;
    private String profile;
    private String email;
    private String sex;
    private String age_range;
}