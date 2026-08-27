/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import enums.Role;
import enums.Status;
import java.io.Serializable;
import java.time.LocalDateTime;
/**
 *
 * @author nguyenhoangminhnhat
 */
public class User implements Serializable {
    private static final long serialVersionUID = 1L;

    private String userId;
    private String username;
    private String passwordHash;
    private String fullName;
    private String email;
    private Role role;
    private Status status;
    private LocalDateTime createdAt;

    public User() {}

    public User(String userId, String username, String passwordHash, String fullName, String email, Role role) {
        this.userId = userId;
        this.username = username;
        this.passwordHash = passwordHash;
        this.fullName = fullName;
        this.email = email;
        this.role = role;
        this.status = Status.ACTIVE;
        this.createdAt = LocalDateTime.now();
    }

    public String getUserId() { return userId; }
    public String getUsername() { return username; }
    public String getPasswordHash() { return passwordHash; }
    public String getFullName() { return fullName; }
    public String getEmail() { return email; }
    public Role getRole() { return role; }
    public Status getStatus() { return status; }
    public LocalDateTime getCreatedAt() { return createdAt; }

    public boolean checkPassword(String password) {
        return this.passwordHash.equals(password); // Thực tế dùng BCrypt hoặc Hashing
    }

    public void changePassword(String newPass) {
        this.passwordHash = newPass;
    }

    public void deactivate() {
        this.status = Status.INACTIVE;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
}
