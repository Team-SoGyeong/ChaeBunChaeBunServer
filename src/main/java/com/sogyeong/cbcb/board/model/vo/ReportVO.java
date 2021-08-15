package com.sogyeong.cbcb.board.model.vo;

import lombok.Data;

@Data
public class ReportVO {
    private long post_id;
    private long author_id;
    private int reason_num;
    private String reason;
}
