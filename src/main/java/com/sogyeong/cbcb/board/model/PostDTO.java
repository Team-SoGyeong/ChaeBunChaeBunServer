package com.sogyeong.cbcb.board.model;

import com.sogyeong.cbcb.board.entity.Posts;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter
@Setter
@NoArgsConstructor
public class PostDTO {
    private long post_id;
    private long category_id;
    private long author_id;
    private String contents;
    private String title;
    private int headcount;
    private int period;
    private int amount;
    private String unit;
    private int total_price;
    private int per_price;
    private String contact;
    private int status;
    private int isDonated;
    private String regDate;

    public PostDTO(Posts posts){
        this.post_id = posts.getSeq();
        this.category_id = posts.getProdId();
        this.author_id = posts.getAuthorId();
        this.contents = posts.getContents();
        this.title = posts.getTitle();
        this.headcount = posts.getPeoples();
        this.period = posts.getPeriod();
        this.amount = posts.getAmount();
        this.unit = posts.getUnit();
        this.total_price = posts.getTotalPrice();
        this.per_price = posts.getPerPrice();
        this.contact = posts.getContact();
        this.status = posts.getStatus();
        this.isDonated = posts.getIsDonated();
        this.regDate = posts.getRegDate().format(DateTimeFormatter.ofPattern("MM-dd HH:mm"));
    }

    public Posts toEntity(){
        return new Posts().builder()
                .seq(post_id)
                .prodId(category_id)
                .authorId(author_id)
                .contents(contents)
                .title(title)
                .peoples(headcount)
                .period(period)
                .amount(amount)
                .unit(unit)
                .totalPrice(total_price)
                .perPrice(per_price)
                .contact(contact)
                .status(status)
                .isDonated(isDonated)
                .regDate(LocalDateTime.now())
                .build();
    }
}