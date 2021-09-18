package com.sogyeong.cbcb.defaults.entity.response;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import com.sogyeong.cbcb.defaults.entity.response.ResultMessage;

@Getter
@Setter
public class ErrorResponse extends BasicResponse {
    private String code;
    private Boolean success = false;
    private String message = ResultMessage.DEFAULT_MSG.getVal();

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



