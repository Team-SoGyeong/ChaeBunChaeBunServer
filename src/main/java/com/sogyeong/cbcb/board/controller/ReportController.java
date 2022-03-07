package com.sogyeong.cbcb.board.controller;

import com.sogyeong.cbcb.board.model.dto.ReportDTO;
import com.sogyeong.cbcb.board.model.vo.ReportVO;
import com.sogyeong.cbcb.board.repository.PostsRepository;
import com.sogyeong.cbcb.board.service.ReportService;
import com.sogyeong.cbcb.defaults.entity.response.BasicResponse;
import com.sogyeong.cbcb.defaults.entity.response.CommonResponse;
import com.sogyeong.cbcb.defaults.entity.response.ErrorResponse;
import com.sogyeong.cbcb.defaults.entity.response.ResultMessage;
import com.sogyeong.cbcb.defaults.repository.ProductsRepository;
import com.sogyeong.cbcb.mypage.repository.UserInfoRepository;
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

    UserInfoRepository userInfoRepository;
    ProductsRepository productsRepository;
    PostsRepository postsRepository;

    private ReportService reportService;

    @PersistenceContext
    private EntityManager em;

    // 게시글 신고
    @PostMapping("/posts/report")
    public ResponseEntity<? extends BasicResponse> saveReport(@RequestBody ReportVO RVO) {

        boolean isUser = userInfoRepository.existsById(RVO.getAuthor_id());
        if (!isUser) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ErrorResponse(ResultMessage.UNDEFINED_USER.getVal()));
        }
        else if (!postsRepository.existsById(RVO.getPost_id())) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ErrorResponse(ResultMessage.UNDEFINED_POST.getVal()));
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
                return  ResponseEntity.ok().body( new CommonResponse(ResultMessage.REPORT_OK.getEditVal("게시글")));
            else
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(new ErrorResponse(ResultMessage.REPORT_FAILED.getEditVal("게시글")));
        }
    }

    @PostMapping("/comments/report")
    public ResponseEntity<? extends BasicResponse> saveCmtReport(@RequestBody ReportVO RVO) {

        boolean isUser = userInfoRepository.existsById(RVO.getAuthor_id());
        if (!isUser) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ErrorResponse(ResultMessage.UNDEFINED_USER.getVal()));
        }
        else if (!postsRepository.existsById(RVO.getPost_id())) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ErrorResponse(ResultMessage.UNDEFINED_POST.getVal()));
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
                return  ResponseEntity.ok().body( new CommonResponse(ResultMessage.REPORT_OK.getEditVal("댓글")));
            else
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(new ErrorResponse(ResultMessage.REPORT_FAILED.getEditVal("댓글")));
        }
    }

    @PostMapping("/posts/blind")
    public ResponseEntity<? extends BasicResponse> saveBlind(@RequestBody ReportVO RVO) {

        boolean isUser = userInfoRepository.existsById(RVO.getAuthor_id());
        if (!isUser) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ErrorResponse(ResultMessage.UNDEFINED_USER.getVal()));
        }
        else if (!postsRepository.existsById(RVO.getPost_id())) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ErrorResponse(ResultMessage.UNDEFINED_POST.getVal()));
        }
        else{
            ReportDTO report = new ReportDTO();

            report.setTyp("blind");
            report.setPost_id(RVO.getPost_id());
            report.setCmt_id(RVO.getCmt_id());
            report.setAuthor_id(RVO.getAuthor_id());
            report.setReason_num(RVO.getReason_num());
            report.setReason(RVO.getReason());

            Boolean isSave = reportService.storeReport(report);
            if(isSave)
                return  ResponseEntity.ok().body( new CommonResponse(ResultMessage.BLIND_OK.getVal()));
            else
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(new ErrorResponse(ResultMessage.BLIND_FAILED.getVal()));
        }
    }
}
