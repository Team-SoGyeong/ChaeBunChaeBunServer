package com.sogyeong.cbcb.defaults.entity.response;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
public class ErrorResponse extends BasicResponse {
    private String code;
    private Boolean success = false;
    private String message = (String) "서버 문제 문의 바람";

    public ErrorResponse(String errorMessage) {
        this.code = "404";
        this.message = errorMessage;
    }
    public ErrorResponse(String errorMessage, HttpStatus httpStatus) {
        this.code = httpStatus.toString().split(" ")[0];
        this.message = errorMessage;

    }

    public ErrorResponse(String errorMessage, String errorCode) {
        this.code = errorCode;
        this.message = errorMessage;
    }
}



