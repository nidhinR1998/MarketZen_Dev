package com.nidhin.marketzen.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import javax.crypto.SecretKey;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public class Jwtprovider {

    private static SecretKey key = Keys.hmacShaKeyFor(JwtConstant.SECRETE_KEY.getBytes());

    public static String generateToken(Authentication auth) {
        Collection<? extends GrantedAuthority> authorities = auth.getAuthorities();
        String roles = popularAuthorities(authorities);
        String jwt = Jwts.builder()
                .setIssuedAt(new Date())
                .setExpiration(new Date(new Date().getTime() + 3600000))
                .claim("email", auth.getName())
                .claim("authorities", roles)
                .signWith(key)
                .compact();

        return jwt;
    }

    public static String getEamilFromToken(String token) {
        token = token.substring(7);
        Claims claims = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
        String email = String.valueOf(claims.get("email"));
        return email;
    }


    public static String getChangePassowrdEamilFromToken(String token) {
        Claims claims = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
        String email = String.valueOf(claims.get("email"));
        return email;
    }


    private static String popularAuthorities(Collection<? extends GrantedAuthority> authorities) {
        Set<String> auth = new HashSet<>();
        for (GrantedAuthority ga : authorities) {
            auth.add(ga.getAuthority());
        }
        return String.join(",", auth);
    }

    // New method for generating a forgot password token
    public static String generateForgotPasswordToken(String email) {
        // Set expiration to 5 minutes
        Date expiryDate = new Date(new Date().getTime() + 5 * 60 * 1000);

        return Jwts.builder()
                .setIssuedAt(new Date())
                .setExpiration(expiryDate)
                .claim("email", email)
                .signWith(key)
                .compact();
    }

}
