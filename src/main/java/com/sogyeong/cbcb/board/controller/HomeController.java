package com.sogyeong.cbcb.board.controller;


import com.sogyeong.cbcb.board.model.vo.UpdatePostVO;
import com.sogyeong.cbcb.board.service.HomeListService;
import com.sogyeong.cbcb.defaults.entity.Address;
import com.sogyeong.cbcb.defaults.entity.response.CommonResponse;
import com.sogyeong.cbcb.defaults.entity.response.ErrorResponse;
import com.sogyeong.cbcb.defaults.repository.AddressRepository;
import com.sogyeong.cbcb.mypage.entity.UserInfo;
import com.sogyeong.cbcb.mypage.repository.UserInfoReposiorty;
import com.sogyeong.cbcb.mypage.service.MyPageService;
import com.sogyeong.cbcb.mypage.service.MyPostService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import com.sogyeong.cbcb.defaults.entity.response.BasicResponse;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.net.URLDecoder;
import java.util.*;

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
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
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
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
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
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ErrorResponse("존재하지 않는 사용자 입니다. "));
        } else {
            List list = myPostService.getMyDeadlineList(userId);
            if (list.size() > 0)
                return ResponseEntity.ok().body(new CommonResponse(list, "나의 마감채분 알럿 출력 성공"));
            else
                return ResponseEntity.ok().body(new CommonResponse(list, "나의 마감직전 채분 없음"));
        }
    }

    //게시글 검색
    @PostMapping("/home/{addr_seq}/{search_str}")
    public ResponseEntity<? extends BasicResponse> searchPosts(@PathVariable("addr_seq") long addr_seq,
                                                               @PathVariable("search_str") String searchStr){
        //해당지역에 올라온 게시글 중 검색이 맞나? 그게 맞다면 url -> /home/{addr_seq}/{search_str}이 나을
        // 1차: 해당 지역 설정 -> 2차: 들어온 스트링 검색
        boolean isAddr = addressRepository.existsById(addr_seq);
        if(!isAddr){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("입력된 주소 일련번호는 존재하지 않습니다. "));
        }
        else{
            //1차: 제목 - 2차: 내용 - 3차: 카테고리 검색
        }

        return null;
    }

    //위치검색
    @PostMapping("/home/location/{addr_str}")
    public ResponseEntity<? extends BasicResponse> searchAddress(@PathVariable("addr_str") String addr_str){

        List addressList = homeListService.getAddressList(addr_str.split(" "));

        if(addr_str.equals("")||addr_str.length()<2){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("두글자 이상 입력 바랍니다."));
        }
        if (addressList.size() > 0)
            return ResponseEntity.ok().body(new CommonResponse(addressList, "주소 검색 출력 성공"));
        else
            return ResponseEntity.ok().body(new CommonResponse(addressList, addr_str+"에 대한 검색결과가 없어요! 다시 시도해주세요!"));
    }

    //위치 수정
    @PutMapping("/posts/location/{user_id}/{addr_seq}")
    public ResponseEntity<? extends BasicResponse> updatePost(@PathVariable("user_id") long userId,@PathVariable("addr_seq") long addr_seq){
        boolean isUser = userInfoReposiorty.existsById(userId);
        Optional<UserInfo> userInfo = userInfoReposiorty.findById(userId);
        if (!isUser) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ErrorResponse("존재하지 않는 사용자 입니다. "));
        } else {
            Boolean isChange = myPostService.updateAddr(userId,addr_seq);
            if (isChange)
                return ResponseEntity.ok().body(new CommonResponse("주소 변경 성공"));
            else
                return ResponseEntity.ok().body(new CommonResponse("주소 변경 실패"));
        }
    }

}
