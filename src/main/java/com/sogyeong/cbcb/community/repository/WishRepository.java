package com.sogyeong.cbcb.community.repository;

import com.sogyeong.cbcb.community.entity.Wish;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface WishRepository extends JpaRepository<Wish,Long> {

    @Query(value = "select case when count(seq) = 0 then 0 else seq end as lastSeq from board_wish order by seq desc ", nativeQuery = true)
    int getLastSeq(); // 찜 삽입전 최신의 댓글 일련 번호를 얻기 위해 필요

    @Query(value = "select * from board_wish WHERE post_id = :post and member=:user ", nativeQuery = true)
    List<Wish> getWish(@Param(value="post") long postID, @Param(value="user") Long user); // 찜 삽입전 최신의 댓글 일련 번호를 얻기 위해 필요

    @Query(value = "select count(post_id) from board_wish WHERE post_id = :post ", nativeQuery = true)
    int getWish(@Param(value="post") long postID);

    @Modifying
    @Query(value = "DELETE FROM board_wish WHERE post_id = :post and member=:user", nativeQuery = true)
    void deleteByIdAndMember(@Param(value="post") long postID,@Param(value="user") long user); // 찜 삭제

    @Modifying
    @Query(value = "DELETE FROM board_wish WHERE post_id = :post", nativeQuery = true)
    void deleteByPostId(@Param(value="post") long postID);

    Boolean existsByPostId(long postId);
}
