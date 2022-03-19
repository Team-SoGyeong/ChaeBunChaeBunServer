package com.sogyeong.cbcb.auth.wotoken.model.vo;

import lombok.Data;

@Data
public class SigninVO {
    private String login_type;
    private String nickname;
    private long address_seq;
    //private String profile;
    private String set_image;
    private String email;
    private long kakao_id;
    private String sex;
    private String age_range;
}
