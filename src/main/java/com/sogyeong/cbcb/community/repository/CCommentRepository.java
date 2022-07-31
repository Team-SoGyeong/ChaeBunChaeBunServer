package com.sogyeong.cbcb.community.repository;

import com.sogyeong.cbcb.community.entity.CComment;
import org.springframework.data.jpa.repository.JpaRepository;


public interface CCommentRepository extends JpaRepository<CComment,Long> , CCommentRepositoryCustom {

//    @Query(value = "select case when count(seq) = 0 then 0 else seq end as lastSeq from board_comment order by seq desc ", nativeQuery = true)
//    int getLastSeq(); // 댓글 삽입전 최신의 댓글 일련 번호를 얻기 위해 필요
//
//    @Modifying
//    @Query(value = "DELETE FROM board_comment WHERE seq = :commentId", nativeQuery = true)
//    void deleteById(@Param(value="commentId") long commentId); // 댓글 삭제
//
//    @Modifying
//    @Query(value = "DELETE FROM board_comment WHERE post_id = :postId", nativeQuery = true)
//    void deleteByPostId(@Param(value="postId")long postId);
//
//    Boolean existsByPostId(long postId);
}
