package com.moogsan.moongsan_backend.domain.groupbuy.dto.command.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.URL;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateGroupBuyRequest {

    @NotBlank(message="제목은 필수 입력 항목입니다.")
    @Size(min = 1, max = 30, message = "제목은 1자 이상, 30자 이하로 입력해주세요.")
    private String title;

    @NotBlank(message="상품명은 필수 입력 항목입니다.")
    @Size(min = 1, max = 30, message = "상품명은 1자 이상, 30자 이하로 입력해주세요.")
    private String name;

    @Size(min = 1, max = 2000, message = "URL은 1자 이상, 2000자 이하로 입력해주세요.")
    @URL(message="URL 형식이 올바르지 않습니다.")
    private String url;

    @NotBlank(message="상품 가격은 필수 입력 항목입니다.")
    @Size(min = 1, message = "상품 가격은 1 이상이어야 합니다.")
    private int price;

    @NotBlank(message="상품 전체 수량은 필수 입력 항목입니다.")
    @Size(min = 1, message = "상품 전체 수량은 1 이상이어야 합니다.")
    private int totalAmount;

    @NotBlank(message="상품 주문 단위는 필수 입력 항목입니다.")
    @Size(min = 1, message = "상품 주문 단위는 1 이상이어야 합니다.")
    private int unitAmount;

    @NotBlank(message="상품 상세 설명은 필수 입력 항목입니다.")
    @Size(min = 2, max = 2000, message = "상품 설명은 2자 이상, 1000자 이하로 입력해주세요.")
    private String description;

    @NotBlank(message="마감 일자는 필수 입력 항목입니다.")
    @Future(message="마감일자는 현재 시간 이후여야 합니다.")
    @JsonFormat(pattern="yyyy-MM-dd'T'HH:mm")
    private String dueDate;

    @NotBlank(message="거래 장소는 필수 입력 항목입니다.")
    private String location;

    @NotBlank(message="픽업 일자는 필수 입력 항목입니다.")
    @Future(message="픽업 일자는 현재 시간 이후여야 합니다.")
    @JsonFormat(pattern="yyyy-MM-dd'T'HH:mm")
    private String pickupDate;

    @NotEmpty(message="상품 이미지는 최소 1개 이상 등록해야 합니다.")
    private List<String> imageUrls;

}
