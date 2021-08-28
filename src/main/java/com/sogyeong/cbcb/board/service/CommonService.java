package com.sogyeong.cbcb.board.service;

import com.sogyeong.cbcb.board.entity.Posts;
import com.sogyeong.cbcb.board.entity.Wish;
import com.sogyeong.cbcb.board.model.response.WishDTO;
import com.sogyeong.cbcb.board.repository.PostsRepository;
import com.sogyeong.cbcb.board.repository.WishRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class CommonService {

    private WishRepository wishRepository;
    private PostsRepository postsRepository;

    @PersistenceContext
    EntityManager em;

    @Transactional(readOnly = false)
    public Boolean storeWish(WishDTO wDTO) {
        int lastSeq= wishRepository.getLastSeq();
        Wish wResult = wishRepository.save(wDTO.toEntity());
        if(wResult.getSeq()>lastSeq)
            return true;
        else return false;
    }

    @Transactional(readOnly = true)
    public Boolean deleteWish(long postId, long userId) {
        List<Wish> wishResult = wishRepository.getWish(postId,userId);

        if (userId == wishResult.get(0).getMember()) {
            wishRepository.deleteByIdAndMember(postId,userId);
            return true;
        }
        return false;
    }

    @Transactional
    public Boolean setPostStatus(long postId){
        Optional<Posts> posts = postsRepository.findById(postId);
        if(posts.get().getStatus()==1){
            return false;
        }
        else{
            posts.ifPresent(updatePost->{
                updatePost.setStatus(1);
                postsRepository.save(updatePost);
            });
            return true;
        }
    }

    @Transactional
    public Boolean setPostDonated(long postId){
        Optional<Posts> posts = postsRepository.findById(postId);
        if(posts.get().getIsDonated()==1){
            return false;
        }
        else{
            posts.ifPresent(updatePost->{
                updatePost.setStatus(1);
                updatePost.setIsDonated(1);
                postsRepository.save(updatePost);
            });
            return true;
        }
    }
}