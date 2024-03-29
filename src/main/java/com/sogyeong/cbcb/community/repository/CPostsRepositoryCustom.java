package com.sogyeong.cbcb.community.repository;

import com.sogyeong.cbcb.community.response.CPostsDTO;
import com.sogyeong.cbcb.community.response.MypageCPostDTO;

import java.util.LinkedHashMap;
import java.util.List;

public interface CPostsRepositoryCustom {

    List<CPostsDTO> getAllCPosts(Long postId, Long userId);
    List<MypageCPostDTO> getMypageCPosts(String type, Long userId);
    CPostsDTO getCPostByPostId(Long postId);
    List<LinkedHashMap<String, Object>> getNoticeListAll(Long userId);
    boolean delPostById(Long postId);
}
