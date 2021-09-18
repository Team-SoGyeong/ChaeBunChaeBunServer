package com.sogyeong.cbcb.board.controller;


import com.sogyeong.cbcb.board.service.HomeListService;
import com.sogyeong.cbcb.defaults.entity.Address;
import com.sogyeong.cbcb.defaults.entity.response.CommonResponse;
import com.sogyeong.cbcb.defaults.entity.response.ErrorResponse;
import com.sogyeong.cbcb.defaults.entity.response.ResultMessage;
import com.sogyeong.cbcb.defaults.repository.AddressRepository;
import com.sogyeong.cbcb.mypage.entity.UserInfo;
import com.sogyeong.cbcb.mypage.repository.UserInfoRepository;
import com.sogyeong.cbcb.mypage.service.MyPageService;
import com.sogyeong.cbcb.mypage.service.MyPostService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import com.sogyeong.cbcb.defaults.entity.response.BasicResponse;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.*;

@RestController
@AllArgsConstructor
public class HomeController {

    AddressRepository addressRepository;
    UserInfoRepository userInfoRepository;

    private HomeListService homeListService;
    private MyPostService myPostService;
    private MyPageService myPageService;

    @PersistenceContext
    private EntityManager em;

    //카테고리리스트
    @GetMapping("/home/category")
    public ResponseEntity<? extends BasicResponse> getCategory() {
        return ResponseEntity.ok().body( new CommonResponse(homeListService.getCategoryList(), ResultMessage.RESULT_OK.getVal()));
    }

    //홈구성통합
    @GetMapping("/home/{userId}")
    public ResponseEntity<? extends BasicResponse> getMainHome(@PathVariable("userId") long userId) {

        boolean isUser = userInfoRepository.existsById(userId);
        Optional<UserInfo> userInfo = userInfoRepository.findById(userId);
        if(!isUser){
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ErrorResponse(ResultMessage.UNDEFINE_USER.getVal()));
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
            map.put("last_list", homeListService.getNewList(addr_seq,userId));
            map.put("deadline_alerts",myPostService.getMyDeadlineList(userId));
            //유저의 마감직전 리스트로 바꿔야함
            homeInfo.add(map);

            return ResponseEntity.ok().body( new CommonResponse(homeInfo,ResultMessage.RESULT_OK.getVal()));
        }
    }

    //신규채분리스트
    @GetMapping("/home/new/{addr_seq}/{userId}")
    public ResponseEntity<? extends BasicResponse> getNewList(@PathVariable("addr_seq") long addr_seq,@PathVariable("userId") long userId) {
        boolean isAddr = addressRepository.existsById(addr_seq);
        if(!isAddr){
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ErrorResponse(ResultMessage.UNDEFINE_ADDRESS.getVal()));
        }
        else{
            List newList = homeListService.getNewList(addr_seq,userId);
            if(newList.size()>0)
                return ResponseEntity.ok().body( new CommonResponse(newList,ResultMessage.RESULT_OK.getVal()));
            else
                return ResponseEntity.ok().body( new CommonResponse(newList,ResultMessage.FAILED_NEW_LIST.getVal()));
        }
    }

    //마감직전채분리스트
    @GetMapping("/home/deadline/{addr_seq}/{userId}")
    public ResponseEntity<? extends BasicResponse> getDeadLineList(@PathVariable("addr_seq") long addr_seq,@PathVariable("userId") long userId) {
        boolean isAddr = addressRepository.existsById(addr_seq);
        if(!isAddr){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse(ResultMessage.UNDEFINE_ADDRESS.getVal()));
        }
        else{
            List deadlineList = homeListService.getDeadlineList(addr_seq,userId);
            if(deadlineList.size()>0)
                return ResponseEntity.ok().body( new CommonResponse(deadlineList,ResultMessage.RESULT_OK.getVal()));
            else
                return ResponseEntity.ok().body( new CommonResponse(deadlineList,ResultMessage.FAILED_ENDED_LIST.getVal()));
        }
    }

    //찜리스트
    @GetMapping("/home/wishlist/{userId}")
    public ResponseEntity<? extends BasicResponse> getMyWishList(@PathVariable("userId") long userId){
        boolean isUser = userInfoRepository.existsById(userId);
        Optional<UserInfo> userInfo = userInfoRepository.findById(userId);
        if(!isUser){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse(ResultMessage.UNDEFINE_USER.getVal()));
        }
        else{
            List wishList = homeListService.getMyLikeList(userId);
            if(wishList.size()>0)
                return ResponseEntity.ok().body( new CommonResponse(wishList,ResultMessage.RESULT_OK.getVal()));
            else
                return ResponseEntity.ok().body( new CommonResponse(wishList,ResultMessage.FAILED_SCRAP_LIST.getVal()));
        }
    }

    @GetMapping("/home/mydeadline/{userId}")
    public ResponseEntity<? extends BasicResponse> getMyDeadLineList(@PathVariable("userId") long userId) {
        boolean isUser = userInfoRepository.existsById(userId);
        Optional<UserInfo> userInfo = userInfoRepository.findById(userId);
        if (!isUser) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ErrorResponse(ResultMessage.UNDEFINE_USER.getVal()));
        } else {
            List list = myPostService.getMyDeadlineList(userId);
            if (list.size() > 0)
                return ResponseEntity.ok().body(new CommonResponse(list, ResultMessage.RESULT_OK.getVal()));
            else
                return ResponseEntity.ok().body(new CommonResponse(list, ResultMessage.FAILED_ENDED_LIST.getVal()));
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
                    .body(new ErrorResponse(ResultMessage.UNDEFINE_ADDRESS.getVal()));
        }
        else{
            //1차: 제목 - 2차: 내용 - 3차: 카테고리 검색
            List searchList = homeListService.getSearchList(addr_seq,searchStr);

            if(searchStr.equals("")||searchStr.length()<2){
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(new ErrorResponse(ResultMessage.SEARCH_OVER_TWO.getVal()));
            }
            else{
                if (searchList.size() > 0)
                    return ResponseEntity.ok().body(new CommonResponse(searchList, ResultMessage.RESULT_OK.getVal()));
                else
                    return ResponseEntity.ok().body(new CommonResponse(searchList, ResultMessage.NOTHING_SEARCH.getVal(searchStr)));
            }
        }
    }

    //위치검색
    @PostMapping("/home/location/{addr_str}")
    public ResponseEntity<? extends BasicResponse> searchAddress(@PathVariable("addr_str") String addr_str){

        List addressList = homeListService.getAddressList(addr_str.split(" "));

        if(addr_str.equals("")||addr_str.length()<2){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse(ResultMessage.SEARCH_OVER_TWO.getVal()));
        }
        if (addressList.size() > 0)
            return ResponseEntity.ok().body(new CommonResponse(addressList, ResultMessage.RESULT_OK.getVal()));
        else
            return ResponseEntity.ok().body(new CommonResponse(addressList, ResultMessage.NOTHING_SEARCH.getVal(addr_str)));
    }

    //위치 수정
    @PutMapping("/posts/location/{user_id}/{addr_seq}")
    public ResponseEntity<? extends BasicResponse> updateLocation(@PathVariable("user_id") long userId,@PathVariable("addr_seq") long addr_seq){
        boolean isUser = userInfoRepository.existsById(userId);
        Optional<UserInfo> userInfo = userInfoRepository.findById(userId);
        if (!isUser) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ErrorResponse(ResultMessage.UNDEFINE_USER.getVal()));
        } else {
            Boolean isChange = myPostService.updateAddr(userId,addr_seq);
            if (isChange)
                return ResponseEntity.ok().body(new CommonResponse(ResultMessage.UPDATE_OK.getVal()));
            else
                return ResponseEntity.ok().body(new CommonResponse(ResultMessage.UPDATE_FAILED.getVal()));
        }
    }

}
