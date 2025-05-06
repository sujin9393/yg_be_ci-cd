package com.moogsan.moongsan_backend.domain.groupbuy.dto.command.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.validator.constraints.URL;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DescriptionGenerationRequest {

    @NotBlank(message = "URL은 필수 입력 항목입니다.")
    @Size(min = 1, max = 2000, message = "URL은 1자 이상, 2000자 이하로 입력해주세요.")
    @URL(message = "URL 형식이 올바르지 않습니다.")
    private String url;
}
