package com.sogyeong.cbcb.community.controller;

import com.sogyeong.cbcb.community.entity.CPosts;
import com.sogyeong.cbcb.community.request.CPostRequest;
import com.sogyeong.cbcb.community.response.CCommentDTO;
import com.sogyeong.cbcb.community.response.CPostsDTO;
import com.sogyeong.cbcb.community.response.MypageCPostDTO;
import com.sogyeong.cbcb.community.service.CPostsService;
import com.sogyeong.cbcb.defaults.entity.response.CommonResponse;
import com.sogyeong.cbcb.defaults.entity.response.ResultMessage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.List;

@RestController
@RequestMapping("/community")
@AllArgsConstructor
public class CommunityController {

    private CPostsService cPostsService;

    //커뮤니티 글 목록
    @ApiOperation("커뮤니티 글 목록")
    @GetMapping("{userId}")
    public CommonResponse<List<CPostsDTO>, String> getCommunityPosts(@PathVariable Long userId){
        return new CommonResponse(cPostsService.getAllCPosts(0L, userId), ResultMessage.RESULT_OK.getVal());
    }

    //커뮤니티 글 상세
    @ApiOperation("커뮤니티 글 상세")
    @GetMapping("/detail/{postId}/{userId}")
    public CommonResponse<List<CPostsDTO>, String> getCommunityPostsDetail(@PathVariable Long postId,@PathVariable Long userId){
        return new CommonResponse(cPostsService.getAllCPosts(postId , userId), ResultMessage.RESULT_OK.getVal());
    }
    //커뮤니티 글 쓰기
    @ApiOperation("커뮤니티 글 쓰기")
    @PostMapping("{userId}")
    public CommonResponse<CPostsDTO, String> writePost(@PathVariable Long userId,
                                                       @RequestBody CPostRequest cPostRequest){
        return new CommonResponse(cPostsService.saveCPost(userId, cPostRequest), ResultMessage.RESULT_OK.getVal());
    }

    //커뮤니티 글 수정
    //커뮤니티 글 삭제
    @ApiOperation("커뮤니티 댓글 삭제")
    @DeleteMapping("/{posytId}/{userId}")
    public CommonResponse delPosts(@PathVariable Long posytId,@PathVariable Long userId){
        return new CommonResponse(cPostsService.delPost(posytId,userId));
    }

    //내가 쓴 댓글리스트 & 내가 쓴 글 리스트
    @ApiOperation("내가 쓴 댓글리스트 & 내가 쓴 글 리스트")
    @GetMapping("/{type}/{userId}")
    public CommonResponse<List<MypageCPostDTO>, String> getMypageCommunityPosts(@PathVariable @ApiParam(value = "내가 쓴 글(post)/댓글(comm)", example = "comm") String type,
                                                                                @PathVariable Long userId){
        return new CommonResponse(cPostsService.getMypageCPosts(type, userId), ResultMessage.RESULT_OK.getVal());
    }

    //커뮤니티 댓글 리스트
    @ApiOperation("커뮤니티 댓글 리스트")
    @GetMapping("/comment/{postId}")
    public CommonResponse<List<CCommentDTO>, String> getCommentTOPosts(@PathVariable Long postId){
        return new CommonResponse(cPostsService.getCommToPost(postId), ResultMessage.RESULT_OK.getVal());
    }
    //커뮤니티 댓글 수정
    //커뮤니티 댓글 삭제
    @ApiOperation("커뮤니티 댓글 삭제")
    @DeleteMapping("/comment/{commId}/{userId}")
    public CommonResponse delCommentTOPosts(@PathVariable Long commId,@PathVariable Long userId){
        return new CommonResponse(cPostsService.delCommToPost(commId,userId));
    }
    //글 숨기기
    //글/댓글 신고
    // 알림 리스트
    // 좋아용 리스트
}
