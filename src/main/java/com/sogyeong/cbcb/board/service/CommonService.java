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
import java.util.LinkedHashMap;
import java.util.LinkedList;
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
    public List storeWish(WishDTO wDTO) {
        List res = new LinkedList();
        int lastSeq= wishRepository.getLastSeq();
        Wish wResult = wishRepository.save(wDTO.toEntity());

        LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
        map.put("status",1);
        map.put("wish_cnts", wishRepository.getWish(wDTO.getPost_id()));
        res.add(map);

        if(wResult.getSeq()>lastSeq)
            return res;
        else return res;
    }

    @Transactional(readOnly = true)
    public List deleteWish(long postId, long userId) {
        List<Wish> wishResult = wishRepository.getWish(postId,userId);
        List res = new LinkedList();

        if (userId == wishResult.get(0).getMember()) {
            wishRepository.deleteByIdAndMember(postId,userId);
            LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
            map.put("status",0);
            map.put("wish_cnts",wishRepository.getWish(postId));
            res.add(map);

            return res;
        }
        return res;
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
