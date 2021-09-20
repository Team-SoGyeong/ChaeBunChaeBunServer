package com.sogyeong.cbcb.board.service;

import com.sogyeong.cbcb.board.entity.Comment;
import com.sogyeong.cbcb.board.entity.Posts;
import com.sogyeong.cbcb.board.entity.Wish;
import com.sogyeong.cbcb.board.model.dto.WishDTO;
import com.sogyeong.cbcb.board.repository.CommentRepository;
import com.sogyeong.cbcb.board.repository.PostsRepository;
import com.sogyeong.cbcb.board.repository.WishRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class CommonService {

    private WishRepository wishRepository;
    private CommentRepository cRepository;
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

    @Transactional(readOnly = false)
    public int hostClickByScrap(long seq){
        Optional<Wish> wishs = wishRepository.findById(seq);
        wishs.ifPresent(updateWish->{
            updateWish.setHost_chk("Y");
            wishRepository.save(updateWish);
        });
        return wishs.stream().count()==1 ? 1:-1;
    }

    @Transactional(readOnly = false)
    public int hostClickByComment(long seq){
        Optional<Comment> cmts = cRepository.findById(seq);
        cmts.ifPresent(updateCmt->{
            updateCmt.setHost_chk("Y");
            cRepository.save(updateCmt);
        });
        return cmts.stream().count()==1 ? 1:-1;
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
