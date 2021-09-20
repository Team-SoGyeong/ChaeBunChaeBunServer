package com.sogyeong.cbcb.board.controller;


import com.sogyeong.cbcb.board.entity.Wish;
import com.sogyeong.cbcb.board.model.dto.WishDTO;
import com.sogyeong.cbcb.board.model.response.ResponseNotice;
import com.sogyeong.cbcb.board.repository.PostsRepository;
import com.sogyeong.cbcb.board.repository.WishRepository;
import com.sogyeong.cbcb.board.service.CommonService;
import com.sogyeong.cbcb.defaults.entity.response.BasicResponse;
import com.sogyeong.cbcb.defaults.entity.response.CommonResponse;
import com.sogyeong.cbcb.defaults.entity.response.ErrorResponse;
import com.sogyeong.cbcb.defaults.entity.response.ResultMessage;
import com.sogyeong.cbcb.mypage.repository.UserInfoRepository;
import com.sogyeong.cbcb.mypage.service.MyPostService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@RestController
@AllArgsConstructor
public class CommonController {

    UserInfoRepository userInfoRepository;
    PostsRepository postsRepository;
    private WishRepository wishRepository;
    private CommonService cService;
    private MyPostService pService;

    @PersistenceContext
    private EntityManager em;


    //소분팟 완료 처리
    @PutMapping("/common/processed/{post_id}/{user_id}")
    public ResponseEntity<? extends BasicResponse> setShared(@PathVariable("post_id") long post_id,@PathVariable("user_id") long user_id){
        boolean isUser = userInfoRepository.existsById(user_id);
        long author = postsRepository.findById(post_id).stream().findFirst().get().getAuthorId();

        if (!isUser) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ErrorResponse(ResultMessage.UNDEFINED_USER.getVal()));
        }
        else if (!postsRepository.existsById(post_id)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ErrorResponse(ResultMessage.UNDEFINED_POST.getVal()));
        }
        else if(author==user_id){
            Boolean isDone = cService.setPostStatus(post_id);
            if(isDone){
                return  ResponseEntity.ok().body( new CommonResponse(ResultMessage.SUBDIVISION_OK.getVal()));
            }
            else{
                return  ResponseEntity.ok().body( new CommonResponse(ResultMessage.SUBDIVISION_ALREADY.getVal()));
            }

        }
        else{
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponse(ResultMessage.NOT_SUBDIVISION_SELF.getVal()));

        }
    }
    //소분팟 기부 처리
    @PutMapping("/common/donated/{post_id}/{user_id}")
    public ResponseEntity<? extends BasicResponse> setDonated(@PathVariable("post_id") long post_id,@PathVariable("user_id") long user_id){
        boolean isUser = userInfoRepository.existsById(user_id);
        long author = postsRepository.findById(post_id).stream().findFirst().get().getAuthorId();

        if (!isUser) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ErrorResponse(ResultMessage.UNDEFINED_USER.getVal()));
        }
        else if (!postsRepository.existsById(post_id)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ErrorResponse(ResultMessage.UNDEFINED_POST.getVal()));
        }
        else if(author==user_id){
            Boolean isDonated = cService.setPostDonated(post_id);
            if(isDonated){
                return  ResponseEntity.ok().body( new CommonResponse(ResultMessage.DONATE_OK.getVal()));
            }
            else{
                return  ResponseEntity.ok().body( new CommonResponse(ResultMessage.DONATE_ALREADY.getVal()));
            }

        }
        else{
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponse(ResultMessage.NOT_DONATE_SELF.getVal()));

        }
    }
    //찜하기
    @PostMapping("/common/wishlist/{post_id}/{user_id}")
    public ResponseEntity<? extends BasicResponse> saveWish(@PathVariable("post_id") long post_id, @PathVariable("user_id") long user_id){
        boolean isUser = userInfoRepository.existsById(user_id);
        long author = 0;
        List<Wish> wishResult = wishRepository.getWish(post_id,user_id);

        if (!isUser) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ErrorResponse(ResultMessage.UNDEFINED_USER.getVal()));
        }
        else if (!postsRepository.existsById(post_id)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ErrorResponse(ResultMessage.UNDEFINED_POST.getVal()));
        }
        else if( postsRepository.findById(post_id).stream().findFirst().get().getAuthorId()==user_id){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponse(ResultMessage.NOT_SCRAP_SELF.getVal()));
        }
        else{
            author = postsRepository.findById(post_id).stream().findFirst().get().getAuthorId();
            if(wishResult.size()==0){
                WishDTO wDTO = new WishDTO();

                wDTO.setPost_id(post_id);
                wDTO.setAuthor_id(author);
                wDTO.setUser_id(user_id);
                wDTO.setHost_chk("N");

                List isSave = cService.storeWish(wDTO);
                if(isSave.size()>0)
                    return  ResponseEntity.ok().body( new CommonResponse(isSave,ResultMessage.SCRAP_OK.getVal()));
                else
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                            .body(new ErrorResponse(ResultMessage.SCRAP_FAILED.getVal()));
            }
            else{
                List isDelete = cService.deleteWish(post_id,user_id);
                if(isDelete.size()>0)
                    return  ResponseEntity.ok().body( new CommonResponse(isDelete,ResultMessage.SCRAP_CANCEL_OK.getVal()));
                else
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                            .body(new ErrorResponse(ResultMessage.SCRAP_CANCEL_FAILED.getVal()));
            }

        }
    }

    //찜, 댓글 알림 받은 리스트
    @GetMapping("/common/notice/{user_id}")
    public ResponseEntity<? extends BasicResponse> getNoticeList(@PathVariable("user_id") long user_id){
        boolean isUser = userInfoRepository.existsById(user_id);
        List<ResponseNotice> list = pService.getMyNotice(user_id);

        if (!isUser) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ErrorResponse(ResultMessage.UNDEFINED_USER.getVal()));
        }
        else {

            if (list.size() > 0)
                return ResponseEntity.ok().body(new CommonResponse(list, ResultMessage.RESULT_OK.getVal()));
            else
                return ResponseEntity.ok().body(new CommonResponse(list, ResultMessage.RESULT_FAILED.getVal("게시글 관련 알림")));
        }
    }

    //찜, 댓글 알림 받은 리스트 클릭시 정보 자장
    @PutMapping("/common/notice/{case_type}/{notice_id}")
    public ResponseEntity<? extends BasicResponse> setNoticeList(@PathVariable("case_type") String types, @PathVariable("notice_id") long seq){
        if(types.equals("scrap")){
            int cilck1 = cService.hostClickByScrap(seq);
            if(cilck1==1){
                return ResponseEntity.ok().body(new CommonResponse(ResultMessage.UPDATE_OK.getVal()));
            }
            else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new ErrorResponse(ResultMessage.UNDEFINE_SCRAP.getVal()));
            }
        }
        else if(types.equals("comment")) {
            int cilck2 = cService.hostClickByComment(seq);
            if(cilck2==1){
                return ResponseEntity.ok().body(new CommonResponse(ResultMessage.UPDATE_OK.getVal()));
            }
            else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new ErrorResponse(ResultMessage.UNDEFINE_COMMENT.getVal()));
            }
        }
        else {
           return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponse(ResultMessage.UNDEFINED_INPUT.getVal()));
        }

    }

}
