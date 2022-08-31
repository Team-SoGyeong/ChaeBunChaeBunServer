package com.sogyeong.cbcb.mypage.controller;

import com.sogyeong.cbcb.board.entity.Posts;
import com.sogyeong.cbcb.board.repository.PostsRepository;
import com.sogyeong.cbcb.board.service.PostsService;
import com.sogyeong.cbcb.defaults.entity.Address;
import com.sogyeong.cbcb.defaults.entity.Products;
import com.sogyeong.cbcb.defaults.entity.response.BasicResponse;
import com.sogyeong.cbcb.defaults.entity.response.CommonResponse;
import com.sogyeong.cbcb.defaults.entity.response.ErrorResponse;
import com.sogyeong.cbcb.defaults.entity.response.ResultMessage;
import com.sogyeong.cbcb.defaults.repository.AddressRepository;
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
    AddressRepository addressRepository;

    @PersistenceContext
    private EntityManager em;

    private PostsService pService;
    private MyPostService myPostService;

    //프로필 조회
    @GetMapping("/mypage/profile/{userId}")
    public ResponseEntity<? extends BasicResponse> getProfile(@PathVariable("userId") long userId){
        boolean isUser = userInfoRepository.existsById(userId);
        Optional<UserInfo> userInfo = userInfoRepository.findById(userId);
        if(!isUser){
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ErrorResponse(ResultMessage.UNDEFINED_USER.getVal()));
        }
        else{
            String profile = userInfo.stream().findFirst().get().getUrl();
            Long addrId = userInfo.stream().findFirst().get().getAddr();
            String nickname = userInfo.stream().findFirst().get().getNickname();

            String addr = addressRepository.findById(addrId).get().getNeighborhood();

            List profileInfo = new ArrayList();
            LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();

            map.put("img", profile);
            map.put("nickname", nickname);
            map.put("address", addr);

            profileInfo.add(map);

            return ResponseEntity.ok().body( new CommonResponse(profileInfo,ResultMessage.RESULT_OK.getEditVal("프로필")));
        }
    }

    //프로필 수정
    @PutMapping("/mypage/profile")
    public ResponseEntity<? extends BasicResponse> updateProfile(@RequestBody ProfileVO PVO){
        boolean isUser = userInfoRepository.existsById(PVO.getUser_id());
        if(!isUser){
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ErrorResponse(ResultMessage.UNDEFINED_USER.getVal()));
        }
        else{
            int isNickname = userInfoRepository.existsByNicknameExceptMe(PVO.getUser_id(), PVO.getNickname());
            if(isNickname==1){
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(new ErrorResponse(ResultMessage.ALREADY_USED_NIK.getVal()));
            }
            else{
                Boolean isChange = myPageService.updateProfile(PVO.getUser_id(), PVO.getProfile_img(), PVO.getNickname());

                if (isChange)
                    return ResponseEntity.ok().body(new CommonResponse(ResultMessage.UPDATE_OK.getEditVal("프로필")));
                else
                    return ResponseEntity.ok().body(new CommonResponse(ResultMessage.UPDATE_FAILED.getEditVal("프로필")));
            }
        }
    }

    //내가 쓴 글 목록 조회
    @GetMapping("/mypage/mypost/{user_id}/{platform_id}/{state_id}")
    public ResponseEntity<? extends BasicResponse> getMyPost(
            @PathVariable("user_id") long userId,
            @PathVariable("platform_id") int platform,
            @PathVariable("state_id") int state) {
        boolean isUser = userInfoRepository.existsById(userId);

        if(!isUser){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse(ResultMessage.UNDEFINED_USER.getVal()));
        }
        else{
            if(platform == 0) { // 채분페이지인경우
                if(state>2){
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                            .body(new ErrorResponse(ResultMessage.UNDEFINED_INPUT.getVal()));
                }
                else return ResponseEntity.ok().body( new CommonResponse(myPostService.getMyPostList(platform,state,userId),ResultMessage.RESULT_OK.getEditVal("내가 쓴글 리스트")));
            }//커뮤니티는 추후 개발
            else if(platform == 1)
                return ResponseEntity.ok().body(new CommonResponse(ResultMessage. COMING_SOON.getVal()));
            else
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(new ErrorResponse(ResultMessage.UNDEFINED_INPUT.getVal()));
        }
    }

    //내가 쓴 글 상세조회
    @GetMapping("/mypage/mypost/{post_id}/{userId}")
    public ResponseEntity<? extends BasicResponse> getMyPostDetail(@PathVariable("userId") long userId,
                                                                      @PathVariable("post_id") long postId) {
        boolean isUser = userInfoRepository.existsById(userId);
        if (!isUser) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ErrorResponse(ResultMessage.UNDEFINED_USER.getVal()));
        }
        if (!postsRepository.existsById(postId)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ErrorResponse(ResultMessage.UNDEFINED_POST.getVal()));
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
            map.put("posts", myPageService.getMySubCategory(post.get().getProdId(), userId, postId));

            sub.add(map);

            String msg = seq >10 ? "내가 쓴 기타 채분 게시글" : "내가 쓴 일반 채분 게시글";
            return ResponseEntity.ok().body( new CommonResponse(sub,ResultMessage.RESULT_OK.getEditVal(msg)));
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
                    .body(new ErrorResponse(ResultMessage.UNDEFINED_USER.getVal()));
        }
        if(platformId == 0) { // 채분페이지인 경우
            if(stateId>2){
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(new ErrorResponse(ResultMessage.UNDEFINED_INPUT.getVal()));
            }
            else return ResponseEntity.ok().body(new CommonResponse
                    (myPageService.getMyCommentList(userId, platformId, stateId),ResultMessage.RESULT_OK.getEditVal("내가 쓴 댓글 목록")));
        }//커뮤니티는 추후 개발
        else if(platformId == 1)
            return ResponseEntity.ok().body(new CommonResponse(ResultMessage.COMING_SOON.getVal()));
        else
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse(ResultMessage.UNDEFINED_INPUT.getVal()));

    }

    //내가 쓴 댓글 상세조회
    @GetMapping("/mypage/mycomment/{userId}/{post_id}")
    public ResponseEntity<? extends BasicResponse> getCommentPostDetail(@PathVariable("userId") long userId,
                                                                      @PathVariable("post_id") long postId) {

        boolean isUser = userInfoRepository.existsById(userId);
        if (!isUser) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ErrorResponse(ResultMessage.UNDEFINED_USER.getVal()));
        }
        if (!postsRepository.existsById(postId)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ErrorResponse(ResultMessage.UNDEFINED_POST.getVal()));
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

            String msg = seq >10 ? "내가 쓴 댓글 기타 채분 게시글 " : "내가 쓴 댓글 일반 채분 게시글 ";
            return ResponseEntity.ok().body( new CommonResponse(sub,ResultMessage.RESULT_OK.getEditVal(msg)));
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
                    .body(new ErrorResponse(ResultMessage.UNDEFINED_USER.getVal()));
        }
        else{
            if(platformId == 0) { // 채분페이지인 경우
                if(stateId>2){
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                            .body(new ErrorResponse(ResultMessage.UNDEFINED_INPUT.getVal()));
                }
                else return ResponseEntity.ok().body(new CommonResponse
                        (myPageService.getScrapList(userId, platformId, stateId),ResultMessage.RESULT_OK.getEditVal("찜한 목록")));
            }//커뮤니티는 추후 개발
            else if(platformId == 1)
                return ResponseEntity.ok().body(new CommonResponse(ResultMessage.COMING_SOON.getVal()));
            else
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(new ErrorResponse(ResultMessage.UNDEFINED_INPUT.getVal()));
        }
    }

    //찜 상세 페이지
    @GetMapping("/mypage/scrap/{userId}/{post_id}")
    public ResponseEntity<? extends BasicResponse> getScrapPostDetail(@PathVariable("userId") long userId,
                                                                      @PathVariable("post_id") long postId) {

        boolean isUser = userInfoRepository.existsById(userId);
        if (!isUser) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ErrorResponse(ResultMessage.UNDEFINED_USER.getVal()));
        }
        if (!postsRepository.existsById(postId)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ErrorResponse(ResultMessage.UNDEFINED_POST.getVal()));
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

            String msg = seq >10 ? "기타 찜 채분 게시글 " : "일반 찜 채분 게시글 ";
            return ResponseEntity.ok().body( new CommonResponse(sub,ResultMessage.RESULT_OK.getEditVal(msg)));
        }

    }

    //탈퇴하기 - PUT
    @PutMapping("/mypage/profile/{userId}")
    public ResponseEntity<? extends BasicResponse> deleteUser(@PathVariable("userId") long userId){
        boolean isUser = userInfoRepository.existsById(userId);
        if (!isUser) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ErrorResponse(ResultMessage.UNDEFINED_USER.getVal()));
        }
        else{
            Boolean isChange = myPageService.updateUserQuitDate(userId);
            if(isChange)
                return  ResponseEntity.ok().body( new CommonResponse(ResultMessage.SUCCESS_QUIT.getVal()));
            else
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(new ErrorResponse(ResultMessage.FAILED_QUIT.getVal()));
        }
    }
}
