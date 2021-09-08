package com.sogyeong.cbcb.mypage.service;

import com.sogyeong.cbcb.board.entity.Album;
import com.sogyeong.cbcb.board.entity.Comment;
import com.sogyeong.cbcb.board.model.response.ResponseSubDetail1;
import com.sogyeong.cbcb.board.model.response.ResponseSubDetail2;
import com.sogyeong.cbcb.board.repository.AlbumRepository;
import com.sogyeong.cbcb.mypage.entity.UserInfo;
import com.sogyeong.cbcb.mypage.entity.UserLogin;
import com.sogyeong.cbcb.mypage.model.response.ResponseMyCommentList;
import com.sogyeong.cbcb.mypage.model.response.ResponseMyWishList;
import com.sogyeong.cbcb.mypage.repository.UserInfoRepository;
import com.sogyeong.cbcb.mypage.repository.UserLoginRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class MyPageService {
    @PersistenceContext
    private EntityManager em;

    UserInfoRepository userInfoRepository;
    UserLoginRepository userLoginRepository;
    private AlbumRepository albumRepository;

    @Transactional
    public Boolean updateProfile(long userId, String image, String nickname){
        Optional<UserInfo> info = userInfoRepository.findById(userId);
        info.ifPresent(profileInfo ->{
            profileInfo.setUrl(image);
            profileInfo.setNickname(nickname);
            userInfoRepository.save(profileInfo);
        });
        return info.stream().count() > 0 ? true : false;
    }

    @Transactional
    public Boolean updateUserQuitDate(long userId){
        Optional<UserLogin> info = userLoginRepository.findById(userId);
        info.ifPresent(loginInfo ->{
            loginInfo.setQuitDate(LocalDateTime.now());
            userLoginRepository.save(loginInfo);
        });
        return info.stream().count() > 0 ? true : false;
    }

    public List getMyCommentList(long userId, long platformId, long stateId) {
        // platformId = 1
        List resultList =  em.createNativeQuery(
                "select distinct "+
                        "dp.seq as category_id, " +
                        "dp.name, bp.seq as postId, " +
                        "bp.title, bp.author_id, " +
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
                        //"left join default_opinion d_o on bp.seq = d_o.post_id " +
                        "join board_comment bc on bc.post_id = bp.seq " +
                        "join default_products dp on bp.prod_id = dp.seq " +
                        "join board_album ba on bp.seq = ba.post_id " +
                        "join user_info ui on bp.author_id = ui.info_id " +
                        "where  bc.member = :user and " +
                        "TIMESTAMPDIFF(day,bp.reg_date,now()) < 7 and " +
                        "case when :state=1 then " +
                        "   bp.status = 1 " +
                        "   when :state=0 then " +
                        "   bp.status = 0 " +
                        "   else " +
                        "   1=1 " +
                        "end "+
                        //"and ( d_o.post_id <> bp.seq and d_o.types <>'blind' and d_o.author_id <> :user) " +
                        "order by diff desc , dp.seq ")
                .setParameter("user", userId)
                .setParameter("state", stateId)
                .getResultList();

        List commentLists = new ArrayList<ResponseMyCommentList>();
        for (Object o: resultList){
            Object[] res = (Object[]) o;

            LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();

            map.put("category_id", res[0]);
            map.put("category_name", res[1]);
            map.put("post_id", res[2]);
            map.put("title", res[3]);
            map.put("author_id", res[4]);
            map.put("buy_date",res[5]);
            map.put("members", res[6]);
            map.put("per_price", res[7].toString()+'원');
            map.put("isAuth", res[8]);
            map.put("url", res[9]);
            map.put("written_by", res[10]);

            commentLists.add(map);
        }
        return commentLists;
    }

    //HomeListService의 getMyLikeList와 동일..!!
    public List getScrapList(long userId, long platformId, long stateId) {
        // platformId = 1
        List resultList =  em.createNativeQuery(
                "select "+
                        "dp.seq as category_id, " +
                        "dp.name, bw.seq as wish_id, " +
                        "bp.seq as post_id, " +
                        "bp.title, bp.author_id, " +
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
                        //"left join default_opinion do on bp.seq = do.post_id " +
                        "join board_wish bw on  bw.post_id = bp.seq " +
                        "join default_products dp on bp.prod_id = dp.seq " +
                        "join board_album ba on bp.seq = ba.post_id " +
                        "join user_info ui on bp.author_id = ui.info_id " +
                        "where bw.member = :user and " +
                        "TIMESTAMPDIFF(day,bp.reg_date,now()) < 7 and " +
                        "case when :state=1 then " +
                        "   bp.status = 1 " +
                        "   when :state=0 then " +
                        "   bp.status = 0 " +
                        "   else " +
                        "   1=1 " +
                        "end "+
                        //"and ( do.post_id <> bp.seq and do.types <>'blind' and do.author_id <> :user) " +
                        "order by diff desc , dp.seq ")
                .setParameter("user", userId)
                .setParameter("state", stateId)
                .getResultList();

        List wishLists = new ArrayList<ResponseMyWishList>();
        for (Object o: resultList){
            Object[] res = (Object[]) o;

            LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();

            map.put("category_id", res[0]);
            map.put("category_name", res[1]);
            map.put("wish_id", res[2]);
            map.put("post_id", res[3]);
            map.put("title", res[4]);
            map.put("author_id", res[5]);
            map.put("buy_date",res[6]);
            map.put("members", res[7]);
            map.put("per_price", res[8].toString()+'원');
            map.put("isAuth", res[9]);
            map.put("url", res[10]);
            map.put("written_by", res[11]);

            wishLists.add(map);
        }
        return wishLists;
    }

    @Transactional(readOnly = true)
    public List getMySubCategory(long category_id,long user_id,long postid) {

        List result =  em.createNativeQuery(
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
                        "bp.headcount , " +
                        "concat(bp.amount,bp.unit) as amount, " +
                        "FORMAT(bp.total_price,0) , " +
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
                        "bp.status, " +
                        "(select " +
                        " case when count(seq)>0 then true" +
                        " else false " +
                        " end " +
                        " from board_wish " +
                        " where board_wish.member = :user and post_id = bp.seq " +
                        ") as isMyWish," +
                        "date_format(bp.reg_date,'%m/%d') as dates, " +
                        "TIMESTAMPDIFF(day,bp.reg_date,now()) as diff," +
                        "dp.name ," +
                        "bp.contact "+
                        "from board_posts bp " +
                        "join default_products dp on bp.prod_id = dp.seq " +
                        "join board_album ba on bp.seq = ba.post_id " +
                        "join user_info ui on bp.author_id = ui.info_id " +
                        "where bp.seq = :post and bp.prod_id = :categoryId " +
                        "order by diff desc , bp.prod_id ")
                .setParameter("categoryId", category_id)
                .setParameter("user", user_id)
                .setParameter("post", postid)
                .getResultList();

        List subDetail =
                category_id<11?
                        new ArrayList<ResponseSubDetail1>() :
                        new ArrayList<ResponseSubDetail2>();

        LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
        LinkedHashMap<String, Object> map2 = new LinkedHashMap<String, Object>();
        Optional<Album> album = albumRepository.findById(postid);

        System.out.println(category_id+":"+result.size());

        if(category_id>11) {
            for (Object o: result) {
                Object[] res = (Object[]) o;
                map.put("post_id", res[0]);
                map.put("detail_name", res[17]);
                map.put("user_id", res[1]);
                map.put("nickname", res[2]);
                map.put("profile", res[3]);
                map.put("title", res[4]);
                map.put("contents", res[5]);
                map.put("buy_date", res[6]);
                map.put("headcounts", res[7].toString() + '명');
                map.put("amount", res[8]);
                map.put("total_price", res[9].toString() + '원');
                map.put("per_price", res[10].toString() + '원');
                map.put("wish_cnts", res[11]);
                map.put("comment_cnts", res[12]);
                map.put("isAuth", res[13]);
                map.put("status", res[14]);
                map.put("isMyWish", res[15]);
                map.put("contact", res[19]);
                map.put("imgs", album);
                map.put("written_by", res[16]);

                subDetail.add(map);
            }
        }
        else{
            for (Object o: result) {
                Object[] res = (Object[]) o;
                map.put("post_id", res[0]);
                map.put("user_id", res[1]);
                map.put("nickname", res[2]);
                map.put("profile", res[3]);
                map.put("title", res[4]);
                map.put("contents", res[5]);
                map.put("buy_date", res[6]);
                map.put("headcounts", res[7].toString() + '명');
                map.put("amount", res[8]);
                map.put("total_price", res[9].toString() + '원');
                map.put("per_price", res[10].toString() + '원');
                map.put("wish_cnts", res[11]);
                map.put("comment_cnts", res[12]);
                map.put("isAuth", res[13]);
                map.put("status", res[14]);
                map.put("isMyWish", res[15]);
                map.put("contact", res[19]);
                map.put("imgs", album);
                map.put("written_by", res[16]);
                subDetail.add(map);
            }
        }
        return subDetail;
    }


}
