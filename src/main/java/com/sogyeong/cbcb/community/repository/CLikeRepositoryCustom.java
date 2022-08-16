package com.sogyeong.cbcb.community.repository;

import com.sogyeong.cbcb.community.response.CLikeStatusDTO;

import java.util.List;

public interface CLikeRepositoryCustom {
    boolean delLikeById(Long postId, Long userId);

    boolean existLike(Long postId, Long userId);

    List<CLikeStatusDTO> existLikeStatus(Long postId, Long userId);
}
