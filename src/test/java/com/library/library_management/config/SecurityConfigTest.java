package com.library.library_management.config;

import com.library.library_management.security.JwtAuthenticationFilter;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import static org.assertj.core.api.Assertions.assertThat;

class SecurityConfigTest {

    private final JwtAuthenticationFilter jwtAuthenticationFilter = Mockito.mock(JwtAuthenticationFilter.class);
    private final SecurityConfig securityConfig = new SecurityConfig(jwtAuthenticationFilter);

    @Test
    void testSecurityFilterChain() throws Exception {
        HttpSecurity httpSecurity = Mockito.mock(HttpSecurity.class);

        Mockito.when(httpSecurity.csrf(Mockito.any())).thenReturn(httpSecurity);
        Mockito.when(httpSecurity.authorizeHttpRequests(Mockito.any())).thenReturn(httpSecurity);
        Mockito.when(httpSecurity.sessionManagement(Mockito.any())).thenReturn(httpSecurity);
        Mockito.when(httpSecurity.addFilterBefore(Mockito.any(), Mockito.any())).thenReturn(httpSecurity);

        SecurityFilterChain mockSecurityFilterChain = Mockito.mock(SecurityFilterChain.class);
        Mockito.doReturn(mockSecurityFilterChain).when(httpSecurity).build();

        SecurityFilterChain securityFilterChain = securityConfig.securityFilterChain(httpSecurity);
        assertThat(securityFilterChain).isNotNull();
    }

    @Test
    void testPasswordEncoder() {
        PasswordEncoder passwordEncoder = securityConfig.passwordEncoder();
        assertThat(passwordEncoder).isNotNull();
        assertThat(passwordEncoder.encode("password")).isNotEmpty();
    }

    @Test
    void testAuthenticationManager() throws Exception {
        AuthenticationConfiguration config = Mockito.mock(AuthenticationConfiguration.class);
        AuthenticationManager mockAuthManager = Mockito.mock(AuthenticationManager.class);

        Mockito.when(config.getAuthenticationManager()).thenReturn(mockAuthManager);

        AuthenticationManager authenticationManager = securityConfig.authenticationManager(config);
        assertThat(authenticationManager).isNotNull();
    }
}