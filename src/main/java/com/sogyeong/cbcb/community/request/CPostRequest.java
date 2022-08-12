package com.sogyeong.cbcb.community.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@NoArgsConstructor
@Getter
@Setter
@ToString
public class CPostRequest {
    @NotNull
    private String content;
    @Size(max = 5)
    private List<MultipartFile> imgs;
}
