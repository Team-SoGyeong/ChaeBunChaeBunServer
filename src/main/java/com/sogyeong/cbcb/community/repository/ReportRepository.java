package com.sogyeong.cbcb.community.repository;


import com.sogyeong.cbcb.community.entity.Opinion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ReportRepository extends JpaRepository<Opinion,Long> {

    @Query(value = "select case when count(seq) = 0 then 0 else seq end as lastSeq from default_opinion where types  like concat('%', :typ, '%') order by seq desc ", nativeQuery = true)
    int getLastSeq(@Param(value="typ") String typ); // 신고하기 삽입전 최신의 신고글 일련 번호를 얻기 위해 필요
}
