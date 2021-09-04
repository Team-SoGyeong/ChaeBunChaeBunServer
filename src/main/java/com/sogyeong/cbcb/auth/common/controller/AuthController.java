package com.sogyeong.cbcb.auth.common.controller;

import com.sogyeong.cbcb.auth.common.AuthType;
import com.sogyeong.cbcb.auth.common.model.response.AuthResponse;
import com.sogyeong.cbcb.auth.common.model.response.SignUpResponse;
import com.sogyeong.cbcb.auth.common.service.AuthService;
import com.sogyeong.cbcb.auth.kakao.service.KaKaoAuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private AuthService authService;

    @Autowired
    private KaKaoAuthService kaKaoAuthService;

    @PostMapping("/signup")
    public SignUpResponse signup(@RequestHeader String token, @RequestParam AuthType type) {
        chooseAuthService(type);
        return authService.signup(token);
    }

    @GetMapping("/signin")
    public AuthResponse signin(@RequestHeader String token, @RequestParam AuthType type ) {
        chooseAuthService(type);
        return authService.signin(token);
    }

    @GetMapping("/refresh")
    public AuthResponse refresh(@AuthenticationPrincipal Long userNo, @RequestParam AuthType type) {
        chooseAuthService(type);
        return authService.refresh(userNo);
    }

    private void chooseAuthService(AuthType type) {
        if(type == AuthType.Kakao) {
            authService = kaKaoAuthService;
        }
    }
}

