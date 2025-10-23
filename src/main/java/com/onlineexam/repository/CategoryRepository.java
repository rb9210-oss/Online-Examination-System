package com.onlineexam.repository;

import com.onlineexam.entity.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    
    boolean existsByName(String name);
    
    Page<Category> findByNameContainingIgnoreCase(String name, Pageable pageable);
}


