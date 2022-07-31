package com.sogyeong.cbcb.community.repository;

import com.sogyeong.cbcb.community.response.CPostsDTO;
import com.sogyeong.cbcb.community.response.MypageCPostDTO;
import lombok.RequiredArgsConstructor;

import javax.persistence.EntityManager;
import java.util.List;

@RequiredArgsConstructor
public class CPostsRepositoryImpl implements CPostsRepositoryCustom{
    private final EntityManager em;

    @Override
    public List<CPostsDTO> getAllCPosts(Long userId) {
        return em.createNativeQuery(
                "select cp.seq, ui.profile, ui.nickname, da.neighborhood, cp.contents, " +
                        "date_format(cp.create_date,'%m/%d') as create_date, " +
                        "cp.img1, cp.img2, cp.img3, cp.img4, cp.img5, " +
                        "(select count(*) from community_like cl where cp.seq = cl.post_id) as like_count, " +
                        "exists(select * from community_like cl where cl.member = :userId and cl.post_id = cp.seq) as is_like, " +
                        "(select count(*) from  community_comment cc where cp.seq = cc.post_id) as comm_count " +
                        "from community_posts cp " +
                        "join user_info ui on cp.user_id = ui.info_id " +
                        "join default_address da on cp.address_id = da.local_code " +
                        "where cp.seq not in (select post_id from community_opinion  where types ='blind' and author_id = :userId) "+
                        "order by cp.create_date desc", "CPostsDTOMapping")
                .setParameter("userId", userId)
                .getResultList();
    }

    @Override
    public List<MypageCPostDTO> getMypageCPosts(String type, Long userId) {
        String  query ="";

        if(type.equals("post")){
            query = "select cp.seq as postId, ui.info_id as userId, cp.contents, " +
                    "(select count(*) from community_like cl where cp.seq = cl.post_id and cl.author_id <> :userId ) as like_count, " +
                    "(select count(*) from  community_comment cc where cp.seq = cc.post_id ) as comm_count " +
                    "from community_posts cp " +
                    "join user_info ui on cp.user_id = ui.info_id " +
                    "where cp.user_id =:userId and cp.seq not in (select post_id from community_opinion  where types ='blind' and author_id = :userId) " +
                    "order by cp.create_date desc";
        }
        else if(type.equals("comm")){
            query = "select cp.seq as postId, ui.info_id as userId, cp.contents, " +
                    "(select count(*) from community_like cl where cp.seq = cl.post_id and cl.author_id <> :userId ) as like_count, " +
                    "(select count(*) from  community_comment cc where cp.seq = cc.post_id ) as comm_count " +
                    "from community_posts cp  " +
                    "left join community_comment cc on cc.post_id = cp.seq " +
                    "join user_info ui on cp.user_id = ui.info_id " +
                    "where cc.member =:userId and cp.seq not in (select post_id from community_opinion  where types ='blind' and author_id = :userId) " +
                    "order by cp.create_date desc";
        }

        return em.createNativeQuery(
                        query, "MypageCPostsDTOMapping")
                .setParameter("userId", userId)
                .getResultList();
    }
}


