package com.sogyeong.cbcb.board.controller;


import com.sogyeong.cbcb.board.model.ResponseHomeList;
import com.sogyeong.cbcb.board.service.HomeListService;
import com.sogyeong.cbcb.defaults.entity.Address;
import com.sogyeong.cbcb.defaults.entity.response.CommonResponse;
import com.sogyeong.cbcb.defaults.entity.Products;
import com.sogyeong.cbcb.defaults.entity.response.ErrorResponse;
import com.sogyeong.cbcb.defaults.repository.AddressRepository;
import com.sogyeong.cbcb.defaults.repository.ProductsRepository;
import com.sogyeong.cbcb.mypage.entity.UserInfo;
import com.sogyeong.cbcb.mypage.repository.UserInfoReposiorty;
import com.sogyeong.cbcb.mypage.service.MyPageService;
import com.sogyeong.cbcb.mypage.service.MyPostService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import com.sogyeong.cbcb.defaults.entity.response.BasicResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Optional;

@RestController
@AllArgsConstructor
public class HomeController {

    AddressRepository addressRepository;
    UserInfoReposiorty userInfoReposiorty;

    private HomeListService homeListService;
    private MyPostService myPostService;
    private MyPageService myPageService;

    @PersistenceContext
    private EntityManager em;

    //카테고리리스트
    @GetMapping("/home/category")
    public ResponseEntity<? extends BasicResponse> getCategory() {
        return ResponseEntity.ok().body( new CommonResponse(homeListService.getCategoryList(),"카테고리 리스트 출력 성공"));
    }

    //홈구성통합
    @GetMapping("/home/{userId}")
    public ResponseEntity<? extends BasicResponse> getMainHome(@PathVariable("userId") long userId) {

        boolean isUser = userInfoReposiorty.existsById(userId);
        Optional<UserInfo> userInfo = userInfoReposiorty.findById(userId);
        if(!isUser){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("존재하지 않는 사용자 입니다. "));
        }
        else{

            Long addr_seq =userInfo.stream().findFirst().get().getAddr();
            Optional<Address> addr = addressRepository.findById(addr_seq);
            String addrInfo = addr.stream().findFirst().get().getCity()+' '+addr.stream().findFirst().get().getDistrict()+' '+addr.stream().findFirst().get().getNeighborhood();

            List homeInfo = new ArrayList();
            LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();

            map.put("user_id", userInfo.stream().findFirst().get().getSeq());
            map.put("address_id", userInfo.stream().findFirst().get().getAddr());
            map.put("full_address", addrInfo);
            map.put("category",homeListService.getCategoryList());
            map.put("last_list", homeListService.getNewList(addr_seq));
            map.put("deadline_alerts",myPostService.getMyDeadlineList(userId));
            //유저의 마감직전 리스트로 바꿔야함
            homeInfo.add(map);

            return ResponseEntity.ok().body( new CommonResponse(homeInfo,"홈 구성 정보 표츌 성공"));
        }
    }

    //신규채분리스트
    @GetMapping("/home/new/{addr_seq}")
    public ResponseEntity<? extends BasicResponse> getNewList(@PathVariable("addr_seq") long addr_seq) {
        boolean isAddr = addressRepository.existsById(addr_seq);
        if(!isAddr){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("입력된 주소 일련번호는 존재하지 않습니다. "));
        }
        else{
            List newList = homeListService.getNewList(addr_seq);
            if(newList.size()>0)
                return ResponseEntity.ok().body( new CommonResponse(newList,"신규 채분 리스트 출력 성공"));
            else
                return ResponseEntity.ok().body( new CommonResponse(newList,"아직 최신 채분이 없어요. 직접 채분을 시작해 신선함을 나눠보세요!"));
        }
    }

    //마감직전채분리스트
    @GetMapping("/home/deadline/{addr_seq}")
    public ResponseEntity<? extends BasicResponse> getDeadLineList(@PathVariable("addr_seq") long addr_seq) {
        boolean isAddr = addressRepository.existsById(addr_seq);
        if(!isAddr){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("입력된 주소 일련번호는 존재하지 않습니다. "));
        }
        else{
            List deadlineList = homeListService.getDeadlineList(addr_seq);
            if(deadlineList.size()>0)
                return ResponseEntity.ok().body( new CommonResponse(deadlineList,"마감 직전 채분 리스트 출력 성공"));
            else
                return ResponseEntity.ok().body( new CommonResponse(deadlineList,"마감 직전인 채분이 없어요. 직접 채분을 시작해 신선함을 나눠보세요!"));
        }
    }

    //찜리스트
    @GetMapping("/home/wishlist/{userId}")
    public ResponseEntity<? extends BasicResponse> getMyWishList(@PathVariable("userId") long userId){
        boolean isUser = userInfoReposiorty.existsById(userId);
        Optional<UserInfo> userInfo = userInfoReposiorty.findById(userId);
        if(!isUser){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("존재하지 않는 사용자 입니다. "));
        }
        else{
            List wishList = homeListService.getMyLikeList(userId);
            if(wishList.size()>0)
                return ResponseEntity.ok().body( new CommonResponse(wishList,"찜한 채분 리스트 출력 성공"));
            else
                return ResponseEntity.ok().body( new CommonResponse(wishList,"찜한 채분이 없어요. 참여하고 싶은 채분을 탐색해 찜해보세요."));
        }
    }

    @GetMapping("/home/mydeadline/{userId}")
    public ResponseEntity<? extends BasicResponse> getMyDeadLineList(@PathVariable("userId") long userId) {
        boolean isUser = userInfoReposiorty.existsById(userId);
        Optional<UserInfo> userInfo = userInfoReposiorty.findById(userId);
        if (!isUser) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("존재하지 않는 사용자 입니다. "));
        } else {
            List list = myPostService.getMyDeadlineList(userId);
            if (list.size() > 0)
                return ResponseEntity.ok().body(new CommonResponse(list, "나의 마감채분 알럿 출력 성공"));
            else
                return ResponseEntity.ok().body(new CommonResponse(list, "나의 마감직전 채분 없음"));
        }
    }

}
