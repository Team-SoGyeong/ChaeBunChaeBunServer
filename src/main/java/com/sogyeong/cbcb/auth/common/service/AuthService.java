package com.sogyeong.cbcb.auth.common.service;

import com.sogyeong.cbcb.auth.common.model.response.AuthResponse;
import com.sogyeong.cbcb.auth.common.model.response.SignUpResponse;

public interface AuthService {
    public SignUpResponse signup(String accessToken);
    public AuthResponse signin(String accessToken);
    public AuthResponse refresh(Long userNo);
    public void validateDuplicateUser(Long userNo);

}
