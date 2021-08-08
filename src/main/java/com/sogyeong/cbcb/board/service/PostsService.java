package com.sogyeong.cbcb.board.service;

import com.sogyeong.cbcb.board.entity.Album;
import com.sogyeong.cbcb.board.model.ResponseSubList;
import com.sogyeong.cbcb.board.repository.AlbumRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class PostsService {

    private AlbumRepository albumRepository;

    @PersistenceContext
    EntityManager em;

    public List getSubCategoryList(long category_id,long addr_seq) {
        List resultList =  em.createNativeQuery(
                        "select "+
                                "bp.seq as postId, " +
                                "ui.info_id as userId, " +
                                "ui.nickname, " +
                                "ui.profile, " +
                                "bp.title, " +
                                "bp.contents, " +
                                "case "+
                                "when bp.period =0 then '1일 전 구매' " +
                                "when bp.period =1 then '2일 전 구매' " +
                                "when bp.period =2 then '3일 전 구매' " +
                                "when bp.period =3 then '1주일 이내 구매' " +
                                "else '2주일 이내 구매' " +
                                "end as buy_date, " +
                                "bp.headcount+'명' as headcount , " +
                                "FORMAT(bp.per_price,0) as price," +
                                "( select IFNULL(count(seq),0)" +
                                "   from  board_wish" +
                                "   where post_id =bp.seq" +
                                ") as wish_cnts, " +
                                "( select IFNULL(count(seq),0) " +
                                "  from  board_comment" +
                                "  where post_id =bp.seq " +
                                ") as comment_cnts, " +
                                "ba.isAuth, " +
                                "date_format(bp.reg_date,'%m/%d') as dates, " +
                                "TIMESTAMPDIFF(day,bp.reg_date,now()) as diff "+
                                "from board_posts bp " +
                                "join default_products dp on bp.prod_id = dp.seq " +
                                "join board_album ba on bp.seq = ba.post_id " +
                                "join user_info ui on bp.author_id = ui.info_id " +
                                "where ui.address = :addrId and  bp.prod_id = :categoryId and bp.status = 0 " + // 소분이 완료되지않는경우만 나오게 하기
                                "and TIMESTAMPDIFF(day,bp.reg_date,now()) < 7 " +
                                "order by diff desc , bp.prod_id ")
                .setParameter("categoryId", category_id)
                .setParameter("addrId", addr_seq)
                .getResultList();

        List subList = new ArrayList<ResponseSubList>();
        for (Object o: resultList){
            Object[] res = (Object[]) o;

            LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
            LinkedHashMap<String, Object> map2 = new LinkedHashMap<String, Object>();
            Optional<Album> album = albumRepository.findById(Long.valueOf(res[0].toString()).longValue());

            map.put("post_id", res[0]);
            map.put("user_id", res[1]);
            map.put("nickname", res[2]);
            map.put("profile", res[3]);
            map.put("title", res[4]);
            map.put("contents",res[5]);
            map.put("buy_date", res[6]);
            map.put("headcounts", res[7].toString()+'원');
            map.put("per_price", res[8]);
            map.put("wish_cnts", res[9]);
            map.put("comment_cnts", res[10]);
            map.put("isAuth", res[11]);
            map.put("imgs", album);
            map.put("witten_by", res[12]);

            subList.add(map);
        }
        return subList;

    }
}
