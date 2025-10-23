package com.onlineexam.controller;

import com.onlineexam.dto.ApiResponse;
import com.onlineexam.dto.CategoryDto;
import com.onlineexam.dto.UserDto;
import com.onlineexam.entity.User;
import com.onlineexam.service.CategoryService;
import com.onlineexam.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
@Tag(name = "Admin Management", description = "Admin management APIs")
public class AdminController {
    
    private final UserService userService;
    private final CategoryService categoryService;
    
    // User Management
    @GetMapping("/users")
    @Operation(summary = "Get all users", description = "Retrieve all users with pagination")
    public ResponseEntity<ApiResponse<Page<UserDto>>> getAllUsers(Pageable pageable) {
        try {
            Page<UserDto> users = userService.getAllUsers(pageable);
            return ResponseEntity.ok(ApiResponse.success(users));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Failed to get users: " + e.getMessage()));
        }
    }
    
    @PutMapping("/users/{userId}/role")
    @Operation(summary = "Update user role", description = "Update a user's role")
    public ResponseEntity<ApiResponse<UserDto>> updateUserRole(@PathVariable Long userId, 
                                                              @RequestParam User.Role role) {
        try {
            UserDto updatedUser = userService.updateUserRole(userId, role);
            return ResponseEntity.ok(ApiResponse.success("User role updated successfully", updatedUser));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Failed to update user role: " + e.getMessage()));
        }
    }
    
    @DeleteMapping("/users/{userId}")
    @Operation(summary = "Delete user", description = "Delete a user")
    public ResponseEntity<ApiResponse<Void>> deleteUser(@PathVariable Long userId) {
        try {
            userService.deleteUser(userId);
            return ResponseEntity.ok(ApiResponse.success("User deleted successfully", null));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Failed to delete user: " + e.getMessage()));
        }
    }
    
    // Category Management
    @PostMapping("/categories")
    @Operation(summary = "Create category", description = "Create a new category")
    public ResponseEntity<ApiResponse<CategoryDto>> createCategory(@Valid @RequestBody CategoryDto categoryDto) {
        try {
            CategoryDto category = categoryService.createCategory(categoryDto);
            return ResponseEntity.ok(ApiResponse.success("Category created successfully", category));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Failed to create category: " + e.getMessage()));
        }
    }
    
    @PutMapping("/categories/{categoryId}")
    @Operation(summary = "Update category", description = "Update a category")
    public ResponseEntity<ApiResponse<CategoryDto>> updateCategory(@PathVariable Long categoryId, 
                                                                 @Valid @RequestBody CategoryDto categoryDto) {
        try {
            CategoryDto category = categoryService.updateCategory(categoryId, categoryDto);
            return ResponseEntity.ok(ApiResponse.success("Category updated successfully", category));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Failed to update category: " + e.getMessage()));
        }
    }
    
    @DeleteMapping("/categories/{categoryId}")
    @Operation(summary = "Delete category", description = "Delete a category")
    public ResponseEntity<ApiResponse<Void>> deleteCategory(@PathVariable Long categoryId) {
        try {
            categoryService.deleteCategory(categoryId);
            return ResponseEntity.ok(ApiResponse.success("Category deleted successfully", null));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Failed to delete category: " + e.getMessage()));
        }
    }
}


