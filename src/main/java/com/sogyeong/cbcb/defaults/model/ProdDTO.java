package com.sogyeong.cbcb.defaults.model;

import com.sogyeong.cbcb.defaults.entity.Products;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter
@Setter
@NoArgsConstructor
public class ProdDTO {
    private long seq;
    private String name;
    private int isOther;
    private int cnts;
    private String regDate;

    public ProdDTO(Products prod){
       this.seq = prod.getSeq();
       this.name = prod.getName();
       this.isOther= 1;
       this.cnts = 0;
       this.regDate = prod.getRegDate().format(DateTimeFormatter.ofPattern("YYYY-MM-dd HH:mm:ss"));
    }

    public Products toEntity(){
        return new Products().builder()
                .seq(seq)
                .name(name)
                .isOther(isOther)
                .cnts(cnts)
                .regDate(LocalDateTime.now())
                .build();
    }
}