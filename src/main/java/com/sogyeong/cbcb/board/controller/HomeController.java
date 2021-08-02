package com.sogyeong.cbcb.board.controller;


import com.sogyeong.cbcb.defaults.entity.CommonResponse;
import com.sogyeong.cbcb.defaults.entity.Products;
import com.sogyeong.cbcb.defaults.repository.ProductsRepository;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.ResponseEntity;
import com.sogyeong.cbcb.defaults.entity.BasicResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

@RestController
@AllArgsConstructor
public class HomeController {

    ProductsRepository productsRepository;

    @GetMapping("/home/category")
    public ResponseEntity<? extends BasicResponse> getCategory() {
        List<Products> list = productsRepository.findAll();
        System.out.println("username=" + list.size());


        List category_list = new ArrayList<Object>(); // 매핑 한거 받는 타입입

        for (Products li : list) {
            LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
            map.put("name",li.getName());
            map.put("category_id",li.getSeq());
            category_list.add(map);

            String name = li.getName();
            Long age = li.getSeq();
            System.out.println("username=" + name);
            System.out.println("age=" + age);
        }
        return ResponseEntity.ok().body( new CommonResponse(category_list,"카테고리 리스트 출력 성공"));
    }
}
