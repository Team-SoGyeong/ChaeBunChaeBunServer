package com.sogyeong.cbcb.community.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotNull;

@NoArgsConstructor
@Getter
@Setter
@ToString
public class CPostRequest {
    @NotNull
    private String content;
    private CAlbum imgs;
}
