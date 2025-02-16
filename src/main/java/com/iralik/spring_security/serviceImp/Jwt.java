package com.iralik.spring_security.serviceImp;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class Jwt {
    private String signInKey= "";

    public Jwt() {
        this.signInKey = generateSecretKey();
    }

    private String generateSecretKey() {
        try {
            KeyGenerator keyGen = KeyGenerator.getInstance("HmacSHA512");
            keyGen.init(512);
            SecretKey secKey = keyGen.generateKey();
            return Base64.getEncoder().encodeToString(secKey.getEncoded());
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Failed to generate secret key", e);
        }
    }

    public String createToken(String userName) {
        Map<String, Object> userInfo = new HashMap<>();
        return Jwts.builder()
                .claims()
                .add(userInfo)
                .subject(userName)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + 30 * 60 * 60 * 1000))
                .and()
                .signWith(getKey())
                .compact();
    }
    private SecretKey getKey(){
        byte[] keyBytes = Decoders.BASE64.decode(signInKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }
    public String getUserName(String token) {
        return extractClaim(token, Claims::getSubject);
    }
    public boolean validateToken(String token, UserDetails userDetail) {
        final String userName = getUserName(token);
        return (userName.equals(userDetail.getUsername()) && !isTokenExpired(token));
    }
    private <T> T extractClaim(String token, Function<Claims, T> claimResolver) {
        final Claims claims = extractAllClaims(token);
        return claimResolver.apply(claims);
    }
    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }
    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }
    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(getKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }








}
