package com.sogyeong.cbcb.board.model.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ResponseCategory {

        private int code;
        private Boolean success;
        private String message;
        private List comments;
}
