package com.sogyeong.cbcb.community.repository;

import com.sogyeong.cbcb.community.entity.CComment;
import com.sogyeong.cbcb.community.entity.CLike;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.EntityManager;
import java.util.List;

public class CLikeRepositoryImpl implements CLikeRepositoryCustom{
    @Autowired
    private  EntityManager em;

    @Override
    public boolean delWishById(Long postId, Long userId) {
        try {
            em.createNativeQuery("DELETE FROM community_like WHERE post_id = :postId and member= :user", CLike.class)
                    .setParameter("postId", postId)
                    .setParameter("user", userId)
                    .executeUpdate();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean existWish(Long postId, Long userId) {

         List<CLike> clike  =  em.createNativeQuery("select * from community_like WHERE post_id = :postId and member = :user ", CLike.class)
            .setParameter("postId", postId)
            .setParameter("user", userId)
                .getResultList();
        return clike.size() >0 ? true : false;
    }
}
