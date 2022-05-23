package com.sogyeong.cbcb.board.controller;


import com.sogyeong.cbcb.board.entity.Posts;
import com.sogyeong.cbcb.board.model.dto.AlbumDTO;
import com.sogyeong.cbcb.board.model.dto.CommentDTO;
import com.sogyeong.cbcb.board.model.dto.PostDTO;
import com.sogyeong.cbcb.board.model.vo.CommentVO;
import com.sogyeong.cbcb.board.model.vo.PostEctVO;
import com.sogyeong.cbcb.board.model.vo.PostVO;
import com.sogyeong.cbcb.board.model.vo.UpdatePostVO;
import com.sogyeong.cbcb.board.repository.CommentRepository;
import com.sogyeong.cbcb.board.repository.PostsRepository;
import com.sogyeong.cbcb.board.repository.WishRepository;
import com.sogyeong.cbcb.board.service.PostsService;
import com.sogyeong.cbcb.defaults.entity.Products;
import com.sogyeong.cbcb.defaults.entity.response.BasicResponse;
import com.sogyeong.cbcb.defaults.entity.response.CommonResponse;
import com.sogyeong.cbcb.defaults.entity.response.ErrorResponse;
import com.sogyeong.cbcb.defaults.entity.response.ResultMessage;
import com.sogyeong.cbcb.defaults.repository.AddressRepository;
import com.sogyeong.cbcb.defaults.repository.ProductsRepository;
import com.sogyeong.cbcb.mypage.entity.UserInfo;
import com.sogyeong.cbcb.mypage.repository.UserInfoRepository;
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
public class PostController {

    AddressRepository addressRepository;
    UserInfoRepository userInfoRepository;
    ProductsRepository productsRepository;
    PostsRepository postsRepository;
    CommentRepository commentRepository;
    private WishRepository wishRepository;
    private PostsService pService;

    @PersistenceContext
    private EntityManager em;

    // 일반 게시글 저장
    @PostMapping("/posts/common")
    public ResponseEntity<? extends BasicResponse> saveCommonPost(@RequestBody PostVO PVO){
        boolean isCategory = productsRepository.existsById(PVO.getCategory_id());
        boolean isUser = userInfoRepository.existsById(PVO.getAuthor_id());
        if(!isCategory){
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ErrorResponse(ResultMessage.UNDEFINED_CATEGORY.getVal()));
        }
        if (!isUser) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ErrorResponse(ResultMessage.UNDEFINED_USER.getVal()));
        }

        PostDTO postDTO = new PostDTO();
        AlbumDTO albumDTO = new AlbumDTO();

        postDTO.setCategory_id(PVO.getCategory_id());
        postDTO.setAuthor_id(PVO.getAuthor_id());
        postDTO.setContents(PVO.getContents());
        postDTO.setTitle(PVO.getTitle());

        if(PVO.getBuy_date().contains("1일")) postDTO.setPeriod(0);
        if(PVO.getBuy_date().contains("2일")) postDTO.setPeriod(1);
        if(PVO.getBuy_date().contains("3일")) postDTO.setPeriod(2);
        if(PVO.getBuy_date().contains("일주일")) postDTO.setPeriod(3);
        if(PVO.getBuy_date().contains("2주일"))postDTO.setPeriod(4);

        postDTO.setAmount(PVO.getAmount());
        postDTO.setUnit(PVO.getUnit());
        postDTO.setTotal_price(PVO.getTotal_price());
        postDTO.setPost_addr(PVO.getPost_addr());
        postDTO.setPer_price(PVO.getPer_price());
        postDTO.setContact(PVO.getContact());

        long isSave = pService.savePosts(postDTO,PVO.getImgs());
        if(isSave!=-1) {
            Optional<Products> products = productsRepository.findById(PVO.getCategory_id());
            Optional<UserInfo> user = userInfoRepository.findById(PVO.getAuthor_id());

            long addr_seq = user.stream().findFirst().get().getAddr();
            String name = PVO.getCategory_id() <11 ? products.stream().findFirst().get().getName() : "기타";

            List sub = new ArrayList();
            LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
            map.put("category_name",name );
            map.put("address_id", addr_seq);
            map.put("posts",pService.getSubCategory(PVO.getCategory_id(),PVO.getAuthor_id(),isSave));

            sub.add(map);

            return ResponseEntity.ok().body(new CommonResponse(sub,ResultMessage.WRITE_OK.getEditVal("일반 게시글")));
        }
        else
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse(ResultMessage.WRITE_FAILED.getEditVal("일반 게시글")));
    }

    // 기타 품목 게시글 저장
    @PostMapping("/posts/etc")
    public ResponseEntity<? extends BasicResponse> saveEctPost(@RequestBody PostEctVO PVO){
        boolean isCategory = productsRepository.existsById(PVO.getCategory_id());
        boolean isUser = userInfoRepository.existsById(PVO.getAuthor_id());
        if(!isCategory){
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ErrorResponse(ResultMessage.UNDEFINED_CATEGORY.getVal()));
        }
        if (!isUser) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ErrorResponse(ResultMessage.UNDEFINED_USER.getVal()));
        }

        PostDTO postDTO = new PostDTO();
        AlbumDTO albumDTO = new AlbumDTO();

        postDTO.setCategory_id(PVO.getCategory_id());
        postDTO.setAuthor_id(PVO.getAuthor_id());
        postDTO.setContents(PVO.getContents());
        postDTO.setTitle(PVO.getTitle());

        if(PVO.getBuy_date().contains("1일")) postDTO.setPeriod(0);
        if(PVO.getBuy_date().contains("2일")) postDTO.setPeriod(1);
        if(PVO.getBuy_date().contains("3일")) postDTO.setPeriod(2);
        if(PVO.getBuy_date().contains("일주일")) postDTO.setPeriod(3);
        if(PVO.getBuy_date().contains("2주일"))postDTO.setPeriod(4);

        postDTO.setAmount(PVO.getAmount());
        postDTO.setUnit(PVO.getUnit());
        postDTO.setTotal_price(PVO.getTotal_price());
        postDTO.setPost_addr(PVO.getPost_addr());
        postDTO.setPer_price(PVO.getPer_price());
        postDTO.setContact(PVO.getContact());

        String res = pService.saveEctPosts(postDTO,PVO.getImgs(),PVO.getEct_name());
        // 게시글을 저장한다. 받은 결과 값은 게시글 순번정보와 품목 순번 정보 이다.
        long isSave = Long.valueOf(res.split(",")[0].toString()).longValue();
        long category = Long.valueOf(res.split(",")[1].toString()).longValue();
        if(isSave!=-1) {
            Optional<UserInfo> user = userInfoRepository.findById(PVO.getAuthor_id());
            long addr_seq = user.stream().findFirst().get().getAddr();

            List sub = new ArrayList();
            LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
            map.put("category_name",PVO.getEct_name());
            map.put("address_id", addr_seq);
            map.put("posts",pService.getSubCategory(category,PVO.getAuthor_id(),isSave));
            // 세부 카테고리 페이지를 불러서 붙인다.
            sub.add(map);

            return ResponseEntity.ok().body(new CommonResponse(sub,ResultMessage.WRITE_OK.getEditVal("기타 게시글")));
        }
        else
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse(ResultMessage.WRITE_FAILED.getEditVal("기타 게시글")));
    }

    //게시글 수정
    @PutMapping("/posts")
    public ResponseEntity<? extends BasicResponse> updatePost(@RequestBody UpdatePostVO PVO){
        boolean isCategory = productsRepository.existsById(PVO.getCategory_id());
        boolean isUser = userInfoRepository.existsById(PVO.getAuthor_id());
        if(!isCategory){
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ErrorResponse(ResultMessage.UNDEFINED_CATEGORY.getVal()));
        }
        if (!postsRepository.existsById(PVO.getPost_id())) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ErrorResponse(ResultMessage.UNDEFINED_POST.getVal()));
        }
        if (!isUser) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ErrorResponse(ResultMessage.UNDEFINED_USER.getVal()));
        }

        PostDTO postDTO = new PostDTO();
        AlbumDTO albumDTO = new AlbumDTO();

        postDTO.setCategory_id(PVO.getCategory_id());
        postDTO.setAuthor_id(PVO.getAuthor_id());
        postDTO.setContents(PVO.getContents());
        postDTO.setTitle(PVO.getTitle());

        if(PVO.getBuy_date().contains("1일")) postDTO.setPeriod(0);
        if(PVO.getBuy_date().contains("2일")) postDTO.setPeriod(1);
        if(PVO.getBuy_date().contains("3일")) postDTO.setPeriod(2);
        if(PVO.getBuy_date().contains("일주일")) postDTO.setPeriod(3);
        if(PVO.getBuy_date().contains("2주일"))postDTO.setPeriod(4);

        postDTO.setAmount(PVO.getAmount());
        postDTO.setUnit(PVO.getUnit());
        postDTO.setTotal_price(PVO.getTotal_price());
        postDTO.setPer_price(PVO.getPer_price());
        postDTO.setContact(PVO.getContact());

        Optional<Products> prod = productsRepository.findById(PVO.getCategory_id());
        long isSave = pService.updatePosts(PVO.getPost_id(),postDTO,PVO.getImgs());
        long category = prod.get().getSeq();
        if(isSave!=-1) {
            Optional<UserInfo> user = userInfoRepository.findById(PVO.getAuthor_id());
            long addr_seq = user.stream().findFirst().get().getAddr();
            String name = prod.get().getName();

            List sub = new ArrayList();
            LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
            map.put("category_name",name);
            map.put("address_id", addr_seq);
            map.put("posts",pService.getSubCategory(category,PVO.getAuthor_id(),PVO.getPost_id()));
            // 세부 카테고리 페이지를 불러서 붙인다.

            sub.add(map);

            return ResponseEntity.ok().body(new CommonResponse(sub,ResultMessage.UPDATE_OK.getEditVal("게시글")));
        }
        else
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse(ResultMessage.UPDATE_FAILED.getEditVal("게시글")));
    }

    //게시글 삭제
    @DeleteMapping("/posts/{post_id}/{user_id}")
    public ResponseEntity<? extends BasicResponse> deletePost(@PathVariable("post_id") long post_id,@PathVariable("user_id") long user_id){
        boolean isUser = userInfoRepository.existsById(user_id);
        if (!isUser) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ErrorResponse(ResultMessage.UNDEFINED_USER.getVal()));
        }
        if (!postsRepository.existsById(post_id)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ErrorResponse(ResultMessage.UNDEFINED_POST.getVal()));
        }
        else{
            Optional<Posts> post  = postsRepository.findById(post_id);
            if(post.get().getAuthorId()==user_id){
                Boolean isDelete = pService.deletePost(post_id);
                if(isDelete)
                    return  ResponseEntity.ok().body( new CommonResponse(ResultMessage.DELETE_OK.getEditVal("게시글")));
                else
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                            .body(new ErrorResponse(ResultMessage.DELETE_FAILED.getEditVal("게시글")));
            }
            else{
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new ErrorResponse(ResultMessage.NOT_DELETE_OTHERS.getVal()));
            }
        }
    }

    //하위카테고리리스트
    @GetMapping("/posts/category/{categoryId}/{user_id}")
    public ResponseEntity<? extends BasicResponse> getSubCategoryList(@PathVariable("categoryId") long category_id,@PathVariable("user_id") long user_id) {
        boolean isCategory = productsRepository.existsById(category_id);
        boolean isUser = userInfoRepository.existsById(user_id);
        if(!isCategory){
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ErrorResponse(ResultMessage.UNDEFINED_CATEGORY.getVal()));
        }
        if (!isUser) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ErrorResponse(ResultMessage.UNDEFINED_USER.getVal()));
        }
        else{
            Optional<Products> products = productsRepository.findById(category_id);
            Optional<UserInfo> user = userInfoRepository.findById(user_id);
            long addr_seq = user.stream().findFirst().get().getAddr();
            String name = products.stream().findFirst().get().getName();
            long seq =products.stream().findFirst().get().getSeq();
            Boolean isEct = products.stream().findFirst().get().getIsOther()==0?false:true;

            List subList = new ArrayList();
            LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
                map.put("category_id1", seq);
                map.put("category_name",name );
                map.put("isEct",isEct);
                map.put("address_id", addr_seq);
                map.put("posts",pService.getSubCategoryList(category_id,user_id,addr_seq));

            subList.add(map);

            return ResponseEntity.ok().body( new CommonResponse(subList,ResultMessage.RESULT_OK.getVal()));
        }

    }

    //채분팟 상세 페이지
    @GetMapping("/posts/{post_id}/{user_id}")
    public ResponseEntity<? extends BasicResponse> getPostDetail(@PathVariable("post_id") long post_id,@PathVariable("user_id") long user_id) {

        boolean isUser = userInfoRepository.existsById(user_id);
        if (!isUser) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ErrorResponse(ResultMessage.UNDEFINED_USER.getVal()));
        }
        if (!postsRepository.existsById(post_id)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ErrorResponse(ResultMessage.UNDEFINED_POST.getVal()));
        } else{
            Optional<Posts> post = postsRepository.findById(post_id);
            Optional<Products> products = productsRepository.findById(post.get().getProdId());
            Optional<UserInfo> user = userInfoRepository.findById(user_id);

            long addr_seq = user.stream().findFirst().get().getAddr();
            long seq =products.stream().findFirst().get().getSeq();
            String name = products.stream().findFirst().get().getName() ;

            List sub = new ArrayList();
            LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
            map.put("category_name",name );
            if(seq>10)  map.put("category_id",seq );
            map.put("address_id", addr_seq);
            map.put("posts",pService.getSubCategory(post.get().getProdId(),user_id,post_id));

            sub.add(map);

            String msg = seq >10 ? "기타 채분 게시글 " : "일반 채분 게시글 ";
            return ResponseEntity.ok().body( new CommonResponse(sub,ResultMessage.RESULT_OK.getEditVal(msg)));
        }

    }

    //댓글 저장
    @PostMapping("/posts/comment")
    public ResponseEntity<? extends BasicResponse> saveComments(@RequestBody CommentVO CVO) {

        boolean isUser = userInfoRepository.existsById(CVO.getUser_id());
        if (!isUser) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ErrorResponse(ResultMessage.UNDEFINED_USER.getVal()));
        }
        else if (!postsRepository.existsById(CVO.getPost_id())) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ErrorResponse(ResultMessage.UNDEFINED_POST.getVal()));
        }
        else{
            CommentDTO commentDTO = new CommentDTO();

            commentDTO.setPost_id(CVO.getPost_id());
            commentDTO.setUser_id(CVO.getUser_id());
            commentDTO.setCmts(CVO.getCmts());
            commentDTO.setHost_chk("N");
            
            Boolean isSave = pService.saveComments(commentDTO);

            if(isSave){
                List list = new ArrayList();
                LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
                map.put("post_id", CVO.getPost_id());
                map.put("cmts", pService.getComments(CVO.getPost_id()) );
                list.add(map);
                return  ResponseEntity.ok().body( new CommonResponse(list,ResultMessage.WRITE_OK.getEditVal("댓글")));
            }

            else
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(new ErrorResponse(ResultMessage.WRITE_FAILED.getEditVal("댓글")));
        }
    }

    // 댓글 삭제
    @DeleteMapping("/posts/comment/{comment_id}/{user_id}")
    public ResponseEntity<? extends BasicResponse> deleteComments(@PathVariable("comment_id") long comment_id,@PathVariable("user_id") long user_id){

        boolean isUser = userInfoRepository.existsById(user_id);
        if (!isUser) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ErrorResponse(ResultMessage.UNDEFINED_USER.getVal()));
        }
        else if (!commentRepository.existsById(comment_id)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ErrorResponse(ResultMessage.UNDEFINE_COMMENT.getVal()));
        }
        else{
            Boolean isDelete = pService.deleteComments(comment_id,user_id);
            if(isDelete)
                return  ResponseEntity.ok().body( new CommonResponse(ResultMessage.DELETE_OK.getEditVal("댓글")));
            else
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(new ErrorResponse(ResultMessage.DELETE_FAILED.getEditVal("댓글")));
        }
    }

    // 댓글리스트
    @GetMapping("/posts/comment/{post_id}")
    public ResponseEntity<? extends BasicResponse> getCommentsList(@PathVariable("post_id") long post_id){
        if (!postsRepository.existsById(post_id)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ErrorResponse(ResultMessage.UNDEFINED_POST.getVal()));
        }
        else{
            List list = new ArrayList();
            LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
            map.put("post_id", post_id);
            map.put("cmts", pService.getComments(post_id) );
            list.add(map);

            if (pService.getComments(post_id).size() > 0)
                return ResponseEntity.ok().body(new CommonResponse(list, ResultMessage.RESULT_OK.getEditVal("댓글들")));
            else
                return ResponseEntity.ok().body(new CommonResponse(list, ResultMessage.RESULT_FAILED.getEditVal("댓글")));
        }
    }

}
