package com.sogyeong.cbcb.board.repository;

import com.sogyeong.cbcb.board.entity.Album;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface AlbumRepository extends JpaRepository<Album,Long> {

    @Modifying
    @Query(value = "DELETE FROM board_album WHERE post_id = :post", nativeQuery = true)
    void deleteById(@Param(value="post") long postID);
}
