package com.sogyeong.cbcb.mypage.controller;

import com.sogyeong.cbcb.defaults.entity.response.BasicResponse;
import com.sogyeong.cbcb.defaults.entity.response.CommonResponse;
import com.sogyeong.cbcb.defaults.entity.response.ErrorResponse;
import com.sogyeong.cbcb.defaults.entity.response.ResultMessage;
import com.sogyeong.cbcb.mypage.repository.UserInfoRepository;
import com.sogyeong.cbcb.mypage.service.MyPostService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@RestController
@AllArgsConstructor
public class MyPostController {

    MyPostService myPostService;
    UserInfoRepository userInfoRepository;

    @PersistenceContext
    private EntityManager em;

    @GetMapping("/mypage/mypost/{user_id}/{platform_id}/{state_id}")
    public ResponseEntity<? extends BasicResponse> getMyPost(
            @PathVariable("user_id") long userId,
            @PathVariable("platform_id") int platform,
            @PathVariable("state_id") int state) {
        boolean isUser = userInfoRepository.existsById(userId);

        if(!isUser){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse(ResultMessage.UNDEFINE_USER.getVal()));
        }
        else{
            if(platform == 0) { // 채분페이지인경우
                if(state>2){
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                            .body(new ErrorResponse(ResultMessage.UNDEFINE_INPUT.getVal()));
                }
                else return ResponseEntity.ok().body( new CommonResponse(myPostService.getMyPostList(platform,state,userId),ResultMessage.RESULT_OK.getVal("내가 쓴글 리스트")));
            }//커뮤니티는 추후 개발
            else if(platform == 1)
                return ResponseEntity.ok().body(new CommonResponse(ResultMessage. COMING_SOON.getVal()));
            else
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse(ResultMessage.UNDEFINE_INPUT.getVal()));
        }
    }
}
