package com.sogyeong.cbcb.community.repository;

import com.sogyeong.cbcb.community.entity.COpinion;
import org.springframework.data.jpa.repository.JpaRepository;

public interface COpinionRepository extends JpaRepository<COpinion,Long> , COpinionRepositoryCustom {
}
