package com.sogyeong.cbcb.test;

import com.sogyeong.cbcb.community.entity.CLike;
import com.sogyeong.cbcb.community.entity.COpinion;
import com.sogyeong.cbcb.community.entity.CPosts;
import com.sogyeong.cbcb.community.repository.CCommentRepository;
import com.sogyeong.cbcb.community.repository.CLikeRepository;
import com.sogyeong.cbcb.community.repository.COpinionRepository;
import com.sogyeong.cbcb.community.repository.CPostsRepository;
import com.sogyeong.cbcb.community.request.CPostsBlindRequest;
import com.sogyeong.cbcb.community.response.CCommentDTO;
import com.sogyeong.cbcb.community.response.CPostsDTO;
import com.sogyeong.cbcb.community.service.CPostsService;
import org.junit.jupiter.api.DisplayName;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class CPostsServiceTest {

    @Autowired
    CPostsService cPostsService;

    @Autowired
    CPostsRepository cPostsRepository;

    @Autowired
    COpinionRepository cOpinionRepository;

    @Autowired
    CCommentRepository cCommRepository;

    @Autowired
    CLikeRepository likeRepository;


    @org.junit.jupiter.api.Test
    @Transactional
    @DisplayName("이 글 더이상 안보기")
    void saveBlind() {
        COpinion beforeSave = cOpinionRepository.findAll(Sort.by(Sort.Direction.DESC, "seq")).get(0);
        CPosts posts = CPosts.builder().seq(1).build();
        COpinion toSave
                = COpinion.builder()
                .type(COpinion.Otype.BLIND)
                .authorId(105)
                .post(posts)
                .cmtId(0)
                .reason_num(1)
                .reason("")
                .regDate(LocalDateTime.now())
                .build();

        COpinion afterSave = cOpinionRepository.save(toSave);
        System.out.println("-------------------------- step 1 : Opinion 테이블에 안보기처리 요청 글+요청자 정보 삽입절차 확인 -------------------------- ");
        System.out.println("저장 전: "+beforeSave.getSeq());
        System.out.println("저장 후: "+afterSave.getSeq());
        assertThat( afterSave.getSeq() ).isGreaterThan(beforeSave.getSeq());

        Optional<CPostsDTO> list = cPostsService.getAllCPosts(posts.getSeq(),105L).stream().filter(s -> s.getPostId().equals(posts.getSeq())).findAny();
        System.out.println("-------------------------- step 2 : Opinion 테이블에 안보기처리 요청글이 요청자의 게시글전체보기에 없는지 확인 -------------------------- ");
        System.out.println(posts.getSeq()+"번 글 출력개수 : "+ list.stream().count());
        assertThat( list.stream().count() ).isEqualTo(0);
    }



    @org.junit.jupiter.api.Test
    @Transactional
    @DisplayName("글 삭제")
    void delPost() {
        System.out.println("-------------------------- step 1 : 글을 지우면 연관된 댓글, 좋아요, 안보이기 처리가 지워지는 지 확인 -------------------------- ");
        cPostsRepository.delPostById(1L);
        System.out.println("-------------------------- step 2 :연관된 댓글 내역 확인 -------------------------- ");
        int cmm = cCommRepository.getCommToPost(1L).size();
        System.out.println("1번글 댓글 갯수 : "+cmm);
        assertThat(cmm).isEqualTo(0);
        System.out.println("-------------------------- step 3 :연관된 좋아요 내역 확인 -------------------------- ");
        long like = cPostsRepository.getNoticeListAll(104L).stream().filter(s -> s.containsKey("caseBy") && s.containsValue("like")).filter(s -> s.containsKey("post_id") && s.containsValue(1)).count();
        System.out.println("1번글 좋아요 내역 : "+like);
        assertThat( like).isEqualTo(0);
        System.out.println("-------------------------- step 4 :연관된 의견 내역 확인 -------------------------- ");
        long opinion = cOpinionRepository.findAll().stream().filter(s->s.getPost().getSeq()==1).count();
        System.out.println("1번글 의견 내역 : "+opinion);
        assertThat(opinion).isEqualTo(0);
    }


    @org.junit.jupiter.api.Test
    @Transactional
    @DisplayName("좋아요 처리")
    void saveWish() {
//        CPosts posts = CPosts.builder().seq(6).build();
//        CLike toSave
//                = CLike.builder()
//                .post(posts)
//                .authorId(104)
//                .member(98)
//                .host_chk("N")
//                .regDate(LocalDateTime.now())
//                .build();
//        CLike newLike = likeRepository.save(toSave);
//        System.out.println(newLike.getSeq());
//        Optional<LinkedHashMap<String,Object>> like = cPostsRepository.getNoticeListAll(104L).stream().filter(s -> s.containsKey("caseBy") && s.containsValue("like")).filter(s -> s.containsKey("post_id") && s.containsValue(6)).findAny();
//        System.out.println("6번글 98번 회원의 좋아요 유무 : "+like.stream().count());
//        System.out.println("6번글 98번 회원의 좋아요 신규여부 : "+like.get().containsKey("isNew"));
//        System.out.println("6번글 98번 회원의 좋아요 읽기여부 : "+like.get().containsKey("isClick"));
//        assertThat(like.stream().count()).isEqualTo(1);
//
//        프로시저로 인해 생명주기가 독립적이라 불가
    }


}