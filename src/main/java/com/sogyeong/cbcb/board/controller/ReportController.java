package com.sogyeong.cbcb.board.controller;

import com.sogyeong.cbcb.board.model.dto.ReportDTO;
import com.sogyeong.cbcb.board.model.vo.ReportVO;
import com.sogyeong.cbcb.board.repository.PostsRepository;
import com.sogyeong.cbcb.board.service.ReportService;
import com.sogyeong.cbcb.defaults.entity.response.BasicResponse;
import com.sogyeong.cbcb.defaults.entity.response.CommonResponse;
import com.sogyeong.cbcb.defaults.entity.response.ErrorResponse;
import com.sogyeong.cbcb.defaults.repository.ProductsRepository;
import com.sogyeong.cbcb.mypage.repository.UserInfoReposiorty;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@RestController
@AllArgsConstructor
public class ReportController {

    UserInfoReposiorty userInfoReposiorty;
    ProductsRepository productsRepository;
    PostsRepository postsRepository;

    private ReportService reportService;

    @PersistenceContext
    private EntityManager em;

    @PostMapping("/posts/report")
    public ResponseEntity<? extends BasicResponse> saveReport(@RequestBody ReportVO RVO) {

        boolean isUser = userInfoReposiorty.existsById(RVO.getAuthor_id());
        if (!isUser) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ErrorResponse("존재하지 않는 사용자 입니다. 다시 시도 해주세요"));
        }
        else if (!postsRepository.existsById(RVO.getPost_id())) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ErrorResponse("존재하지 않는 게시글 입니다. 다시 시도 해주세요"));
        }
        else{
            ReportDTO report = new ReportDTO();

            report.setTyp("report");
            report.setPost_id(RVO.getPost_id());
            report.setCmt_id(RVO.getCmt_id());
            report.setAuthor_id(RVO.getAuthor_id());
            report.setReason_num(RVO.getReason_num());
            report.setReason(RVO.getReason());

            Boolean isSave = reportService.storeReport(report);
            if(isSave)
                return  ResponseEntity.ok().body( new CommonResponse("게시긓 신고 하기 성공"));
            else
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(new ErrorResponse("게시글 신고 하기 실패"));
        }
    }

    @PostMapping("/comments/report")
    public ResponseEntity<? extends BasicResponse> saveCmtReport(@RequestBody ReportVO RVO) {

        boolean isUser = userInfoReposiorty.existsById(RVO.getAuthor_id());
        if (!isUser) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ErrorResponse("존재하지 않는 사용자 입니다. 다시 시도 해주세요"));
        }
        else if (!postsRepository.existsById(RVO.getPost_id())) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ErrorResponse("존재하지 않는 게시글 입니다. 다시 시도 해주세요"));
        }
        else{
            ReportDTO report = new ReportDTO();

            report.setTyp("report_cmt");
            report.setPost_id(RVO.getPost_id());
            report.setCmt_id(RVO.getCmt_id());
            report.setAuthor_id(RVO.getAuthor_id());
            report.setReason_num(RVO.getReason_num());
            report.setReason(RVO.getReason());

            Boolean isSave = reportService.storeReport(report);
            if(isSave)
                return  ResponseEntity.ok().body( new CommonResponse("댓글 신고 하기 성공"));
            else
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(new ErrorResponse("댓글 신고 하기 실패"));
        }
    }
}
