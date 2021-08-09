package com.sogyeong.cbcb.mypage.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Service
@AllArgsConstructor
public class MyPageService {
    @PersistenceContext
    private EntityManager em;
}
