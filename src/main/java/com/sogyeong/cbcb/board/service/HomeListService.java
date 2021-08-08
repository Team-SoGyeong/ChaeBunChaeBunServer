package com.sogyeong.cbcb.board.service;

import com.sogyeong.cbcb.board.model.ResponseHomeList;
import com.sogyeong.cbcb.defaults.entity.Products;
import com.sogyeong.cbcb.defaults.repository.ProductsRepository;
import com.sogyeong.cbcb.mypage.model.response.ResponseMyPostList;
import com.sogyeong.cbcb.mypage.model.response.ResponseMyWishtList;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

@Service
@AllArgsConstructor
public class HomeListService {

    ProductsRepository productsRepository;

    @PersistenceContext
    EntityManager em;

    public List getCategoryList() {
        List<Products> list = productsRepository.findAll();
        System.out.println("username=" + list.size());

        List category_list = new ArrayList<Object>(); // 매핑 한거 받는 타입입

        for (Products li : list) {
            LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
            map.put("name",li.getName());
            map.put("category_id",li.getSeq());
            if(li.getSeq()>11) break;
            category_list.add(map);
        }

        return category_list;
    }
    public List getNewList(long addrSeq) {
        List resultList =  em.createNativeQuery(
                "select "+
                        "dp.seq as categoryId, " +
                        "dp.name, bp.seq as postId, " +
                        "ui.info_id ,  bp.title, " +
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
                        "date_format(bp.reg_date,'%m/%d') as dates " +
                        "from board_posts bp " +
                        "join default_products dp on bp.prod_id = dp.seq " +
                        "join board_album ba on bp.seq = ba.post_id " +
                        "join user_info ui on bp.author_id = ui.info_id " +
                        "where ui.address = :addrId and bp.status = 0 " +
                        "order by dates desc, dp.seq " +
                        "limit 3 ")
                .setParameter("addrId", addrSeq)
                .getResultList();

        List newList = new ArrayList<ResponseHomeList>();
        for (Object o: resultList){
            Object[] res = (Object[]) o;

            LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();

            map.put("category_id", res[0]);
            map.put("category_name", res[1]);
            map.put("post_id", res[2]);
            map.put("author_id", res[3]);
            map.put("title", res[4]);
            map.put("buy_date",res[5]);
            map.put("members", res[6]);
            map.put("per_price", res[7].toString()+'원');
            map.put("isAuth", res[8]);
            map.put("url", res[9]);
            map.put("witten_by", res[10]);

            newList.add(map);
        }
        return newList;
    }
    public List getDeadlineList(long addrSeq) {
        List resultList =  em.createNativeQuery(
                "select "+
                        "dp.seq as categoryId, " +
                        "dp.name, bp.seq as postId, " +
                        "ui.info_id ,  bp.title, " +
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
                        "TIMESTAMPDIFF(day,bp.reg_date,now()) as diff "+
                        "from board_posts bp " +
                        "join default_products dp on bp.prod_id = dp.seq " +
                        "join board_album ba on bp.seq = ba.post_id " +
                        "join user_info ui on bp.author_id = ui.info_id " +
                        "where ui.address = :addrId and bp.status = 0 " + // 소분이 완료되지않는경우만 나오게 하기
                        "and (TIMESTAMPDIFF(day,bp.reg_date,now()) > 4 and TIMESTAMPDIFF(day,bp.reg_date,now()) < 7)" +
                        "order by diff desc , dp.seq  " +
                        "limit 3 ")
                .setParameter("addrId", addrSeq)
                .getResultList();

        List deadlineList = new ArrayList<ResponseHomeList>();
        for (Object o: resultList){
            Object[] res = (Object[]) o;

            LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();

            map.put("category_id", res[0]);
            map.put("category_name", res[1]);
            map.put("post_id", res[2]);
            map.put("author_id", res[3]);
            map.put("title", res[4]);
            map.put("buy_date",res[5]);
            map.put("members", res[6]);
            map.put("per_price", res[7].toString()+'원');
            map.put("isAuth", res[8]);
            map.put("url", res[9]);
            map.put("witten_by", res[10]);

            deadlineList.add(map);
        }
        return deadlineList;
    }
    public List getMyLikeList(long user) {
        // platform = 1
        List resultList =  em.createNativeQuery(
                        "select "+
                                "dp.seq as category_id, " +
                                "dp.name, bw.seq as wish_id, " +
                                "bp.seq as post_id, " +
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
                                "from board_wish bw " +
                                "join board_posts bp on  bw.post_id = bp.seq " +
                                "join default_products dp on bp.prod_id = dp.seq " +
                                "join board_album ba on bp.seq = ba.post_id " +
                                "join user_info ui on bp.author_id = ui.info_id " +
                                "where  bw.member = :user and bp.status = 0 and bw.author_id <> :user " +
                                "order by diff desc , dp.seq " +
                                "limit 3")
                .setParameter("user", user)
                .getResultList();

        List wishLists = new ArrayList<ResponseMyWishtList>();
        for (Object o: resultList){
            Object[] res = (Object[]) o;

            LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();

            map.put("category_id", res[0]);
            map.put("category_name", res[1]);
            map.put("wish_id", res[2]);
            map.put("post_id", res[3]);
            map.put("title", res[4]);
            map.put("buy_date",res[5]);
            map.put("members", res[6]);
            map.put("per_price", res[7].toString()+'원');
            map.put("isAuth", res[8]);
            map.put("url", res[9]);
            map.put("witten_by", res[10]);

            wishLists.add(map);
        }
        return wishLists;
    }
}
