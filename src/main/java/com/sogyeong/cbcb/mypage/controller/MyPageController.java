package com.sogyeong.cbcb.mypage.controller;

import com.sogyeong.cbcb.defaults.entity.response.BasicResponse;
import com.sogyeong.cbcb.defaults.entity.response.CommonResponse;
import com.sogyeong.cbcb.defaults.entity.response.ErrorResponse;
import com.sogyeong.cbcb.mypage.entity.UserInfo;
import com.sogyeong.cbcb.mypage.model.vo.ProfileVO;
import com.sogyeong.cbcb.mypage.repository.UserInfoReposiorty;
import com.sogyeong.cbcb.mypage.service.MyPageService;
import com.sogyeong.cbcb.mypage.service.MyPostService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Optional;

@RestController
@AllArgsConstructor
public class MyPageController {

    MyPageService myPageService;
    UserInfoReposiorty userInfoReposiorty;

    @PersistenceContext
    private EntityManager em;

    //프로필 조회
    @GetMapping("/mypage/profile/{userId}")
    public ResponseEntity<? extends BasicResponse> getProfile(@PathVariable("userId") long userId){
        boolean isUser = userInfoReposiorty.existsById(userId);
        Optional<UserInfo> userInfo = userInfoReposiorty.findById(userId);
        if(!isUser){
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ErrorResponse("존재하지 않는 사용자 입니다. "));
        }
        else{
            String profile = userInfo.stream().findFirst().get().getUrl();
            String nickname = userInfo.stream().findFirst().get().getNickname();

            List profileInfo = new ArrayList();
            LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();

            map.put("img", profile);
            map.put("nickname", nickname);

            profileInfo.add(map);

            return ResponseEntity.ok().body( new CommonResponse(profileInfo,"프로필 조회 성공"));
        }
    }

    //프로필 수정
    @PutMapping("/mypage/profile")
    public ResponseEntity<? extends BasicResponse> updateProfile(@RequestBody ProfileVO PVO){
        boolean isUser = userInfoReposiorty.existsById(PVO.getUser_id());
        Optional<UserInfo> userInfo = userInfoReposiorty.findById(PVO.getUser_id());
        if(!isUser){
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ErrorResponse("존재하지 않는 사용자 입니다. "));
        }
        else{
            Boolean isChange = myPageService.updateProfile(PVO.getUser_id(), PVO.getProfile_img(), PVO.getNickname());

            if (isChange)
                return ResponseEntity.ok().body(new CommonResponse("프로필 변경 성공"));
            else
                return ResponseEntity.ok().body(new CommonResponse("프로필 변경 실패"));
        }
    }

    //찜목록
    @GetMapping("/mypage/scrap/{userId}/{platform_id}/{state_id}")
    public ResponseEntity<? extends BasicResponse> getScrapList(@PathVariable("userId") long userId,
                                                                @PathVariable("platform_id") long platformId,
                                                                @PathVariable("state_id") long stateId){
        boolean isUser = userInfoReposiorty.existsById(userId);
        if(!isUser){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("존재하지 않는 사용자 입니다. "));
        }
        else{
            if(platformId == 0) { // 채분페이지인 경우
                if(stateId>2){
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                            .body(new ErrorResponse("잘못된 상태 값입니다. 다시 시도 부탁드립니다."));
                }
                else return ResponseEntity.ok().body(new CommonResponse
                        (myPageService.getScrapList(userId, platformId, stateId),"찜목록 출력 성공"));
            }//커뮤니티는 추후 개발
            else if(platformId == 1)
                return ResponseEntity.ok().body(new CommonResponse("커뮤니티 서비스는 추후에 오픈됩니다."));
            else
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(new ErrorResponse("잘못된 플랫폼 타입입니다. 다시 시도 부탁드립니다."));
        }
    }
}
