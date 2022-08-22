package com.sogyeong.cbcb.community.service;

import com.sogyeong.cbcb.board.model.response.ResponseNotice;
import com.sogyeong.cbcb.community.entity.*;
import com.sogyeong.cbcb.community.repository.CCommentRepository;
import com.sogyeong.cbcb.community.repository.CLikeRepository;
import com.sogyeong.cbcb.community.repository.COpinionRepository;
import com.sogyeong.cbcb.community.repository.CPostsRepository;
import com.sogyeong.cbcb.community.request.CPostRequest;
import com.sogyeong.cbcb.community.request.CPostsBlindRequest;
import com.sogyeong.cbcb.community.response.CCommentDTO;
import com.sogyeong.cbcb.community.response.CLikeStatusDTO;
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

import static java.lang.Integer.parseInt;

@Slf4j
@Service
@AllArgsConstructor
public class CPostsService {
    private final CPostsRepository cPostsRepository;
    private final CCommentRepository cCommRepository;
    private final UserInfoRepository userInfoRepository;
    private final COpinionRepository cOpinionRepository;
    private  final CLikeRepository likeRepository;
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
        UserInfo user = userInfoRepository
                .findById(authorId)
                .orElseThrow(() -> new IllegalArgumentException(ResultMessage.UNDEFINED_USER.getVal()));

        CImages cImages = uploadImages(cPostRequest.getImgs(), authorId);
        CPosts toSave
                = CPosts.builder()
                .cImages(cImages)
                .contents(cPostRequest.getContent())
                .user(user)
                .address(Address.builder().seq(user.getAddr()).build())
                .create_date(LocalDateTime.now())
                .update_date(LocalDateTime.now())
                .build();
        CPosts newPost = cPostsRepository.save(toSave);
        return getCPostByPostId(newPost.getSeq());
    }

    @Transactional
    public CPostsDTO updateCPost(Long postId, Long authorId, CPostRequest cPostRequest){
        UserInfo user = userInfoRepository
                .findById(authorId)
                .orElseThrow(() -> new IllegalArgumentException(ResultMessage.UNDEFINED_USER.getVal()));

        CPosts cPosts = cPostsRepository
                .findByUserAndSeq(user, postId)
                .orElseThrow(() -> new IllegalStateException(ResultMessage.UNDEFINED_POST.getVal()));

        CImages cImages = uploadImages(cPostRequest.getImgs(), authorId);
        cPosts.update(cPostRequest.getContent(), cImages);
        return getCPostByPostId(postId);
    }

    private CImages uploadImages(List<MultipartFile> imgs, Long authorId){
        CImages cImages = new CImages();
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
        return cImages;
    }

    @Transactional
    public String saveBlind(CPostsBlindRequest blindRequest){
        boolean isUser = userInfoRepository.existsById(blindRequest.getAuthor_id());
        Optional<CPosts> posts = cPostsRepository.findById(blindRequest.getPost_id());
        if (!isUser) {
            return ResultMessage.UNDEFINED_USER.getVal();
        }
        else if(posts.isEmpty()) {
            return ResultMessage.UNDEFINED_POST.getVal();
        }
        else{
            COpinion toSave
                    = COpinion.builder()
                    .type(COpinion.Otype.BLIND)
                    .authorId(blindRequest.getAuthor_id())
                    .post(posts.get())
                    .cmtId(blindRequest.getCmt_id())
                    .reason_num(blindRequest.getReason_num())
                    .reason(blindRequest.getReason())
                    .regDate(LocalDateTime.now())
                    .build();
            COpinion newOpinion = cOpinionRepository.save(toSave);
            if(newOpinion.getSeq()>0)
                return  ResultMessage.BLIND_OK.getVal();
            else
                return ResultMessage.BLIND_FAILED.getVal();
        }
    }
    @Transactional
    public String saveReport(CPostsBlindRequest blindRequest){
        boolean isUser = userInfoRepository.existsById(blindRequest.getAuthor_id());
        Optional<CPosts> posts = cPostsRepository.findById(blindRequest.getPost_id());
        if (!isUser) {
            return ResultMessage.UNDEFINED_USER.getVal();
        }
        else if(posts.isEmpty()) {
            return ResultMessage.UNDEFINED_POST.getVal();
        }
        else{
            COpinion.Otype typ = COpinion.Otype.REPORT;
            String ment = "커뮤니티 글";

            if(parseInt(String.valueOf(blindRequest.getCmt_id()))>0){
                typ = COpinion.Otype.REPORT_CMT  ;
                ment = "커뮤니티 댓글";
            }

            COpinion toSave
                    = COpinion.builder()
                    .type(typ)
                    .authorId(blindRequest.getAuthor_id())
                    .post(posts.get())
                    .cmtId(blindRequest.getCmt_id())
                    .reason_num(blindRequest.getReason_num())
                    .reason(blindRequest.getReason())
                    .regDate(LocalDateTime.now())
                    .build();
            COpinion newOpinion = cOpinionRepository.save(toSave);
            if(newOpinion.getSeq()>0)
                return  ResultMessage.REPORT_OK.getEditVal(ment);
            else
                return ResultMessage.REPORT_FAILED.getEditVal(ment);
        }
    }

    private CPostsDTO getCPostByPostId(Long postId){
        return cPostsRepository.getCPostByPostId(postId);
    }

    public List<CLikeStatusDTO> getLikeStatus(Long postId, Long userId){
        return likeRepository.existLikeStatus(postId,userId);
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

    @Transactional(readOnly = true)
    public String saveWish(Long postId, Long userId) {

        boolean isUser = userInfoRepository.existsById(userId);
        Optional<CPosts> posts = cPostsRepository.findById(postId);
        UserInfo author = posts.get().getUser();
        if (!isUser) {
            return ResultMessage.UNDEFINED_USER.getVal();
        }
        else if(posts.isEmpty()) {
            return ResultMessage.UNDEFINED_POST.getVal();
        }
        else if (author.getSeq() == userId){
            return ResultMessage.NOT_LIKE_SELF.getVal();
        }
        else{
            if(likeRepository.existLike(postId,userId)) {
                if(likeRepository.delLikeById(postId,userId)){
                    return  ResultMessage.LIKE_CANCEL_OK.getVal();
                }
                else return ResultMessage.LIKE_CANCEL_FAILED.getVal();
            }
            else{
                CLike toSave
                        = CLike.builder()
                        .post(posts.get())
                        .authorId(author.getSeq())
                        .member(userId)
                        .host_chk("N")
                        .regDate(LocalDateTime.now())
                        .build();
                CLike newLike = likeRepository.save(toSave);
                if(newLike.getSeq()>0)
                    return  ResultMessage.LIKE_OK.getVal();
                else
                    return ResultMessage.LIKE_FAILED.getVal();
            }
        }
    }

    public List getMyNotice(Long userId){
        userInfoRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException(ResultMessage.UNDEFINED_USER.getVal()));
        return cPostsRepository.getNoticeListAll(userId);
    }

    @Transactional(readOnly = false)
    public int hostClickByScrap(long seq){
        Optional<CLike> likes = likeRepository.findById(seq);
        likes.ifPresent(updateWish->{
            updateWish.setHost_chk("Y");
            likeRepository.save(updateWish);
        });
        return likes.stream().count()==1 ? 1:-1;
    }

    @Transactional(readOnly = false)
    public int hostClickByComment(long seq){
        Optional<CComment> cmts = cCommRepository.findById(seq);
        cmts.ifPresent(updateCmt->{
            updateCmt.setHost_chk("Y");
            cCommRepository.save(updateCmt);
        });
        return cmts.stream().count()==1 ? 1:-1;
    }
}
