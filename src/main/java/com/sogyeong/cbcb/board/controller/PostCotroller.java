package com.sogyeong.cbcb.board.controller;


import com.sogyeong.cbcb.board.model.CommentDTO;
import com.sogyeong.cbcb.board.model.CommentVO;
import com.sogyeong.cbcb.board.repository.CommentRepository;
import com.sogyeong.cbcb.board.repository.PostsRepository;
import com.sogyeong.cbcb.board.service.HomeListService;
import com.sogyeong.cbcb.board.service.PostsService;
import com.sogyeong.cbcb.defaults.entity.Products;
import com.sogyeong.cbcb.defaults.entity.response.BasicResponse;
import com.sogyeong.cbcb.defaults.entity.response.CommonResponse;
import com.sogyeong.cbcb.defaults.entity.response.ErrorResponse;
import com.sogyeong.cbcb.defaults.repository.AddressRepository;
import com.sogyeong.cbcb.defaults.repository.ProductsRepository;
import com.sogyeong.cbcb.mypage.entity.UserInfo;
import com.sogyeong.cbcb.mypage.repository.UserInfoReposiorty;
import com.sogyeong.cbcb.mypage.service.MyPageService;
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
public class PostCotroller {

    AddressRepository addressRepository;
    UserInfoReposiorty userInfoReposiorty;
    ProductsRepository productsRepository;
    PostsRepository postsRepository;
    CommentRepository commentRepository;

    private HomeListService homeListService;
    private PostsService pService;
    private MyPageService myPageService;

    @PersistenceContext
    private EntityManager em;

    @GetMapping("/posts/category/{categoryId}/{addr_seq}")
    public ResponseEntity<? extends BasicResponse> getSubCategoryList(@PathVariable("categoryId") long category_id,@PathVariable("addr_seq") long addr_seq) {
        boolean isAddr = addressRepository.existsById(addr_seq);
        boolean isCategory = productsRepository.existsById(category_id);
        if(!isCategory){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("입력된 카테고리 정보는 존재하지 않습니다. "));
        }
        if(!isAddr){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("입력된 주소 일련번호는 존재하지 않습니다. "));
        }
        else{
            Optional<Products> products = productsRepository.findById(category_id);
            String name =products.stream().findFirst().get().getName();
            long seq =products.stream().findFirst().get().getSeq();
            Boolean isEct = products.stream().findFirst().get().getIsOther()==0?false:true;

            List subList = new ArrayList();
            LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();

            if(seq>10) {
                map.put("category_id1", 11);
                map.put("category_id2", products.stream().findFirst().get().getSeq());
                map.put("category_name","기타" );
            }
            else {
                map.put("category_id1", seq);
                map.put("category_name",name );
            }
            map.put("isEct",isEct);
            map.put("address_id", addr_seq);
            map.put("posts",pService.getSubCategoryList(category_id,addr_seq));

            subList.add(map);

            return ResponseEntity.ok().body( new CommonResponse(subList,"하위 카테고리 리스트 출력 성공"));
        }

    }

    @PostMapping("/posts/comment")
    public ResponseEntity<? extends BasicResponse> saveComments(@RequestBody CommentVO CVO) {

        boolean isUser = userInfoReposiorty.existsById(CVO.getUser_id());
        if (!isUser) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ErrorResponse("존재하지 않는 사용자 입니다. 다시 시도 해주세요"));
        }
        else if (!postsRepository.existsById(CVO.getPost_id())) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ErrorResponse("존재하지 않는 게시글 입니다. 다시 시도 해주세요"));
        }
        else{
            CommentDTO commentDTO = new CommentDTO();

            commentDTO.setPost_id(CVO.getPost_id());
            commentDTO.setUser_id(CVO.getUser_id());
            commentDTO.setCmts(CVO.getCmts());
            
            Boolean isSave = pService.saveComments(commentDTO);
            if(isSave)
                return  ResponseEntity.ok().body( new CommonResponse("댓글 작성 성공"));
            else
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(new ErrorResponse("댓글 작성 실패"));
        }
    }

    @DeleteMapping("/posts/comment/{comment_id}/{user_id}")
    public ResponseEntity<? extends BasicResponse> deleteComments(@PathVariable("comment_id") long comment_id,@PathVariable("user_id") long user_id){

        boolean isUser = userInfoReposiorty.existsById(user_id);
        if (!isUser) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ErrorResponse("존재하지 않는 사용자 입니다. 다시 시도 해주세요"));
        }
        else if (!commentRepository.existsById(comment_id)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ErrorResponse("존재하지 않는 댓글 입니다. 다시 시도 해주세요"));
        }
        else{
            Boolean isDelete = pService.deleteComments(comment_id,user_id);
            if(isDelete)
                return  ResponseEntity.ok().body( new CommonResponse("댓글 삭제 성공"));
            else
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(new ErrorResponse("댓글 삭제 실패"));
        }
    }
}
