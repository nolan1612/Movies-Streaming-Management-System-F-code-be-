/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;

import enums.Role;
import model.User;
import service.AuthService;
/**
 *
 * @author nguyenhoangminhnhat
 */
public class AuthController {
    private AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    public User register(String username, String password, String fullName, String email, Role role) {
        try {
            return authService.register(username, password, fullName, email, role);
        } catch (Exception e) {
            System.err.println("Đăng ký thất bại: " + e.getMessage());
            return null;
        }
    }

    public User login(String username, String password) {
        return authService.login(username, password);
    }
}
