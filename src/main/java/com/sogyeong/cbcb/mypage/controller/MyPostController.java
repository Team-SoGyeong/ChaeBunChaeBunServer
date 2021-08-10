package com.sogyeong.cbcb.mypage.controller;

import com.sogyeong.cbcb.defaults.entity.response.BasicResponse;
import com.sogyeong.cbcb.defaults.entity.response.CommonResponse;
import com.sogyeong.cbcb.defaults.entity.response.ErrorResponse;
import com.sogyeong.cbcb.mypage.repository.UserInfoReposiorty;
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
    UserInfoReposiorty userInfoReposiorty;

    @PersistenceContext
    private EntityManager em;

    @GetMapping("/mypage/mypost/{user_id}/{platform_id}/{state_id}")
    public ResponseEntity<? extends BasicResponse> getMyPost(
            @PathVariable("user_id") long userId,
            @PathVariable("platform_id") int platform,
            @PathVariable("state_id") int state) {
        boolean isUser = userInfoReposiorty.existsById(userId);

        if(!isUser){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("존재하지 않는 사용자 입니다. "));
        }
        else{
            if(platform == 0) { // 채분페이지인경우
                if(state>2){
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                            .body(new ErrorResponse("잘못된 상태 값입니다. 다시 시도 부탁드립니다."));
                }
                else return ResponseEntity.ok().body( new CommonResponse(myPostService.getMyPostList(platform,state,userId),"내가 쓴글 리스트 출력 성공"));
            }//커뮤니티는 추후 개발
            else if(platform == 1)
                return ResponseEntity.ok().body(new CommonResponse("커뮤니티 서비스는 추후에 오픈됩니다."));
            else
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("잘못된 플랫폼 타입입니다. 다시 시도 부탁드립니다."));
        }
    }
}
