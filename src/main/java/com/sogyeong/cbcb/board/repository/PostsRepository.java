package com.sogyeong.cbcb.board.repository;

import com.sogyeong.cbcb.board.entity.Posts;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostsRepository extends JpaRepository<Posts,Long> {
}
