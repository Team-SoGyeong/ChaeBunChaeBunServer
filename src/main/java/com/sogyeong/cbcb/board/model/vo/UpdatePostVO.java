package com.sogyeong.cbcb.board.model.vo;

import lombok.Data;

@Data
public class UpdatePostVO {
    private long post_id;
    private long category_id;
    private long author_id;
    private String contents;
    private String contact;
}
