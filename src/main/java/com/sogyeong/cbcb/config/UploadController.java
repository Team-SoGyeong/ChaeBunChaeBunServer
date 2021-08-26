package com.sogyeong.cbcb.config;

import com.sogyeong.cbcb.defaults.entity.response.BasicResponse;
import com.sogyeong.cbcb.defaults.entity.response.CommonResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.List;

@RequiredArgsConstructor
@RestController
public class UploadController {

    private final S3Uploader s3Uploader;

    @PostMapping("/image/upload/{user}")
    public ResponseEntity<? extends BasicResponse> upload(@RequestPart("data") MultipartFile file, @PathVariable("user") long user) throws IOException {
        return ResponseEntity.ok().body( new CommonResponse(s3Uploader.upload(file, "static/"+user),"이미지변환완료")); // S3 bucket의 static/ 폴더를 지정한 것.
    }

}