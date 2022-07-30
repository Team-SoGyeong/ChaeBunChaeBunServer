package com.sogyeong.cbcb.community.service;

import com.sogyeong.cbcb.community.repository.CPostsRepository;
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
    private final UserInfoRepository userInfoRepository;

    @Transactional(readOnly = true)
    public List<CPostsDTO> getAllCPosts(Long userId){
        if(userInfoRepository.findById(userId).isEmpty())
            throw new IllegalArgumentException(ResultMessage.UNDEFINED_USER.getVal());
        return cPostsRepository.getAllCPosts(userId);
    }

    @Transactional(readOnly = true)
    public List<MypageCPostDTO> getMypageCPosts(String type, Long userId){
        if(userInfoRepository.findById(userId).isEmpty())
            throw new IllegalArgumentException(ResultMessage.UNDEFINED_USER.getVal());
        return cPostsRepository.getMypageCPosts(type,userId);
    }
}
