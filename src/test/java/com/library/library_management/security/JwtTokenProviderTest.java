package com.library.library_management.security;

import com.library.library_management.entity.Role;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.crypto.SecretKey;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class JwtTokenProviderTest {

    private JwtTokenProvider jwtTokenProvider;

    @BeforeEach
    void setUp() {
        jwtTokenProvider = new JwtTokenProvider();
    }

    @Test
    void testGenerateToken() {
        String token = jwtTokenProvider.generateToken("testUser", Role.ADMIN);
        assertNotNull(token);
    }

    @Test
    void testGetClaimsFromToken() {
        String token = jwtTokenProvider.generateToken("testUser", Role.ADMIN);
        Claims claims = jwtTokenProvider.getClaimsFromToken(token);

        assertEquals("testUser", claims.getSubject());
        assertEquals("ADMIN", claims.get("role"));
    }

    @Test
    void testGetUsernameFromToken() {
        String token = jwtTokenProvider.generateToken("testUser", Role.ADMIN);
        String username = jwtTokenProvider.getUsernameFromToken(token);

        assertEquals("testUser", username);
    }

    @Test
    void testValidateToken_ValidToken() {
        String token = jwtTokenProvider.generateToken("testUser", Role.ADMIN);
        assertTrue(jwtTokenProvider.validateToken(token));
    }

    @Test
    void testValidateToken_ExpiredToken() {
        SecretKey testSecretKey = Keys.secretKeyFor(SignatureAlgorithm.HS512);
        JwtTokenProvider expiredTokenProvider = new JwtTokenProvider() {
            @Override
            public String generateToken(String username, Role role) {
                return Jwts.builder()
                        .setSubject(username)
                        .claim("role", role.name())
                        .setExpiration(new Date(System.currentTimeMillis() - 1000))
                        .signWith(testSecretKey)
                        .compact();
            }

            @Override
            public boolean validateToken(String token) {
                try {
                    Jwts.parserBuilder().setSigningKey(testSecretKey).build().parseClaimsJws(token);
                    return true;
                } catch (Exception e) {
                    return false;
                }
            }
        };

        String token = expiredTokenProvider.generateToken("testUser", Role.ADMIN);
        assertFalse(expiredTokenProvider.validateToken(token));
    }

    @Test
    void testValidateToken_InvalidToken() {
        String invalidToken = "invalid.token.value";
        assertThrows(io.jsonwebtoken.MalformedJwtException.class, () -> jwtTokenProvider.validateToken(invalidToken));
    }
}