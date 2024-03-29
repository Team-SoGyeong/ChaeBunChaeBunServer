package com.sogyeong.cbcb.community.repository;

import com.sogyeong.cbcb.board.model.response.ResponseNotice;
import com.sogyeong.cbcb.community.entity.CPosts;
import com.sogyeong.cbcb.community.response.CPostsDTO;
import com.sogyeong.cbcb.community.response.MypageCPostDTO;
import lombok.RequiredArgsConstructor;

import javax.persistence.EntityManager;
import javax.persistence.StoredProcedureQuery;
import java.util.*;

@RequiredArgsConstructor
public class CPostsRepositoryImpl implements CPostsRepositoryCustom{
    private final EntityManager em;

    @Override
    public List<CPostsDTO> getAllCPosts(Long postId, Long userId) {
        return em.createNativeQuery(
                "select cp.seq, cp.user_id, ui.profile, ui.nickname, da.neighborhood, cp.contents, " +
                        "date_format(cp.create_date,'%m/%d') as create_date, " +
                        "cp.img1, cp.img2, cp.img3, cp.img4, cp.img5, " +
                        "(select count(*) from community_like cl where cp.seq = cl.post_id) as like_count, " +
                        "exists(select * from community_like cl where cl.member = :userId and cl.post_id = cp.seq) as is_like, " +
                        "(select count(*) from  community_comment cc where cp.seq = cc.post_id) as comm_count " +
                        "from community_posts cp " +
                        "join user_info ui on cp.user_id = ui.info_id " +
                        "join default_address da on cp.address_id = da.local_code " +
                        "where cp.seq not in (select post_id from community_opinion  where types ='blind' and author_id = :userId) "+
                        "and (case when :postId>0 then cp.seq else 1 end ) = (case when :postId>0 then :postId  else 1 end ) "+
                        "order by cp.create_date desc", "CPostsDTOMapping")
                .setParameter("userId", userId)
                .setParameter("postId",postId.intValue())
                .getResultList();
    }

    @Override
    public List<MypageCPostDTO> getMypageCPosts(String type, Long userId) {
        String  query ="";

        if(type.equals("post")){
            query = "select distinct cp.seq as postId, ui.info_id as userId, cp.contents, " +
                    "(select count(*) from community_like cl where cp.seq = cl.post_id and cl.author_id <> :userId ) as like_count, " +
                    "(select count(*) from  community_comment cc where cp.seq = cc.post_id ) as comm_count, " +
                    "(select  case when count(seq) > 0 then 1 else 0 end  from community_like cl where cl.member = :userId and cl.post_id = cp.seq) as is_like " +
                    "from community_posts cp " +
                    "join user_info ui on cp.user_id = ui.info_id " +
                    "where cp.user_id =:userId and cp.seq not in (select post_id from community_opinion  where types ='blind' and author_id = :userId) " +
                    "order by cp.create_date desc";
        }
        else if(type.equals("comm")){
            query = "select distinct cp.seq as postId, ui.info_id as userId, cp.contents, " +
                    "(select count(*) from community_like cl where cp.seq = cl.post_id and cl.author_id <> :userId ) as like_count, " +
                    "(select count(*) from  community_comment cc where cp.seq = cc.post_id ) as comm_count, " +
                    "(select  case when count(seq) > 0 then 1 else 0 end  from community_like cl where cl.member = :userId and cl.post_id = cp.seq) as is_like " +
                    "from community_posts cp  " +
                    "left join community_comment cc on cc.post_id = cp.seq " +
                    "join user_info ui on cp.user_id = ui.info_id " +
                    "where cc.member =:userId and cp.seq not in (select post_id from community_opinion  where types ='blind' and author_id = :userId) " +
                    "order by cc.reg_date desc";
        }

        return em.createNativeQuery(
                        query, "MypageCPostsDTOMapping")
                .setParameter("userId", userId)
                .getResultList();
    }

    @Override
    public CPostsDTO getCPostByPostId(Long postId) {
        return (CPostsDTO) em.createNativeQuery(
                "select cp.seq, cp.user_id, ui.profile, ui.nickname, da.neighborhood, cp.contents, " +
                        "date_format(cp.create_date,'%m/%d') as create_date, " +
                        "cp.img1, cp.img2, cp.img3, cp.img4, cp.img5, " +
                        "(select count(*) from community_like cl where cp.seq = cl.post_id) as like_count, " +
                        "false as is_like, " +
                        "(select count(*) from  community_comment cc where cp.seq = cc.post_id) as comm_count " +
                        "from community_posts cp " +
                        "join user_info ui on cp.user_id = ui.info_id " +
                        "join default_address da on cp.address_id = da.local_code " +
                        "where cp.seq = :postId "+
                        "order by cp.create_date desc", "CPostsDTOMapping")
                .setParameter("postId", postId)
                .getSingleResult();
    }

    @Override
    public List<LinkedHashMap<String, Object>> getNoticeListAll(Long userId){
        StoredProcedureQuery spqq =
                em.createNamedStoredProcedureQuery(CPosts.sp_getNoticeList);
        spqq.setParameter("_USERID", userId.intValue());
        List resultList = spqq.getResultList();
        List noticeList = new ArrayList<ResponseNotice>();
        for (Object o:  resultList) {
            Object[] res = (Object[]) o;
            LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
            map.put("notice_id", res[8]);
            map.put("caseBy", res[0]);
            map.put("nickname", res[10]);
            map.put("post_id", res[13]);
            map.put("category_id", res[14]);
            map.put("title", res[1]);
            map.put("img1", res[11]);
            map.put("contents", res[12]);
            map.put("buy_date", res[2]);
            map.put("total_price", res[3]);
            map.put("dates", res[5]);
            map.put("isClick", res[7]);
            map.put("isNew", res[15]);
            map.put("isAuth", res[9]);

            noticeList.add(map);
        }
        return noticeList;
    }
                
    @Override
    public boolean delPostById(Long postId) {
        try {
            em.createNativeQuery("DELETE FROM community_posts WHERE seq = ?", CPosts.class)
                    .setParameter(1, postId)
                    .executeUpdate();
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}


