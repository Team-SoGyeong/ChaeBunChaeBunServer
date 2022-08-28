package com.sogyeong.cbcb.community.controller;

import com.sogyeong.cbcb.community.request.CCommentRequest;
import com.sogyeong.cbcb.community.request.CPostRequest;
import com.sogyeong.cbcb.community.request.CPostsBlindRequest;
import com.sogyeong.cbcb.community.response.CCommentDTO;
import com.sogyeong.cbcb.community.response.CPostsDTO;
import com.sogyeong.cbcb.community.response.MypageCPostDTO;
import com.sogyeong.cbcb.community.service.CPostsService;
import com.sogyeong.cbcb.defaults.entity.response.CommonResponse;
import com.sogyeong.cbcb.defaults.entity.response.ResultMessage;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/community")
@AllArgsConstructor
public class CommunityController {

    private final CPostsService cPostsService;

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
    @PostMapping(path = "{userId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public CommonResponse<CPostsDTO, String> writePost(@PathVariable Long userId,
                                                       @ModelAttribute CPostRequest request
                                                       ){
        return new CommonResponse(cPostsService.saveCPost(userId, request), ResultMessage.RESULT_OK.getVal());
    }

    //커뮤니티 글 수정
    @ApiOperation("커뮤니티 글 수정")
    @PutMapping(path = "/{postId}/{userId}")
    public CommonResponse<CPostsDTO, String> updatePost(@PathVariable Long postId,
                                                        @PathVariable Long userId,
                                                        @ModelAttribute CPostRequest request){
        return new CommonResponse(cPostsService.updateCPost(postId, userId, request), ResultMessage.RESULT_OK.getVal());
    }

    //커뮤니티 글 삭제
    @ApiOperation("커뮤니티 글 삭제")
    @DeleteMapping("/{postId}/{userId}")
    public CommonResponse delPosts(@PathVariable Long postId,@PathVariable Long userId){
        return new CommonResponse(cPostsService.delPost(postId,userId));
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

    //커뮤니티 댓글 작성
    @ApiOperation("커뮤니티 댓글 작성")
    @PostMapping("/comment")
    public CommonResponse<List<CCommentDTO>, String> writeComment(@Valid @RequestBody CCommentRequest commentRequest){
        Long commId = cPostsService.saveCComment(commentRequest);
        if (commId > 0) //작성 성공
            return new CommonResponse(cPostsService.getCommToPost(commentRequest.getPostId()), ResultMessage.WRITE_OK.getVal());
        else //작성 실패
            return new CommonResponse(null, ResultMessage.WRITE_FAILED.getVal());
    }

    //커뮤니티 댓글 삭제
    @ApiOperation("커뮤니티 댓글 삭제")
    @DeleteMapping("/comment/{commId}/{userId}")
    public CommonResponse delCommentTOPosts(@PathVariable Long commId, @PathVariable Long userId){
        return new CommonResponse(cPostsService.delCommToPost(commId,userId));
    }
    //글 숨기기
    @ApiOperation("커뮤니티 글 숨기기")
    @PostMapping("/blind")
    public CommonResponse saveBlind( @RequestBody CPostsBlindRequest blindRequest) {
        return new CommonResponse(cPostsService.saveBlind(blindRequest));
    }
    //글/댓글 신고
    @ApiOperation("커뮤니티 글/댓글 신고")
    @PostMapping("/report")
    public CommonResponse saveReport( @RequestBody CPostsBlindRequest blindRequest) {
        return new CommonResponse(cPostsService.saveReport(blindRequest));
    }

    @ApiOperation("커뮤니티 글 좋아요")
    @PostMapping("/wishlist/{postId}/{userId}")
    public CommonResponse saveWish( @PathVariable("postId") Long postId, @PathVariable("userId") Long userId) {
        String res = cPostsService.saveWish(postId,userId);
        if( res.indexOf("성공")>=0 )
            return new CommonResponse(cPostsService.getLikeStatus(postId,userId),res);
        else return new CommonResponse(res);
    }
    // 알림 리스트
    // 좋아용 리스트
}
