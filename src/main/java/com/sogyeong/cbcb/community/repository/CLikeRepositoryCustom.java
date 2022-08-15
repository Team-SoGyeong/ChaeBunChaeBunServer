package com.sogyeong.cbcb.community.repository;

public interface CLikeRepositoryCustom {
    boolean delWishById(Long postId, Long userId);

    boolean existWish(Long postId, Long userId);
}
