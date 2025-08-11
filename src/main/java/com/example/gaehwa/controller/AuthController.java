package com.example.gaehwa.controller;

import com.example.gaehwa.dto.request.IdTokenRequest;
import com.example.gaehwa.dto.request.RefreshRequest;
import com.example.gaehwa.dto.response.TokenResponse;
import com.example.gaehwa.jwt.JwtUtil;
import com.example.gaehwa.service.FirebaseAuthService;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseToken;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    private final JwtUtil jwtUtil;
    private final FirebaseAuthService firebaseAuthService; //추가

    @PostMapping("/google")
    public ResponseEntity<?> googleLogin(@RequestBody IdTokenRequest request) {
        String idToken = request.getIdToken();

        try {
            //유저 저장 또는 업데이트
            firebaseAuthService.verifyAndSaveMember(idToken);

            FirebaseToken decodedToken = FirebaseAuth.getInstance().verifyIdToken(idToken);
            String uid = decodedToken.getUid();

            //JWT 발급
            String accessToken = jwtUtil.generateAccessToken(uid);
            String refreshToken = jwtUtil.generateRefreshToken(uid);

            return ResponseEntity.ok(new TokenResponse(accessToken, refreshToken));
        } catch (Exception e) {
            log.error("Token verification failed", e);
            return ResponseEntity.status(401).body("Invalid Firebase ID token");
        }
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refresh(@RequestBody RefreshRequest request) {
        String refreshToken = request.getRefreshToken();

        if (jwtUtil.isExpired(refreshToken)) {
            return ResponseEntity.status(401).body("Refresh Token expired");
        }

        String uid = jwtUtil.getUidFromToken(refreshToken);
        String newAccessToken = jwtUtil.generateAccessToken(uid);

        return ResponseEntity.ok(new TokenResponse(newAccessToken, refreshToken));
    }

}
