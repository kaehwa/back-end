package com.example.gaehwa.repository;

import com.example.gaehwa.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByFirebaseUid(String firebaseUid);

}



