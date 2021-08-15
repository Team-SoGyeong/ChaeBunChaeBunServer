package com.sogyeong.cbcb.board.service;

import com.sogyeong.cbcb.board.entity.Album;
import com.sogyeong.cbcb.board.entity.Comment;
import com.sogyeong.cbcb.board.entity.Posts;
import com.sogyeong.cbcb.board.entity.Wish;
import com.sogyeong.cbcb.board.model.AlbumDTO;
import com.sogyeong.cbcb.board.model.AlbumVO;
import com.sogyeong.cbcb.board.model.CommentDTO;
import com.sogyeong.cbcb.board.model.PostDTO;
import com.sogyeong.cbcb.board.model.response.*;
import com.sogyeong.cbcb.board.repository.AlbumRepository;
import com.sogyeong.cbcb.board.repository.CommentRepository;
import com.sogyeong.cbcb.board.repository.PostsRepository;
import com.sogyeong.cbcb.board.repository.WishRepository;
import com.sogyeong.cbcb.mypage.repository.UserInfoReposiorty;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    private CommentRepository commentRepository;
    private WishRepository wishRepository;
    private UserInfoReposiorty userInfoReposiorty;
    private PostsRepository postsRepository;

    @PersistenceContext
    EntityManager em;

    @Transactional(readOnly = true)
    public List getSubCategoryList(long category_id,long user_id,long addr_seq) {

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
                                "bp.headcount, " +
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
                                "(select " +
                                " case when count(seq)>0 then true" +
                                " else false " +
                                " end " +
                                " from board_wish " +
                                " where board_wish.member = :user and post_id = bp.seq " +
                                ") as isMyWish," +
                                "date_format(bp.reg_date,'%m/%d') as dates, " +
                                "TIMESTAMPDIFF(day,bp.reg_date,now()) as diff "+
                                "from board_posts bp " +
                                "join default_products dp on bp.prod_id = dp.seq " +
                                "join board_album ba on bp.seq = ba.post_id " +
                                "join user_info ui on bp.author_id = ui.info_id " +
                                "where ui.address = :addrId and " +
                                "case when :categoryId > 10 then bp.prod_id >=11 else bp.prod_id = :categoryId end " +
                                "and bp.status = 0 " + // 소분이 완료되지않는경우만 나오게 하기
                                "and TIMESTAMPDIFF(day,bp.reg_date,now()) < 7 " +
                                "order by diff desc , bp.prod_id ")
                .setParameter("categoryId", category_id)
                .setParameter("user", user_id)
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
            map.put("headcounts", res[7].toString()+'명');
            map.put("per_price", res[8].toString()+'원');
            map.put("wish_cnts", res[9]);
            map.put("comment_cnts", res[10]);
            map.put("isAuth", res[11]);
            map.put("isMyWish", res[12]);
            map.put("imgs", album);
            map.put("witten_by", res[13]);

            subList.add(map);
        }
        return subList;

    }

    @Transactional(readOnly = true)
    public List getSubCategory(long category_id,long user_id,long postid) {

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
                        "(select " +
                        " case when count(seq)>0 then true" +
                        " else false " +
                        " end " +
                        " from board_wish " +
                        " where board_wish.member = :user and post_id = bp.seq " +
                        ") as isMyWish," +
                        "date_format(bp.reg_date,'%m/%d') as dates, " +
                        "TIMESTAMPDIFF(day,bp.reg_date,now()) as diff," +
                        "dp.name "+
                        "from board_posts bp " +
                        "join default_products dp on bp.prod_id = dp.seq " +
                        "join board_album ba on bp.seq = ba.post_id " +
                        "join user_info ui on bp.author_id = ui.info_id " +
                        "where bp.seq = :post and bp.prod_id = :categoryId " +
                        "and bp.status = 0 and bp.isDonated = 0 " + // 소분이 완료되지않는경우만 나오게 하기
                        "and TIMESTAMPDIFF(day,bp.reg_date,now()) < 7 " +
                        "order by diff desc , bp.prod_id ")
                .setParameter("categoryId", category_id)
                .setParameter("user", user_id)
                .setParameter("post", postid)
                .getResultList();

            List subDetail = category_id<11? new ArrayList<ResponseSubDetail1>() : new ArrayList<ResponseSubDetail2>();

            LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
            LinkedHashMap<String, Object> map2 = new LinkedHashMap<String, Object>();
            Optional<Album> album = albumRepository.findById(postid);

            System.out.println(category_id+":"+result.size());

        if(category_id>11) {
            for (Object o: result) {
                Object[] res = (Object[]) o;
                map.put("post_id", res[0]);
                map.put("detail_name", res[15]);
                map.put("user_id", res[1]);
                map.put("nickname", res[2]);
                map.put("profile", res[3]);
                map.put("title", res[4]);
                map.put("contents", res[5]);
                map.put("buy_date", res[6]);
                map.put("headcounts", res[7].toString() + '명');
                map.put("per_price", res[8].toString() + '원');
                map.put("wish_cnts", res[9]);
                map.put("comment_cnts", res[10]);
                map.put("isAuth", res[11]);
                map.put("isMyWish", res[12]);
                map.put("imgs", album);
                map.put("witten_by", res[13]);

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
                map.put("per_price", res[8].toString() + '원');
                map.put("wish_cnts", res[9]);
                map.put("comment_cnts", res[10]);
                map.put("isAuth", res[11]);
                map.put("isMyWish", res[12]);
                map.put("imgs", album);
                map.put("witten_by", res[13]);

                subDetail.add(map);
            }
        }
        return subDetail;

    }

    @Transactional(readOnly = false)
    public long savePosts(PostDTO postDTO, AlbumVO imgs) {
        AlbumDTO aDTO =new AlbumDTO();

        int lastSeq= postsRepository.getLastSeq();
        Posts posts = postsRepository.save(postDTO.toEntity());
        if(posts.getSeq()>lastSeq) {

            aDTO.setPost_id(posts.getSeq());
            aDTO.setBill1(imgs.getBill1());
            aDTO.setBill2(imgs.getBill2());
            aDTO.setImg1(imgs.getImg1());
            aDTO.setImg2(imgs.getImg2());
            aDTO.setImg3(imgs.getImg3());
            aDTO.setImg4(imgs.getImg4());
            aDTO.setImg5(imgs.getImg5());

            albumRepository.save(aDTO.toEntity());
            return posts.getSeq();
        }
        else return -1;
    }

    @Transactional(readOnly = false)
    public Boolean saveComments(CommentDTO comment) {
        int lastSeq= commentRepository.getLastSeq();
        Comment commentResult = commentRepository.save(comment.toEntity());
        if(commentResult.getSeq()>lastSeq)
            return true;
        else return false;
    }

    @Transactional(readOnly = true)
    public Boolean deleteComments(long cmtId, long userId) {
        Comment commentResult = commentRepository.getOne(cmtId);

        if (userId == commentResult.getMember()) {
            commentRepository.deleteById(cmtId);
            return true;
        }
        return false;
    }

    @Transactional(readOnly = false)
    public List getComments(long postId) {

        List resultList =  em.createNativeQuery(
                        "select" +
                                "        ui.info_id as userId, " +
                                "        ui.nickname, " +
                                "        ui.profile," +
                                "        bc.contents," +
                                "        date_format(bc.reg_date,'%m/%d %H:%m') as dates " +
                                "from board_posts bp\n" +
                                "left join board_comment bc on bp.seq = bc.post_id " +
                                "join user_info ui on bc.member = ui.info_id " +
                                "where bp.seq = :post_id " +
                                "order by dates; ")
                .setParameter("post_id", postId)
                .getResultList();

        List cmtList = new ArrayList<ResponseCmtList>();
        for (Object o: resultList) {
            Object[] res = (Object[]) o;

            LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();

            map.put("user_id", res[0]);
            map.put("nickname", res[1]);
            map.put("profile", res[2]);
            map.put("comments", res[3]);
            map.put("witten_by", res[4]);

            cmtList.add(map);

        }
        return cmtList;
    }

    @Transactional(readOnly = false)
    public Boolean storeWish(WishDTO wDTO) {
        int lastSeq= wishRepository.getLastSeq();
        Wish wResult = wishRepository.save(wDTO.toEntity());
        if(wResult.getSeq()>lastSeq)
            return true;
        else return false;
    }

    @Transactional(readOnly = true)
    public Boolean deleteWish(long postId, long userId) {
        List<Wish> wishResult = wishRepository.getWish(postId,userId);

        if (userId == wishResult.get(0).getMember()) {
            wishRepository.deleteByIdAndMember(postId,userId);
            return true;
        }
        return false;
    }
}
