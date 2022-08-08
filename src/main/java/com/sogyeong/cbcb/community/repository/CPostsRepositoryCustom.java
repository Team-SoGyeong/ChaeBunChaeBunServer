package com.sogyeong.cbcb.community.repository;

import com.sogyeong.cbcb.community.response.CPostsDTO;
import com.sogyeong.cbcb.community.response.MypageCPostDTO;

import java.util.List;

public interface CPostsRepositoryCustom {

    List<CPostsDTO> getAllCPosts(Long postId, Long userId);
    public List<MypageCPostDTO> getMypageCPosts(String type, Long userId);
    public CPostsDTO getCPostByPostId(Long postId);
    boolean delPostById(Long postId);
}
