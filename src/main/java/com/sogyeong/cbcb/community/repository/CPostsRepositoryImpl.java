package com.sogyeong.cbcb.community.repository;

import com.sogyeong.cbcb.community.response.CPostsDTO;
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
                        "(select count(*) from community_posts cp join community_like cl on cp.seq = cl.post_id) as like_count, " +
                        "exists(select * from community_like cl where cl.member = :userId and cl.post_id = cp.seq) as is_like " +
                        "from community_posts cp " +
                        "join user_info ui on cp.user_id = ui.info_id " +
                        "join default_address da on cp.address_id = da.local_code " +
                        "order by cp.create_date desc", "CPostsDTOMapping")
                .setParameter("userId", userId)
                .getResultList();
    }
}


