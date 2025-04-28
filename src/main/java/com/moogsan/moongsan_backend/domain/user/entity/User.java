package com.moogsan.moongsan_backend.domain.user.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 255)
    private String email;

    @Column(length = 60)
    private String nickname;

    @Column(length = 36)
    private String name;

    @Column(name = "phone_number", length = 11)
    private String phoneNumber;

    @Column(length = 60)
    private String password;

    @Column(name = "account_bank", length = 30)
    private String accountBank;

    @Column(name = "account_number", length = 32)
    private String accountNumber;

    @Column(name = "image_url", length = 512)
    private String imageUrl;

    @Column(nullable = false, length = 30)
    private String type; // USER, ADMIN

    @Column(nullable = false, length = 30)
    private String status; // ACTIVE, SUSPENDED, DEACTIVATED

    @Column(name = "joined_at", nullable = false)
    private LocalDateTime joinedAt;

    @Column(name = "modified_at")
    private LocalDateTime modifiedAt;

    @Column(name = "logout_at")
    private LocalDateTime logoutAt;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;
}
