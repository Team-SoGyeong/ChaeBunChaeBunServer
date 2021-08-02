package com.sogyeong.cbcb.defaults.entity;


import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@Table(name = "default_products")
public class Products {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name ="seq" , unique = true, nullable = false)
    private long seq;

    @Column(name ="name")
    private String name;

    @Column(name = "isOther")
    private int isOther;

    @Column(name = "search_cnt")
    private int cnts;

    @Column(name = "reg_date")
    private LocalDateTime regDate;

    @Builder
    Products(long seq, String name, int isOther, int cnts, LocalDateTime regDate){
        this.seq = seq;
        this.name =name;
        this.isOther = isOther;
        this.cnts=cnts;
        this.regDate = regDate;
    }


}
