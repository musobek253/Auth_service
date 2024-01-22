package com.musobek.auth_service.config;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

import static com.musobek.auth_service.entity.enam.Permission.*;
import static com.musobek.auth_service.entity.enam.Role.*;
import static org.springframework.http.HttpMethod.*;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfiguration {

    private final AuthenticationProvider authenticationProvider;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final LogoutHandler logoutHandler;
    @Autowired
    public SecurityConfiguration(AuthenticationProvider authenticationProvider, JwtAuthenticationFilter jwtAuthenticationFilter, LogoutHandler logoutHandler) {
        this.authenticationProvider = authenticationProvider;
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
        this.logoutHandler = logoutHandler;
    }
    private static final String[] WHITE_LIST_URL ={"/api/v1/auth/**",
            "/v2/api-docs",
            "/v3/api-docs",
            "/v3/api-docs/**",
            "/swagger-resources",
            "/swagger-resources/**",
            "/configuration/ui",
            "/configuration/security",
            "/swagger-ui/**",
            "/webjars/**",
            "/swagger-ui.html",

    };


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
        http.csrf(AbstractHttpConfigurer::disable)
                .cors(httpSecurityCorsConfigurer -> httpSecurityCorsConfigurer.configurationSource(corsConfigurationSource()))
                .authorizeHttpRequests(auth -> auth
                .requestMatchers(WHITE_LIST_URL).permitAll()
                .requestMatchers("/api/v1/mengment/**").hasAnyRole(ADMIN.name(),MENGER.name())
                .requestMatchers(GET, "/api/v1/mengment/**").hasAnyAuthority(ADMIN_READ.name(),MANGER_READ.name())
                .requestMatchers(POST, "/api/v1/mengment/**").hasAnyAuthority(ADMIN_CREATE.name(),MANGER_CREATE.name())
                .requestMatchers(PUT, "/api/v1/mengment/**").hasAnyAuthority(ADMIN_UPDATE.name(),MANGER_UPDATE.name())
                .requestMatchers(DELETE, "/api/v1/mengment/**").hasAnyAuthority(ADMIN_DELETE.name(),MANGER_DELETE.name())
//                .requestMatchers("/api/v1/admin/*").hasAnyRole(ADMIN.name())
//                .requestMatchers(GET,"/api/v1/admin/**").hasAuthority(ADMIN_READ.name())
//                .requestMatchers(POST,"/api/v1/admin/**").hasAuthority(ADMIN_CREATE.name())
//                .requestMatchers(DELETE,"/api/v1/admin/**").hasAuthority(ADMIN_DELETE.name())
//                .requestMatchers(PUT,"/api/v1/admin/**").hasAuthority(ADMIN_UPDATE.name())
//                .requestMatchers("/api/v-1/room/add").hasAnyRole(ADMIN.name(), MENGER.name())
                .requestMatchers(POST, "/api/v-1/room/add").hasAnyAuthority(ADMIN_CREATE.name(),MANGER_CREATE.name())
                .requestMatchers("/api/v-1/room/all").hasAnyRole(ADMIN.name(), USER.name(), MENGER.name())
                .requestMatchers(GET,"/api/v-1/room/all").hasAnyAuthority(ADMIN_READ.name(),USER_READ.name(),MANGER_READ.name())
                .anyRequest().authenticated())
                .sessionManagement((sesssio)->sesssio.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .logout(logout -> logout
                                .logoutUrl("/api/v1/auth/logout")
                                .addLogoutHandler(logoutHandler)
                                .logoutSuccessHandler(((request,response,authentication) -> SecurityContextHolder.clearContext()))
                );

        return http.build();
    }
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowCredentials(true);
        configuration.addAllowedOrigin("http:/localhost:5173");
        configuration.setAllowedHeaders(Arrays.asList(
                HttpHeaders.AUTHORIZATION,
                HttpHeaders.CONTENT_TYPE,
                HttpHeaders.ACCEPT
        ));
        configuration.setAllowedMethods(Arrays.asList(
                HttpMethod.GET.name(),
                HttpMethod.POST.name(),
                HttpMethod.PUT.name(),
                HttpMethod.DELETE.name()
        ));
        configuration.setMaxAge(3600L);
        source.registerCorsConfiguration("/**",configuration);
        return source;
    }

}
