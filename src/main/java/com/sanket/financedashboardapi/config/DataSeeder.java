package com.sanket.financedashboardapi.config;

import com.sanket.financedashboardapi.enums.Role;
import com.sanket.financedashboardapi.enums.UserStatus;
import com.sanket.financedashboardapi.model.User;
import com.sanket.financedashboardapi.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class DataSeeder implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        if (userRepository.count() == 0) {
            User admin = User.builder()
                    .name("Super Admin")
                    .email("admin@financeapi.com")
                    .passwordHash(passwordEncoder.encode("Admin@123"))
                    .role(Role.ADMIN)
                    .status(UserStatus.ACTIVE)
                    .createdAt(LocalDateTime.now())
                    .updatedAt(LocalDateTime.now())
                    .build();

            userRepository.save(admin);
            log.info("Default admin seeded → email: admin@financeapi.com | password: Admin@123");
        } else {
            log.info("Users already exist, skipping seed.");
        }
    }
}