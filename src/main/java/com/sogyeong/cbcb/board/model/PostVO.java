package com.sogyeong.cbcb.board.model;

import lombok.Data;

import java.util.List;

@Data
public class PostVO {
    private long category_id;
    private long author_id;
    private String title;
    private String contents;
    private String buy_date;
    private int amount;
    private String unit;
    private int total_price;
    private int headcount;
    private int per_price;
    private String contact;
    private List<String> imgs;
    private List<String> bill;
}
