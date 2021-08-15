package com.sogyeong.cbcb.board.service;


import com.sogyeong.cbcb.board.model.dto.ReportDTO;
import com.sogyeong.cbcb.board.repository.PostsRepository;
import com.sogyeong.cbcb.board.repository.ReportRepository;
import com.sogyeong.cbcb.defaults.entity.Opinion;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Service
@AllArgsConstructor
public class ReportService {

    private PostsRepository postsRepository;
    private ReportRepository rRepository;

    @PersistenceContext
    EntityManager em;

    @Transactional(readOnly = false)
    public Boolean storeReport(ReportDTO rDTO) {
        int lastSeq= rRepository.getLastSeq(rDTO.getTyp());
        Opinion oResult = rRepository.save(rDTO.toEntity());
        if(oResult.getSeq()>lastSeq)
            return true;
        else return false;
    }
}
