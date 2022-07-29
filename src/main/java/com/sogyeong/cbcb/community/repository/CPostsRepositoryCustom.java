package com.sogyeong.cbcb.community.repository;

import com.sogyeong.cbcb.community.response.CPostsDTO;

import java.util.List;

public interface CPostsRepositoryCustom {
    public List<CPostsDTO> getAllCPosts(Long userId);
}
