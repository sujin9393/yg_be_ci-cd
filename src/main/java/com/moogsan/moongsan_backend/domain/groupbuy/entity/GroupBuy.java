package com.moogsan.moongsan_backend.domain.groupbuy.entity;

import com.moogsan.moongsan_backend.domain.BaseEntity;
import com.moogsan.moongsan_backend.domain.groupbuy.dto.command.request.CreateGroupBuyRequest;
import com.moogsan.moongsan_backend.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
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

    @OneToMany(mappedBy = "groupBuy",
               cascade = CascadeType.ALL,
               orphanRemoval = true)
    @OrderBy("imageSeqNo ASC")
    private List<Image> images = new ArrayList<>();

    // 공구 게시글 생성 팩토리 메서드
    public static GroupBuy create(CreateGroupBuyRequest req, User host) {
        GroupBuy gb = new GroupBuy();
        gb.title = req.getTitle();
        gb.name = req.getName();
        gb.url = req.getUrl();
        gb.price = req.getPrice();
        gb.unitPrice =  (req.getPrice() / req.getTotalAmount()) * req.getUnitAmount();
        gb.totalAmount = req.getTotalAmount();
        gb.leftAmount = req.getLeftAmount();
        gb.unitAmount = req.getUnitAmount();
        gb.description = req.getDescription();
        gb.dueDate = LocalDateTime.parse(req.getDueDate());
        gb.location = req.getLocation();
        gb.pickupDate = LocalDateTime.parse(req.getPickupDate());

        gb.user = host;

        return gb;
    }

    // 공구 게시글 이미지 저장 로직
    public void addImage(String url, String imageUrlResized, int seqNo, boolean thumbnail) {
        Image img = Image.builder()
                .imageUrl(url)
                .imageUrlResized(imageUrlResized)
                .imageSeqNo(seqNo)
                .thumbnail(thumbnail)
                .groupBuy(this)
                .build();
        this.images.add(img);
    }
}
