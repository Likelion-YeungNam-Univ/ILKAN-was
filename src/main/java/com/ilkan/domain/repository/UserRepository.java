package com.ilkan.domain.repository;

import com.ilkan.domain.entity.Work;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

// 내가 올린 일거리 보관함
public interface UserRepository extends JpaRepository<Work, Long> {
    /*
     로그인한 사용자가 requester(의뢰자)인 일거리만 조회
     스프링 데이터 JPA 메서드 네이밍 규칙 사용
     @param requesterId 로그인한 사용자 ID (user.user_id)
     @param pageable 페이징 정보
     @return Page<Work> */

    Page<Work> findByRequester_Id(Long requesterId, Pageable pageable);
}
