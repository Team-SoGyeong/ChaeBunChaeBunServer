package com.sogyeong.cbcb.board.controller;


import com.sogyeong.cbcb.board.service.HomeListService;
import com.sogyeong.cbcb.defaults.entity.response.CommonResponse;
import com.sogyeong.cbcb.defaults.entity.Products;
import com.sogyeong.cbcb.defaults.entity.response.ErrorResponse;
import com.sogyeong.cbcb.defaults.repository.AddressRepository;
import com.sogyeong.cbcb.defaults.repository.ProductsRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import com.sogyeong.cbcb.defaults.entity.response.BasicResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

@RestController
@AllArgsConstructor
public class HomeController {

    ProductsRepository productsRepository;
    AddressRepository addressRepository;

    private HomeListService homeListService;

    @PersistenceContext
    private EntityManager em;

    //카테고리리스트
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
        }
        return ResponseEntity.ok().body( new CommonResponse(category_list,"카테고리 리스트 출력 성공"));
    }

    //신규채분리스트
    @GetMapping("/home/new/{addr_seq}")
    public ResponseEntity<? extends BasicResponse> getNewList(@PathVariable("addr_seq") long addr_seq) {
        boolean isAddr = addressRepository.existsById(addr_seq);
        if(!isAddr){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("입력된 주소 일련번호는 존재하지 않습니다. "));
        }
        else{
            List newList = homeListService.getNewList(addr_seq);
            if(newList.size()>0)
                return ResponseEntity.ok().body( new CommonResponse(newList,"신규 채분 리스트 출력 성공"));
            else
                return ResponseEntity.ok().body( new CommonResponse(newList,"아직 최신 채분이 없어요. 직접 채분을 시작해 신선함을 나눠보세요!"));
        }
    }
}
