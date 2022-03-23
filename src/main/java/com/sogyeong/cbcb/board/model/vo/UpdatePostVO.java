package com.sogyeong.cbcb.board.model.vo;

import lombok.Data;

@Data
public class UpdatePostVO {
    private long post_id;
    private long category_id;
    private long author_id;
    private String title;
    private String contents;
    private String buy_date;
    private int amount;
    private String unit;
    private int total_price;
    private int per_price;
    private String contact;
    private AlbumVO imgs;
}
