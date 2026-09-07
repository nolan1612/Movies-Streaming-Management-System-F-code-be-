package service;

import model.User;
import repository.UserRepository;

public class UserService {
    private UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public void updateUserProfile(User user) {
        userRepository.update(user);
    }
}