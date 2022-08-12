package com.sogyeong.cbcb.community.service;

import com.sogyeong.cbcb.community.entity.CComment;
import com.sogyeong.cbcb.community.entity.CImages;
import com.sogyeong.cbcb.community.entity.CPosts;
import com.sogyeong.cbcb.community.repository.CCommentRepository;
import com.sogyeong.cbcb.community.repository.CPostsRepository;
import com.sogyeong.cbcb.community.request.CPostRequest;
import com.sogyeong.cbcb.community.response.CCommentDTO;
import com.sogyeong.cbcb.community.response.CPostsDTO;
import com.sogyeong.cbcb.community.response.MypageCPostDTO;
import com.sogyeong.cbcb.config.S3Uploader;
import com.sogyeong.cbcb.defaults.entity.Address;
import com.sogyeong.cbcb.defaults.entity.response.ResultMessage;
import com.sogyeong.cbcb.mypage.entity.UserInfo;
import com.sogyeong.cbcb.mypage.repository.UserInfoRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;

@Slf4j
@Service
@AllArgsConstructor
public class CPostsService {
    private final CPostsRepository cPostsRepository;
    private final CCommentRepository cCommRepository;
    private final UserInfoRepository userInfoRepository;
    private final S3Uploader s3Uploader;

    @Transactional(readOnly = true)
    public List<CPostsDTO> getAllCPosts(Long postId,Long userId){
        if(userInfoRepository.findById(userId).isEmpty())
            throw new IllegalArgumentException(ResultMessage.UNDEFINED_USER.getVal());
        if(postId>0){
            if(cPostsRepository.findById(postId).isEmpty())
            throw new IllegalArgumentException(ResultMessage.UNDEFINED_POST.getVal());
        }
        return cPostsRepository.getAllCPosts(postId,userId);
    }

    @Transactional
    public CPostsDTO saveCPost(Long authorId, CPostRequest cPostRequest){
        Optional<UserInfo> user = userInfoRepository.findById(authorId);
        if(user.isEmpty())
            throw new IllegalArgumentException(ResultMessage.UNDEFINED_USER.getVal());

        CImages cImages = new CImages();
        List<MultipartFile> imgs = cPostRequest.getImgs();
        if (!(imgs == null) && !imgs.isEmpty()){
            List<String> fileNames = new ArrayList<>();
            imgs.forEach(file -> {
                try {
                    fileNames.add(s3Uploader.upload(file, "community/" + authorId));
                } catch (IOException e) {
                    log.info("파일 변환 중 오류: ", e);
                }
            });
            cImages.setCImages(fileNames);
        }
        CPosts toSave
                = CPosts.builder()
                .cImages(cImages)
                .contents(cPostRequest.getContent())
                .user(user.get())
                .address(Address.builder().seq(user.get().getAddr()).build())
                .create_date(LocalDateTime.now())
                .update_date(LocalDateTime.now())
                .build();
        CPosts newPost = cPostsRepository.save(toSave);
        return getCPostByPostId(newPost.getSeq());
    }

    private CPostsDTO getCPostByPostId(Long postId){
        return cPostsRepository.getCPostByPostId(postId);
    }

    @Transactional(readOnly = true)
    public List<MypageCPostDTO> getMypageCPosts(String type, Long userId){
        if (!(type.equals("post") || type.equals("comm")))
            throw new IllegalArgumentException(ResultMessage.TYPE_ERROR.getVal());
        if(userInfoRepository.findById(userId).isEmpty())
            throw new IllegalArgumentException(ResultMessage.UNDEFINED_USER.getVal());
        return cPostsRepository.getMypageCPosts(type,userId);
    }

    @Transactional(readOnly = true)
    public List<CCommentDTO> getCommToPost(Long postId){
        if(cPostsRepository.findById(postId).isEmpty())
            throw new IllegalArgumentException(ResultMessage.UNDEFINED_POST.getVal());
        return cCommRepository.getCommToPost(postId);
    }

    @Transactional(readOnly = true)
    public String delPost(Long postId,Long userId){
        Optional<CPosts> postResult = cPostsRepository.findById(postId);
        if(postResult.isEmpty())
            throw new IllegalArgumentException(ResultMessage.UNDEFINED_POST.getVal());

        //1.글 자체 일련 번호로 쿼리 결과를 가져온다
        if (userId == postResult.get().getUser().getSeq()) {
            //2. 글 작성자가 본인일때만 삭제하게 한다.
            Boolean isDelete = cPostsRepository.delPostById(postId);
            if(isDelete)
                return  ResultMessage.DELETE_OK.getEditVal("커뮤니티 게시글 ");
            else  return ResultMessage.DELETE_FAILED.getEditVal("커뮤니티 게시글 ");
        }
        else
            return ResultMessage.NOT_DELETE_OTHERS.getVal();

    }

    @Transactional(readOnly = true)
    public String delCommToPost(Long commId,Long userId){
        if(cCommRepository.findById(commId).isEmpty())
            throw new IllegalArgumentException(ResultMessage.UNDEFINE_COMMENT.getVal());

        CComment commentResult = cCommRepository.findById(commId).get();

        //1. 댓글 자체 일련 번호로 쿼리 결과를 가져온다
        if (userId == commentResult.getMemberId()) {
            //2. 댓글 작성자가 본인일때만 삭제하게 한다.
            Boolean isDelete = cCommRepository.delCommentById(commId);
            if(isDelete)
                return  ResultMessage.DELETE_OK.getEditVal("댓글");
            else  return ResultMessage.DELETE_FAILED.getEditVal("댓글");
        }
        else
            return ResultMessage.NOT_DELETE_OTHERS.getVal();

    }

}
