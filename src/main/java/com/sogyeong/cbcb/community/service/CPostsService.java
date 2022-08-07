package com.sogyeong.cbcb.community.service;

import com.sogyeong.cbcb.community.repository.CCommentRepository;
import com.sogyeong.cbcb.community.repository.CPostsRepository;
import com.sogyeong.cbcb.community.response.CCommentDTO;
import com.sogyeong.cbcb.community.response.CPostsDTO;
import com.sogyeong.cbcb.community.response.MypageCPostDTO;
import com.sogyeong.cbcb.defaults.entity.response.ResultMessage;
import com.sogyeong.cbcb.mypage.repository.UserInfoRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@AllArgsConstructor
public class CPostsService {
    private final CPostsRepository cPostsRepository;
    private final CCommentRepository cCommRepository;
    private final UserInfoRepository userInfoRepository;

    @Transactional(readOnly = true)
    public List<CPostsDTO> getAllCPosts(Long postId,Long userId){
        if(userInfoRepository.findById(userId).isEmpty())
            throw new IllegalArgumentException(ResultMessage.UNDEFINED_USER.getVal());
        if(postId>0){
            if(cPostsRepository.findById(postId).isEmpty())
            throw new IllegalArgumentException(ResultMessage.RESULT_FAILED.getVal());
        }
        return cPostsRepository.getAllCPosts(postId,userId);
    }


    @Transactional(readOnly = true)
    public List<MypageCPostDTO> getMypageCPosts(String type, Long userId){
        if (!(type.equals("post") || type.equals("comm")))
            throw new IllegalArgumentException(ResultMessage.TYPE_ERROR.getVal());
        if(userInfoRepository.findById(userId).isEmpty())
            throw new IllegalArgumentException(ResultMessage.UNDEFINED_USER.getVal());
        return cPostsRepository.getMypageCPosts(type,userId);
    }

    @Transactional(readOnly = true)
    public List<CCommentDTO> getCommToPost(Long postId){
        if(cPostsRepository.findById(postId).isEmpty())
            throw new IllegalArgumentException(ResultMessage.RESULT_FAILED.getVal());
        return cCommRepository.getCommToPost(postId);
    }
}
