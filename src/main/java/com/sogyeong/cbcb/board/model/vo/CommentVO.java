package com.sogyeong.cbcb.board.model.vo;

import lombok.Data;

@Data
public class CommentVO {
    private long post_id;
    private long user_id;
    private String cmts;
}
