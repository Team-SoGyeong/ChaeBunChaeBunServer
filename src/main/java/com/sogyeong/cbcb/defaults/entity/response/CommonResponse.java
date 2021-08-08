package com.sogyeong.cbcb.defaults.entity.response;

import com.sogyeong.cbcb.defaults.entity.response.BasicResponse;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommonResponse<T,String> extends BasicResponse {
    private int code=200;
    private Boolean success = true;
    private String message = (String) "성공";
    private T data;

    public CommonResponse(T data, String message) {
        this.message=message ;
        this.data = data;
    }
    public CommonResponse(String message) {
        this.message=message ;
    }
    public CommonResponse() {
        this.data= (T) "ok";
    }
}
