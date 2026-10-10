package com.nexus.platform.security;
import io.jsonwebtoken.*; import io.jsonwebtoken.security.Keys; import org.springframework.beans.factory.annotation.Value; import org.springframework.stereotype.Service; import java.nio.charset.StandardCharsets; import java.security.Key; import java.util.*;
@Service public class JwtService {
 private final Key key; private final long expiration;
 public JwtService(@Value("${nexus.jwt.secret}") String secret,@Value("${nexus.jwt.expiration-ms}") long expiration){ key=Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8)); this.expiration=expiration; }
 public String generate(String email,String role){return Jwts.builder().subject(email).claim("role",role).issuedAt(new Date()).expiration(new Date(System.currentTimeMillis()+expiration)).signWith(key).compact();}
 private Claims claims(String token){return Jwts.parser().verifyWith((javax.crypto.SecretKey)key).build().parseSignedClaims(token).getPayload();}
 public String extractEmail(String token){return claims(token).getSubject();}
 public String extractRole(String token){return claims(token).get("role",String.class);}
 public boolean valid(String token){try{claims(token);return true;}catch(Exception e){return false;}}
}
