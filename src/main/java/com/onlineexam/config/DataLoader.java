package com.onlineexam.config;

import com.onlineexam.entity.Category;
import com.onlineexam.entity.User;
import com.onlineexam.repository.CategoryRepository;
import com.onlineexam.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataLoader implements CommandLineRunner {
    
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final PasswordEncoder passwordEncoder;
    
    @Override
    public void run(String... args) throws Exception {
        loadUsers();
        loadCategories();
    }
    
    private void loadUsers() {
        if (userRepository.count() == 0) {
            log.info("Loading users...");
            
            // Create Admin User
            User admin = new User();
            admin.setName("System Administrator");
            admin.setEmail("admin@onlineexam.com");
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setRole(User.Role.ADMIN);
            userRepository.save(admin);
            
            // Create Teacher User
            User teacher = new User();
            teacher.setName("John Teacher");
            teacher.setEmail("teacher@onlineexam.com");
            teacher.setPassword(passwordEncoder.encode("teacher123"));
            teacher.setRole(User.Role.TEACHER);
            userRepository.save(teacher);
            
            // Create Student User
            User student = new User();
            student.setName("Jane Student");
            student.setEmail("student@onlineexam.com");
            student.setPassword(passwordEncoder.encode("student123"));
            student.setRole(User.Role.STUDENT);
            userRepository.save(student);
            
            log.info("Users loaded successfully");
        }
    }
    
    private void loadCategories() {
        if (categoryRepository.count() == 0) {
            log.info("Loading categories...");
            
            Category java = new Category();
            java.setName("Java Programming");
            java.setDescription("Questions related to Java programming language");
            categoryRepository.save(java);
            
            Category mathematics = new Category();
            mathematics.setName("Mathematics");
            mathematics.setDescription("Mathematical problems and concepts");
            categoryRepository.save(mathematics);
            
            Category science = new Category();
            science.setName("Science");
            science.setDescription("General science questions");
            categoryRepository.save(science);
            
            Category generalKnowledge = new Category();
            generalKnowledge.setName("General Knowledge");
            generalKnowledge.setDescription("General knowledge and current affairs");
            categoryRepository.save(generalKnowledge);
            
            Category computerScience = new Category();
            computerScience.setName("Computer Science");
            computerScience.setDescription("Computer science fundamentals");
            categoryRepository.save(computerScience);
            
            log.info("Categories loaded successfully");
        }
    }
}


