package com.sogyeong.cbcb.board.repository;

import com.sogyeong.cbcb.board.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CommentRepository extends JpaRepository<Comment,Long> {

    @Query(value = "select case when count(seq) = 0 then 0 else seq end as lastSeq from board_comment order by seq desc ", nativeQuery = true)
    int getLastSeq(); // 댓글 삽입전 최신의 댓글 일련 번호를 얻기 위해 필요

    @Modifying
    @Query(value = "DELETE FROM board_comment WHERE seq = :commentId", nativeQuery = true)
    void deleteById(@Param(value="commentId") long commentId); // 댓글 삭제
}
