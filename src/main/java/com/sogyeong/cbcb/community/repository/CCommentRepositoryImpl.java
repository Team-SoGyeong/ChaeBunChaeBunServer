package com.sogyeong.cbcb.community.repository;

import com.sogyeong.cbcb.community.entity.CComment;
import com.sogyeong.cbcb.community.response.CCommentDTO;
import lombok.RequiredArgsConstructor;

import javax.persistence.EntityManager;
import java.util.List;

@RequiredArgsConstructor
public class CCommentRepositoryImpl implements CCommentRepositoryCustom{
    private final EntityManager em;

    @Override
    public List<CCommentDTO> getCommToPost(Long postId) {
        return em.createNativeQuery(
                "select cp.seq as postId, cc.seq as commId, cc.member as comm_user, ui.profile, ui.nickname, cc.contents, " +
                        "date_format(cc.reg_date,'%m/%d %H:%i') as create_date " +
                        "from community_posts cp " +
                        "left join community_comment cc on cc.post_id = cp.seq " +
                        "join user_info ui on cc.member = ui.info_id " +
                        "where cp.seq = :postId "+
                        "order by cc.reg_date desc ", "CCommentDTOMapping")
                .setParameter("postId", postId)
                .getResultList();
    }
    @Override
    public boolean delCommentById(Long commId)
    {
        try {
            em.createNativeQuery("DELETE FROM community_comment WHERE seq = ?", CComment.class)
                    .setParameter(1, commId)
                    .executeUpdate();
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}


