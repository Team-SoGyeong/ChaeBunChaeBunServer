package com.sogyeong.cbcb.community.request;

import com.sogyeong.cbcb.community.entity.CImages;
import com.sogyeong.cbcb.community.entity.CPosts;
import com.sogyeong.cbcb.defaults.entity.Address;
import com.sogyeong.cbcb.mypage.entity.UserInfo;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@NoArgsConstructor
@Getter
public class CPostRequest {
    @NotNull
    private String content;
    private String img1;
    private String img2;
    private String img3;
    private String img4;
    private String img5;

    public CPosts newPost(UserInfo userInfo){
        return new CPosts().builder()
                .user(userInfo)
                .address(Address.builder().seq(userInfo.getAddr()).build())
                .contents(content)
                .cImages(new CImages(img1, img2, img3, img4, img5))
                .create_date(LocalDateTime.now())
                .update_date(LocalDateTime.now())
                .build();
    }

}
