package com.sogyeong.cbcb.community.repository;

import com.sogyeong.cbcb.community.entity.CPosts;
import com.sogyeong.cbcb.mypage.entity.UserInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CPostsRepository extends JpaRepository<CPosts, Long>, CPostsRepositoryCustom {
    Optional<CPosts> findByUserAndSeq(UserInfo user, Long postId);
    //    @Query(value = "select case when count(seq) = 0 then 0 else seq end as lastSeq from board_posts order by seq desc ", nativeQuery = true)
//    int getLastSeq(); // 댓글 삽입전 최신의 댓글 일련 번호를 얻기 위해 필요
//
//    @Modifying
//    @Query(value = "DELETE FROM board_posts WHERE seq = :post", nativeQuery = true)
//    void deleteById(@Param(value="post") long postID);
}
