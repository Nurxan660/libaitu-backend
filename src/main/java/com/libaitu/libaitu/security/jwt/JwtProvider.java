package com.libaitu.libaitu.security.jwt;

import io.jsonwebtoken.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtProvider {
    @Value("${secretKey}")
    private String secretKey;
    @Value("${expiration}")
    private int expiration;
    private  static final Logger log = LoggerFactory.getLogger(JwtProvider.class);

    public String generateToken(String barcode){
        return Jwts.builder()
                .setSubject(barcode)
                .setIssuedAt(new Date())
                .setExpiration(new Date(new Date().getTime()+expiration))
                .signWith(SignatureAlgorithm.HS512,secretKey)
                .compact();
    }

    public String getBarCodeFromToken(String jwt){
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(jwt).getBody().getSubject();
    }

    public boolean validateToken(String jwt)  {
        try {
            Jwts.parser().setSigningKey(secretKey).parseClaimsJws(jwt);
            return true;
        } catch (SignatureException e) {
            log.error("Invalid JWT signature");
        } catch (MalformedJwtException e) {
            log.error("Invalid JWT token");
        } catch (ExpiredJwtException e) {
            log.error("Token expired");
        } catch (UnsupportedJwtException e) {
            log.error("JWT token is unsupported");
        } catch (IllegalArgumentException e) {
            log.error("JWT claims string is empty");
        }
        return false;
    }
}
