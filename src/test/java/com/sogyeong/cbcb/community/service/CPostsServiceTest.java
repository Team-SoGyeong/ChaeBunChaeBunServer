package com.sogyeong.cbcb.community.service;

import com.sogyeong.cbcb.community.entity.CPosts;
import com.sogyeong.cbcb.community.repository.CPostsRepository;
import com.sogyeong.cbcb.community.request.CPostRequest;
import com.sogyeong.cbcb.community.response.CPostsDTO;
import com.sogyeong.cbcb.defaults.entity.Address;
import com.sogyeong.cbcb.mypage.entity.UserInfo;
import com.sogyeong.cbcb.mypage.repository.UserInfoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static java.time.LocalDate.now;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CPostsServiceTest {

    @Mock
    private CPostsRepository cPostsRepository;
    @Mock
    private UserInfoRepository userInfoRepository;
    @InjectMocks
    private CPostsService cPostsService;

    @Test
    @DisplayName("포스트 전체 조회")
    void getAllCPosts() {
        UserInfo user = UserInfo.builder()
                .seq(1L)
                .addr(100)
                .email("dddd@naver.com")
                .nickname("yooo")
                .build();
        UserInfo savedUser = userInfoRepository.save(user);
        CPosts cPosts = CPosts.builder()
                .user(user)
                .address(Address.builder().seq(user.getAddr()).build())
                .contents("test")
                .create_date(LocalDateTime.now())
                .update_date(LocalDateTime.now())
                .build();
        CPosts savedPost = cPostsRepository.save(cPosts);
        CPostRequest req = new CPostRequest();
        req.setContent("test");
        cPostsService.saveCPost(savedPost.getSeq(), req);
        final List<CPostsDTO> cPostsList = cPostsService.getAllCPosts(savedPost.getSeq(), savedUser.getSeq());

        assertThat(cPostsList.size()).isEqualTo(1);
    }

    @Test
    @DisplayName("게시물 저장 성공 테스트")
    void saveCPost_success() {
    }

    private List<CPostsDTO> cPostList(){
        List<CPostsDTO> cPostsDto = new ArrayList<>();
        for(long i = 0; i < 5; i++){
            cPostsDto.add(new CPostsDTO(i, "profile " + i, "yoo", "addr", "content", now().toString(), "img1", "img2", "img3", "img4", "img5", 0, false, 0));
        }
        return cPostsDto;
    }
//
//    @Test
//    @DisplayName("게시물 수정")
//    void updateCPost() {
//    }
//
//    @Test
//    @DisplayName("댓글 저장")
//    void saveCComment() {
//    }
}