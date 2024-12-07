package com.example.jwt.service;

import com.example.jwt.entity.UserEntity;
import com.example.jwt.repository.UserRepository;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Optional;

@Service
public class LoginService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Value("${jwt.Key}")
    private String keyJWT;

    public String login(String email, String password) {
        String token = "";
        Optional<UserEntity> user = userRepository.findByEmail(email);
        if (user.isPresent()) {
            // có giá trị
            // hủy giá trị
            UserEntity userEntity = user.get();
            if (passwordEncoder.matches(password, userEntity.getPassword())) {
                SecretKey key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(keyJWT));
                String jws = Jwts.builder().subject(userEntity.getRole()).signWith(key).compact();
                token = jws;
            }
        }
        return token;
    }
}