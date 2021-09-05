package com.sogyeong.cbcb.mypage.controller;

import com.sogyeong.cbcb.board.entity.Posts;
import com.sogyeong.cbcb.board.repository.PostsRepository;
import com.sogyeong.cbcb.board.service.PostsService;
import com.sogyeong.cbcb.defaults.entity.Products;
import com.sogyeong.cbcb.defaults.entity.response.BasicResponse;
import com.sogyeong.cbcb.defaults.entity.response.CommonResponse;
import com.sogyeong.cbcb.defaults.entity.response.ErrorResponse;
import com.sogyeong.cbcb.defaults.repository.ProductsRepository;
import com.sogyeong.cbcb.mypage.entity.UserInfo;
import com.sogyeong.cbcb.mypage.model.vo.ProfileVO;
import com.sogyeong.cbcb.mypage.repository.UserInfoRepository;
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
    UserInfoRepository userInfoRepository;
    PostsRepository postsRepository;
    ProductsRepository productsRepository;

    @PersistenceContext
    private EntityManager em;

    private PostsService pService;

    //프로필 조회
    @GetMapping("/mypage/profile/{userId}")
    public ResponseEntity<? extends BasicResponse> getProfile(@PathVariable("userId") long userId){
        boolean isUser = userInfoRepository.existsById(userId);
        Optional<UserInfo> userInfo = userInfoRepository.findById(userId);
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
        boolean isUser = userInfoRepository.existsById(PVO.getUser_id());
        //Optional<UserInfo> userInfo = userInfoRepository.findById(PVO.getUser_id());
        if(!isUser){
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ErrorResponse("존재하지 않는 사용자 입니다. "));
        }
        else{
            //닉네임 중복 확인->이거 HttpStatus.NOT_FOUND 쓰는 게 맞나..
            boolean isNickname = userInfoRepository.existsByNickname(PVO.getNickname());
            if(isNickname){
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ErrorResponse("이미 사용 중인 닉네임 입니다. 다른 닉네임을 입력하세요."));
            }
            else{
                Boolean isChange = myPageService.updateProfile(PVO.getUser_id(), PVO.getProfile_img(), PVO.getNickname());

                if (isChange)
                    return ResponseEntity.ok().body(new CommonResponse("프로필 변경 성공"));
                else
                    return ResponseEntity.ok().body(new CommonResponse("프로필 변경 실패"));
            }
        }
    }

    //내가 쓴 글 상세조회
    @GetMapping("/mypage/mypost/{post_id}/{userId}")
    public ResponseEntity<? extends BasicResponse> getMyPostDetail(@PathVariable("userId") long userId,
                                                                      @PathVariable("post_id") long postId) {
        boolean isUser = userInfoRepository.existsById(userId);
        if (!isUser) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ErrorResponse("존재하지 않는 사용자 입니다. 다시 시도 해주세요"));
        }
        if (!postsRepository.existsById(postId)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ErrorResponse("존재하지 않는 게시글 입니다. 다시 시도 해주세요"));
        } else{
            Optional<Posts> post = postsRepository.findById(postId);
            Optional<Products> products = productsRepository.findById(post.get().getProdId());
            Optional<UserInfo> user = userInfoRepository.findById(userId);

            long addr_seq = user.stream().findFirst().get().getAddr();
            long seq =products.stream().findFirst().get().getSeq();
            String name = seq <11 ? products.stream().findFirst().get().getName() : "기타";

            List sub = new ArrayList();
            LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
            map.put("category_name",name );
            map.put("address_id", addr_seq);
            map.put("posts",pService.getSubCategory(post.get().getProdId(),userId,postId));

            sub.add(map);

            String msg = seq >10 ? "내가 쓴 기타 채분 게시글 표출 성공" : "내가 쓴 일반 채분 게시글 표출 성공";
            return ResponseEntity.ok().body( new CommonResponse(sub,msg));
        }

    }

    //내가 쓴 댓글 목록
    @GetMapping("/mypage/mycomment/{userId}/{platform_id}/{state_id}")
    public ResponseEntity<? extends BasicResponse> getMyCommentList(@PathVariable("userId") long userId,
                                                                    @PathVariable("platform_id") long platformId,
                                                                    @PathVariable("state_id") long stateId){
        boolean isUser = userInfoRepository.existsById(userId);
        if(!isUser){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("존재하지 않는 사용자 입니다. "));
        }
        if(platformId == 0) { // 채분페이지인 경우
            if(stateId>2){
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(new ErrorResponse("잘못된 상태 값입니다. 다시 시도 부탁드립니다."));
            }
            else return ResponseEntity.ok().body(new CommonResponse
                    (myPageService.getMyCommentList(userId, platformId, stateId),"내가 쓴 댓글 목록 출력 성공"));
        }//커뮤니티는 추후 개발
        else if(platformId == 1)
            return ResponseEntity.ok().body(new CommonResponse("커뮤니티 서비스는 추후에 오픈됩니다."));
        else
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("잘못된 플랫폼 타입입니다. 다시 시도 부탁드립니다."));

    }

    //내가 쓴 댓글 상세조회
    @GetMapping("/mypage/mycomment/{userId}/{post_id}")
    public ResponseEntity<? extends BasicResponse> getCommentPostDetail(@PathVariable("userId") long userId,
                                                                      @PathVariable("post_id") long postId) {

        boolean isUser = userInfoRepository.existsById(userId);
        if (!isUser) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ErrorResponse("존재하지 않는 사용자 입니다. 다시 시도 해주세요"));
        }
        if (!postsRepository.existsById(postId)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ErrorResponse("존재하지 않는 게시글 입니다. 다시 시도 해주세요"));
        } else{
            Optional<Posts> post = postsRepository.findById(postId);
            Optional<Products> products = productsRepository.findById(post.get().getProdId());
            Optional<UserInfo> user = userInfoRepository.findById(userId);

            long addr_seq = user.stream().findFirst().get().getAddr();
            long seq =products.stream().findFirst().get().getSeq();
            String name = seq <11 ? products.stream().findFirst().get().getName() : "기타";

            List sub = new ArrayList();
            LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
            map.put("category_name",name );
            map.put("address_id", addr_seq);
            map.put("posts",pService.getSubCategory(post.get().getProdId(),userId,postId));

            sub.add(map);

            String msg = seq >10 ? "내가 쓴 댓글 기타 채분 게시글 표출 성공" : "내가 쓴 댓글 일반 채분 게시글 표출 성공";
            return ResponseEntity.ok().body( new CommonResponse(sub,msg));
        }

    }


    //찜목록
    @GetMapping("/mypage/scrap/{userId}/{platform_id}/{state_id}")
    public ResponseEntity<? extends BasicResponse> getScrapList(@PathVariable("userId") long userId,
                                                                @PathVariable("platform_id") long platformId,
                                                                @PathVariable("state_id") long stateId){
        boolean isUser = userInfoRepository.existsById(userId);
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

    //찜 상세 페이지
    @GetMapping("/mypage/scrap/{userId}/{post_id}")
    public ResponseEntity<? extends BasicResponse> getScrapPostDetail(@PathVariable("userId") long userId,
                                                                      @PathVariable("post_id") long postId) {

        boolean isUser = userInfoRepository.existsById(userId);
        if (!isUser) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ErrorResponse("존재하지 않는 사용자 입니다. 다시 시도 해주세요"));
        }
        if (!postsRepository.existsById(postId)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ErrorResponse("존재하지 않는 게시글 입니다. 다시 시도 해주세요"));
        } else{
            Optional<Posts> post = postsRepository.findById(postId);
            Optional<Products> products = productsRepository.findById(post.get().getProdId());
            Optional<UserInfo> user = userInfoRepository.findById(userId);

            long addr_seq = user.stream().findFirst().get().getAddr();
            long seq =products.stream().findFirst().get().getSeq();
            String name = seq <11 ? products.stream().findFirst().get().getName() : "기타";

            List sub = new ArrayList();
            LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
            map.put("category_name",name );
            map.put("address_id", addr_seq);
            map.put("posts",pService.getSubCategory(post.get().getProdId(),userId,postId));

            sub.add(map);

            String msg = seq >10 ? "기타 찜 채분 게시글 표출 성공" : "일반 찜 채분 게시글 표출 성공";
            return ResponseEntity.ok().body( new CommonResponse(sub,msg));
        }

    }

    //탈퇴하기 - PUT
    @PutMapping("/mypage/profile/{userId}")
    public ResponseEntity<? extends BasicResponse> deleteUser(@PathVariable("userId") long userId){
        boolean isUser = userInfoRepository.existsById(userId);
        if (!isUser) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ErrorResponse("존재하지 않는 사용자 입니다. 다시 시도 해주세요"));
        }
        else{
            Boolean isChange = myPageService.updateUserQuitDate(userId);
            if(isChange)
                return  ResponseEntity.ok().body( new CommonResponse("탈퇴 성공"));
            else
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(new ErrorResponse("탈퇴 실패"));
        }
    }
}
