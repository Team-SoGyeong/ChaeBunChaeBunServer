package com.sogyeong.cbcb.defaults.entity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommonResponse<T,String> extends BasicResponse{
    private int code=200;
    private Boolean success = true;
    private String message = (String) "성공";
    private T data;

    public CommonResponse(T data, String message) {
        this.message=message ;
        this.data = data;
    }

    public CommonResponse() {
        this.data= (T) "ok";
    }
}
