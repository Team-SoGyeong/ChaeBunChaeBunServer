package com.sogyeong.cbcb.community.repository;

import com.sogyeong.cbcb.community.entity.CComment;
import com.sogyeong.cbcb.community.response.CCommentDTO;

import java.util.List;

public interface CCommentRepositoryCustom {
    public List<CCommentDTO> getCommToPost(Long postId);

    boolean delCommentById(Long commId);

}
