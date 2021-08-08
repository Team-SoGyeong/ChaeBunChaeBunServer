package com.sogyeong.cbcb.mypage.service;

import com.sogyeong.cbcb.board.model.ResponseHomeList;
import com.sogyeong.cbcb.mypage.model.response.ResponseMyDedelineList;
import com.sogyeong.cbcb.mypage.model.response.ResponseMyPostList;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

@Service
@AllArgsConstructor
public class MyPostService {

    @PersistenceContext
    private EntityManager em;

    public List getMyPostList(int platform, int state, long user) {
        // platform = 1
        List resultList =  em.createNativeQuery(
                "select "+
                        "dp.seq as categoryId, " +
                        "dp.name, bp.seq as postId, " +
                        "bp.title, " +
                        "case "+
                        "when bp.period =0 then '1일 전 구매' " +
                        "when bp.period =1 then '2일 전 구매' " +
                        "when bp.period =2 then '3일 전 구매' " +
                        "when bp.period =3 then '1주일 이내 구매' " +
                        "else '2주일 이내 구매' " +
                        "end as buy_date, " +
                        "bp.headcount+'명' as headcount , " +
                        "FORMAT(bp.per_price,0) as price, " +
                        "ba.isAuth, ba.img1, " +
                        "date_format(bp.reg_date,'%m/%d') as dates, " +
                        "TIMESTAMPDIFF(day,bp.reg_date,now()) as diff " +
                        "from board_posts bp " +
                        "join default_products dp on bp.prod_id = dp.seq " +
                        "join board_album ba on bp.seq = ba.post_id " +
                        "join user_info ui on bp.author_id = ui.info_id " +
                        "where  ui.info_id = :user and " +
                        "case when :state=1 then " +
                        "   bp.status = 1 " +
                        "   when :state=0 then " +
                        "   bp.status = 0 " +
                        "   else " +
                        "   1=1 " +
                        "end "+
                        "order by diff desc ,dp.seq ")
                .setParameter("state", state)
                .setParameter("user", user)
                .getResultList();

        List myPostLists = new ArrayList<ResponseMyPostList>();
        for (Object o: resultList){
            Object[] res = (Object[]) o;

            LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();

            map.put("category_id", res[0]);
            map.put("category_name", res[1]);
            map.put("post_id", res[2]);
            map.put("title", res[3]);
            map.put("buy_date",res[4]);
            map.put("members", res[5]);
            map.put("per_price", res[6].toString()+'원');
            map.put("isAuth", res[7]);
            map.put("url", res[8]);
            map.put("witten_by", res[9]);

            myPostLists.add(map);
        }
        return myPostLists;
    }
    public List getMyDeadlineList(long user) {
        // platform = 1
        List resultList =  em.createNativeQuery(
                "select "+
                        "bp.seq as postId, " +
                        "bp.title, ui.nickname " +
                        "from board_posts bp " +
                        "join default_products dp on bp.prod_id = dp.seq " +
                        "join user_info ui on bp.author_id = ui.info_id " +
                        "where  ui.info_id = :user and bp.status = 0 and TIMESTAMPDIFF(day,bp.reg_date,now()) >=7 " +
                        "order by dp.seq ")
                .setParameter("user", user)
                .getResultList();

        List myDeadlineLists = new ArrayList<ResponseMyDedelineList>();
        for (Object o: resultList){
            Object[] res = (Object[]) o;

            LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();

            map.put("post_id", res[0]);
            map.put("post_title", res[1]);
            map.put("title", res[2]);

            myDeadlineLists.add(map);
        }
        return myDeadlineLists;
    }

}
