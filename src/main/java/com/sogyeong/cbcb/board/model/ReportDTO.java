package com.sogyeong.cbcb.board.model;

import com.sogyeong.cbcb.defaults.entity.Opinion;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter
@Setter
@NoArgsConstructor
public class ReportDTO {
    private String typ;
    private long post_id;
    private long author_id;
    private int reason_num;
    private String reason;
    private String regDate;

    public ReportDTO(Opinion oi){
        this.typ =  oi.getType();
        this.post_id = oi.getPostId();
        this.author_id = oi.getAuthorId();
        this.reason_num = oi.getReason_num();
        this.reason = oi.getReason();
        this.regDate = oi.getRegDate().format(DateTimeFormatter.ofPattern("YYYY-MM-dd HH:mm:ss"));
    }

    public Opinion toEntity(){
        return new Opinion().builder()
                .type(typ)
                .postId(post_id)
                .authorId(author_id)
                .reason_num(reason_num)
                .reason(reason)
                .regDate(LocalDateTime.now())
                .build();
    }
}
