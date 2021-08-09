package com.sogyeong.cbcb.board.repository;

import com.sogyeong.cbcb.board.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CommentRepository extends JpaRepository<Comment,Long> {

    @Query(value = "select case when count(seq) = 0 then 0 else seq end as lastSeq from board_comment order by seq desc limit 1", nativeQuery = true)
    int getLastSeq();

    @Modifying
    @Query(value = "DELETE FROM board_comment WHERE seq = :commentId", nativeQuery = true)
    void deleteById(@Param(value="commentId") Long commentId);

}
