/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package service;

import enums.Role;
import model.Admin;
import model.User;
import repository.UserRepository;
import utils.IdGenerator;
/**
 *
 * @author nguyenhoangminhnhat
 */
public class AuthService {
    private UserRepository userRepository;

    public AuthService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User register(String username, String password, String fullName, String email, Role role) {
        if (userRepository.findByUsername(username) != null) {
            throw new IllegalArgumentException("Username đã tồn tại!");
        }
        String userId = IdGenerator.generateId("USR");
        User newUser;
        if (role == Role.ADMIN) {
            newUser = new Admin(userId, username, password, fullName, email, 1);
        } else {
            newUser = new User(userId, username, password, fullName, email, role);
        }
        userRepository.add(newUser);
        return newUser;
    }

    public User login(String username, String password) {
        User user = userRepository.findByUsername(username);
        if (user != null && user.checkPassword(password)) {
            return user;
        }
        return null;
    }
}
