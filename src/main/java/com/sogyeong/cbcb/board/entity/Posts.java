package com.sogyeong.cbcb.board.entity;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@Table(name = "board_posts")
public class Posts {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name ="seq" , unique = true, nullable = false)
    private long seq;

    @Column(name = "prod_id")
    private long prodId;

    @Column(name = "author_id")
    private long authorId;

    @Column(name = "title")
    private String title;

    @Column(name = "contents")
    private String contents;

    @Column(name = "headcount")
    private int peoples;

    @Column(name = "period")
    private int period;

    @Column(name = "amount")
    private int amount;

    @Column(name = "unit")
    private String unit;

    @Column(name = "total_price")
    private int totalPrice;

    @Column(name = "per_price")
    private int perPrice;

    @Column(name = "contact")
    private String contact;

    @Column(name = "status")
    private int status;

    @Column(name = "isDonated")
    private int isDonated;

    @Column(name = "reg_date")
    private LocalDateTime regDate;

    @Builder
    Posts(long seq,long prodId,long authorId ,String title,String contents,int peoples
        ,int period,int amount,String unit,int totalPrice,int perPrice,String contact,int status
        ,int isDonated,LocalDateTime regDate) {

        this.seq=seq;
        this.prodId=prodId;
        this.authorId=authorId;
        this.title=title;
        this.contents=contents;
        this.peoples=peoples;
        this.period=period;
        this.amount=amount;
        this.unit =  unit;
        this.totalPrice=totalPrice;
        this.perPrice=perPrice;
        this.contact=contact;
        this.status=status;
        this.isDonated=isDonated;
        this.regDate=regDate;
    }

}
