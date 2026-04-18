package com.finance.dashboard.config;

import com.finance.dashboard.enums.Role;
import com.finance.dashboard.model.User;
import com.finance.dashboard.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataSeeder implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public DataSeeder(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        if (Boolean.FALSE.equals(userRepository.existsByEmail("admin@finance.com"))) {
            User admin = User.builder()
                    .name("Super Admin")
                    .email("admin@finance.com")
                    .password(passwordEncoder.encode("Admin@1234"))
                    .role(Role.ADMIN)
                    .isActive(true)
                    .build();
            userRepository.save(admin);
            System.out.println("Default admin user created: admin@finance.com / Admin@1234");
        }

        if (Boolean.FALSE.equals(userRepository.existsByEmail("viewer@finance.com"))) {
            User viewer = User.builder()
                    .name("Test Viewer")
                    .email("viewer@finance.com")
                    .password(passwordEncoder.encode("Viewer@1234"))
                    .role(Role.VIEWER)
                    .isActive(true)
                    .build();
            userRepository.save(viewer);
        }

        if (Boolean.FALSE.equals(userRepository.existsByEmail("analyst@finance.com"))) {
            User analyst = User.builder()
                    .name("Test Analyst")
                    .email("analyst@finance.com")
                    .password(passwordEncoder.encode("Analyst@1234"))
                    .role(Role.ANALYST)
                    .isActive(true)
                    .build();
            userRepository.save(analyst);
        }
    }
}
