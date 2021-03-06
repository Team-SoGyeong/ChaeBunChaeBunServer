package com.sogyeong.cbcb.board.model.dto;

import com.sogyeong.cbcb.board.entity.Comment;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter
@Setter
@NoArgsConstructor
public class CommentDTO {
    private long cmt_id;
    private long post_id;
    private long user_id;
    private String cmts;
    private String host_chk;
    private String regDate;

    public CommentDTO(Comment cmts){
        this.cmt_id = cmts.getSeq();
        this.post_id = cmts.getPostId();
        this.user_id = cmts.getMember();
        this.cmts = cmts.getContent();
        this.host_chk= "N";
        this.regDate = cmts.getRegDate().format(DateTimeFormatter.ofPattern("MM-dd HH:mm"));

    }

    public Comment toEntity() {
        return new Comment().builder()
                .postId(post_id)
                .member(user_id)
                .content(cmts)
                .host_chk(host_chk)
                .regDate(LocalDateTime.now())
                .build();
    }
}
