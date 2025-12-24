package com.example.QLTV.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

@Configuration
@EnableJpaAuditing(auditorAwareRef = "auditorProvider") // Kích hoạt Auditing
public class JpaAuditingConfig {

    @Bean
    public AuditorAware<String> auditorProvider() {
        return () -> {
            // Lấy thông tin xác thực từ SecurityContext (đã được JWT Filter nạp vào)
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            // Nếu không có thông tin hoặc là người dùng ẩn danh
            if (authentication == null || !authentication.isAuthenticated()
                    || authentication.getPrincipal().equals("anonymousUser")) {
                return Optional.of("SYSTEM");
            }

            // Trả về tên người dùng từ JWT Token
            return Optional.ofNullable(authentication.getName());
        };
    }
}