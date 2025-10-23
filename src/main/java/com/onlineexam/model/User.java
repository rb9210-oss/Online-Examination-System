package com.onlineexam.model;

/**
 * User model class representing a user in the system
 * Can be either a Student or Admin
 */
public class User {
    private int id;
    private String username;
    private String password;
    private String role; // "STUDENT" or "ADMIN"
    private String email;
    private String fullName;

    /**
     * Default constructor
     */
    public User() {}

    /**
     * Constructor with all fields
     * @param id user ID
     * @param username username
     * @param password hashed password
     * @param role user role (STUDENT/ADMIN)
     * @param email user email
     * @param fullName user's full name
     */
    public User(int id, String username, String password, String role, String email, String fullName) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.role = role;
        this.email = email;
        this.fullName = fullName;
    }

    /**
     * Constructor for new user registration (without ID)
     * @param username username
     * @param password hashed password
     * @param role user role
     * @param email user email
     * @param fullName user's full name
     */
    public User(String username, String password, String role, String email, String fullName) {
        this.username = username;
        this.password = password;
        this.role = role;
        this.email = email;
        this.fullName = fullName;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    /**
     * Check if user is an admin
     * @return true if user is admin, false otherwise
     */
    public boolean isAdmin() {
        return "ADMIN".equalsIgnoreCase(role);
    }

    /**
     * Check if user is a student
     * @return true if user is student, false otherwise
     */
    public boolean isStudent() {
        return "STUDENT".equalsIgnoreCase(role);
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", role='" + role + '\'' +
                ", email='" + email + '\'' +
                ", fullName='" + fullName + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        User user = (User) obj;
        return id == user.id;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(id);
    }
}
