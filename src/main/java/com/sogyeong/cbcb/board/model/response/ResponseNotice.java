package com.sogyeong.cbcb.board.model.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ResponseNotice {
    private long notice_id;
    private long post_id;
    private long category_id;
    private String caseBy;
    private String nickname;
    private String title;
    private String img1;
    private String contents;
    private String buy_date;
    private String total_price;
    private String dates;
    private String w_dates;
    private String isClick;
    private String isNew;
    private String isAuth;
}
