package com.moogsan.moongsan_backend.domain.groupbuy.entity;

import com.moogsan.moongsan_backend.domain.BaseEntity;
import com.moogsan.moongsan_backend.domain.groupbuy.dto.command.request.CreateGroupBuyRequest;
import com.moogsan.moongsan_backend.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name="group_buy")
public class GroupBuy extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 40)
    private String title;

    @Column(nullable = false, length = 40)
    private String name;

    private String url;

    @Column(nullable = false)
    private int price;

    @Column(nullable = false)
    private int unitPrice;

    @Column(nullable = false)
    private int totalAmount;

    @Column(nullable = false)
    private int leftAmount;

    @Column(nullable = false)
    private int unitAmount;

    @Column(nullable = false, length = 500)
    private String description;

    @Builder.Default
    @Column(nullable = false)
    private boolean dueSoon = false;

    @Column(length = 20)
    private String badge;

    @Column(nullable = false)
    private LocalDateTime dueDate;

    @Column(nullable = false)
    private String location;

    @Column(nullable = false)
    private LocalDateTime pickupDate;

    @Builder.Default
    @Column(nullable = false)
    private int wishCount = 0;

    @Builder.Default
    @Column(nullable = false)
    private int viewCount = 0;

    @Builder.Default
    @Column(nullable = false)
    private int participantCount = 0;

    @Builder.Default
    @Column(nullable = false, length = 10)
    private String postStatus = "OPEN";

    private String pickupChangeReason;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="user_id", nullable = false)
    private User user;

    @Builder.Default
    @OneToMany(mappedBy = "groupBuy", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<GroupBuyCategory> groupBuyCategories = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "groupBuy",
               cascade = CascadeType.ALL,
               orphanRemoval = true)
    @OrderBy("imageSeqNo ASC")
    private List<Image> images = new ArrayList<>();


    public List<Category> getCategories() {
        return groupBuyCategories.stream()
                .map(GroupBuyCategory::getCategory)
                .collect(Collectors.toList());
    }

    public void increaseLeftAmount(int quantity) {
        this.leftAmount += quantity;
    }

    public void decreaseLeftAmount(int quantity) {
        this.leftAmount = Math.max(0, this.leftAmount - quantity);
    }

    public void increaseParticipantCount() {
        this.participantCount++;
    }

    public void decreaseParticipantCount() {
        this.participantCount = Math.max(0, this.participantCount - 1);
    }

    // 공구 게시글 생성 팩토리 메서드
    public static GroupBuy of(CreateGroupBuyRequest req, User host) {
        System.out.println(">> DTO 이미지 리스트: " + req.getImageUrls());
        System.out.println(">> DTO URL: " + req.getUrl());
        System.out.println(">> 로그인 유저: " + host);

        return GroupBuy.builder()
                .title(req.getTitle())
                .name(req.getName())
                .url(req.getUrl())
                .price(req.getPrice())
                .unitPrice(req.getPrice() / req.getTotalAmount()) // 정수로 치환 필요
                .totalAmount(req.getTotalAmount())
                .leftAmount(req.getLeftAmount())
                .unitAmount(req.getUnitAmount())
                .description(req.getDescription())
                .dueDate(req.getDueDate())
                .location(req.getLocation())
                .pickupDate(req.getPickupDate())
                .user(host)
                .build();
    }

    @Transient
    public double getSoldRatio() {
        if (totalAmount == 0) return 0.0;
        return (double)(totalAmount - leftAmount) / totalAmount;
    }

    @Transient
    public boolean isDueSoon() {
        return getSoldRatio() >= 0.8;
    }

}
