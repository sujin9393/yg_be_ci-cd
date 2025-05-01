package com.moogsan.moongsan_backend.domain.user.repository;

import com.moogsan.moongsan_backend.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * User 엔티티에 대한 데이터베이스 접근을 처리하는 Repository 인터페이스.
 * Spring Data JPA가 자동으로 구현체를 생성해준다.
 */
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * 이메일 중복 여부를 확인하는 메서드.
     * @param email 조회할 이메일
     * @return 해당 이메일이 존재하면 true, 없으면 false
     */
    boolean existsByEmail(String email);

    /**
     * 닉네임 중복 여부를 확인하는 메서드.
     * @param nickname 조회할 닉네임
     * @return 해당 닉네임이 존재하면 true, 없으면 false
     */
    boolean existsByNickname(String nickname);

    /**
     * 전화번호 중복 여부를 확인하는 메서드.
     * @param phoneNumber 조회할 전화번호
     * @return 해당 전화번호가 존재하면 true, 없으면 false
     */
    boolean existsByPhoneNumber(String phoneNumber);

    /**
     * 이메일로 사용자를 조회하는 메서드.
     * @param email 조회할 이메일
     * @return Optional로 감싼 User 엔티티
     */
    java.util.Optional<User> findByEmail(String email);
}