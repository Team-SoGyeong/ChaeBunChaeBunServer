package com.sogyeong.cbcb.community.controller;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@RestController
@RequestMapping(value = "community")
@AllArgsConstructor

public class CommunityController {
    @PersistenceContext
    private EntityManager em;

    //커뮤니티 글 목록
    //커뮤니티 글 상세
    //커뮤니티 글 쓰기
    //커뮤니티 글 수정
    //커뮤니티 글 삭제
    //내가 쓴 댓글리스트
    //내가 쓴 글 리스트
    //커뮤니티 댓글 리스트
    //커뮤니티 댓글 수정
    //커뮤니티 댓글 삭제
    //글 숨기기
    //글/댓글 신고
    // 알림 리스트
    // 좋아용 리스트
}
