package com.sogyeong.cbcb.board.model.response;

import com.sogyeong.cbcb.board.entity.Comment;
import com.sogyeong.cbcb.board.entity.Wish;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter
@Setter
@NoArgsConstructor
public class WishDTO {
    private long wish_id;
    private long post_id;
    private long user_id;
    private long author_id;
    private String regDate;

    public WishDTO(Wish w){
        this.wish_id = w.getSeq();
        this.post_id = w.getPostId();
        this.user_id = w.getMember();
        this.author_id = w.getAuthorId();
        this.regDate = w.getRegDate().format(DateTimeFormatter.ofPattern("MM-dd HH:mm"));

    }

    public Wish toEntity() {
        return new Wish().builder()
                .postId(post_id)
                .authorId(author_id)
                .member(user_id)
                .regDate(LocalDateTime.now())
                .build();
    }
}
