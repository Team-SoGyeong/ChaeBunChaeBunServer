package com.sogyeong.cbcb.community.controller;

import com.sogyeong.cbcb.community.response.CPostsDTO;
import com.sogyeong.cbcb.community.response.MypageCPostDTO;
import com.sogyeong.cbcb.community.service.CPostsService;
import com.sogyeong.cbcb.defaults.entity.response.BasicResponse;
import com.sogyeong.cbcb.defaults.entity.response.CommonResponse;
import com.sogyeong.cbcb.defaults.entity.response.ResultMessage;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/community")
@AllArgsConstructor
public class CommunityController {

    private CPostsService cPostsService;

    //커뮤니티 글 목록
    @GetMapping("{userId}")
    public ResponseEntity<? extends BasicResponse> getCommunityPosts(@PathVariable Long userId){
        return ResponseEntity.ok().body( new CommonResponse(cPostsService.getAllCPosts(userId), ResultMessage.RESULT_OK.getVal()));
    }

    //커뮤니티 글 상세
    //커뮤니티 글 쓰기
    //커뮤니티 글 수정
    //커뮤니티 글 삭제

    //내가 쓴 댓글리스트 & 내가 쓴 글 리스트
    @GetMapping("/{type}/{userId}")
    public ResponseEntity<? extends BasicResponse> getMypageCommunityPosts(@PathVariable String type, @PathVariable Long userId){
        return ResponseEntity.ok().body( new CommonResponse(cPostsService.getMypageCPosts(type,userId), ResultMessage.RESULT_OK.getVal()));
    }
    //커뮤니티 댓글 리스트
    @GetMapping("/comment/{postId}")
    public ResponseEntity<? extends BasicResponse> getCommentTOPosts(@PathVariable Long postId){
        return ResponseEntity.ok().body( new CommonResponse(cPostsService.getCommToPost(postId), ResultMessage.RESULT_OK.getVal()));
    }
    //커뮤니티 댓글 수정
    //커뮤니티 댓글 삭제
    //글 숨기기
    //글/댓글 신고
    // 알림 리스트
    // 좋아용 리스트
}
