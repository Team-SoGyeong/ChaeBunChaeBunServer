package com.sogyeong.cbcb.mypage.service;

import com.sogyeong.cbcb.board.entity.Comment;
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
            map.put("wish_id", res[2]);
            map.put("post_id", res[3]);
            map.put("title", res[4]);
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

    public List getScrapList(long userId, long platformId, long stateId) {
        // platformId = 1
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
                        "where  bw.member = :user and " +
                        "TIMESTAMPDIFF(day,bp.reg_date,now()) < 7 and " +
                        "case when :state=1 then " +
                        "   bp.status = 1 " +
                        "   when :state=0 then " +
                        "   bp.status = 0 " +
                        "   else " +
                        "   1=1 " +
                        "end "+
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
            map.put("buy_date",res[5]);
            map.put("members", res[6]);
            map.put("per_price", res[7].toString()+'원');
            map.put("isAuth", res[8]);
            map.put("url", res[9]);
            map.put("written_by", res[10]);

            wishLists.add(map);
        }
        return wishLists;
    }


}
