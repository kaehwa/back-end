package com.example.gaehwa.service;

import com.example.gaehwa.dto.MemberDto;
import com.example.gaehwa.entity.Member;
import com.example.gaehwa.repository.MemberRepository;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseToken;
import org.springframework.stereotype.Service;

@Service
public class FirebaseAuthService {

    private final MemberRepository memberRepository;

    public FirebaseAuthService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public MemberDto verifyAndSaveMember(String idToken) throws Exception {
        // 1. Firebase 토큰 검증
        FirebaseToken decodedToken = FirebaseAuth.getInstance().verifyIdToken(idToken);
        String firebaseUid = decodedToken.getUid();
        String email = decodedToken.getEmail();
        String nickname = decodedToken.getName();
        String profileImage = decodedToken.getPicture();

        // 2. firebaseUid 기준으로 회원 조회
        Member member = memberRepository.findByFirebaseUid(firebaseUid).orElse(null);

        if (member == null) {
            // 새 유저 등록
            member = Member.builder()
                    .firebaseUid(firebaseUid)
                    .email(email)
                    .nickname(nickname)
                    .image(profileImage)
                    .useYn(true)
                    .build();
        } else {
            // 기존 유저 정보 업데이트 (선택 사항)
            member.setEmail(email);
            member.setNickname(nickname);
            member.setImage(profileImage);
        }

        // 3. 저장
        Member saved = memberRepository.save(member);

        // 4. DTO 변환 및 반환
        return MemberDto.from(saved);
    }
}

