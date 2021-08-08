package com.sogyeong.cbcb.board.repository;

import com.sogyeong.cbcb.board.entity.Album;
import org.springframework.data.jpa.repository.JpaRepository;
public interface AlbumRepository extends JpaRepository<Album,Long> {
}
