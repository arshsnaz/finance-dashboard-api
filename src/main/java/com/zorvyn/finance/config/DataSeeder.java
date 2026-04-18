package com.zorvyn.finance.config;

import com.zorvyn.finance.enums.Role;
import com.zorvyn.finance.model.User;
import com.zorvyn.finance.repository.UserRepository;
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

        if (Boolean.FALSE.equals(userRepository.existsByEmail("viewer@zorvyn.com"))) {
            User viewer = User.builder()
                    .name("Test Viewer")
                    .email("viewer@zorvyn.com")
                    .password(passwordEncoder.encode("Viewer@1234"))
                    .role(Role.VIEWER)
                    .isActive(true)
                    .build();
            userRepository.save(viewer);
        }

        if (Boolean.FALSE.equals(userRepository.existsByEmail("analyst@zorvyn.com"))) {
            User analyst = User.builder()
                    .name("Test Analyst")
                    .email("analyst@zorvyn.com")
                    .password(passwordEncoder.encode("Analyst@1234"))
                    .role(Role.ANALYST)
                    .isActive(true)
                    .build();
            userRepository.save(analyst);
        }
    }
}
