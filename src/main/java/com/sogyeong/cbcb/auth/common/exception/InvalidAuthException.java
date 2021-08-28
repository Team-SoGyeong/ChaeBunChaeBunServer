package com.sogyeong.cbcb.auth.common.exception;

import org.springframework.http.HttpStatus;

public class InvalidAuthException extends BaseException{
    public InvalidAuthException(String message) {
        super(message, HttpStatus.UNAUTHORIZED);
    }
}
