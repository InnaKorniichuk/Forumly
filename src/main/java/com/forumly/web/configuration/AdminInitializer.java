package com.forumly.web.configuration;

import com.forumly.web.model.Role;
import com.forumly.web.model.User;
import com.forumly.web.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class AdminInitializer {
    @Bean
    CommandLineRunner createAdmin(UserRepository userRepository,
                                  PasswordEncoder passwordEncoder,
                                  @Value("${ADMIN_EMAIL}") String email,
                                  @Value("${ADMIN_PASSWORD}") String password) {
        return args -> {
            if (userRepository.existsByEmail(email)) {
                return;
            }

            User admin = new User();
            admin.setEmail(email);
            admin.setNickname("admin");
            admin.setPassword(passwordEncoder.encode(password));
            admin.setRole(Role.ADMIN);

            userRepository.save(admin);
        };
    }
}