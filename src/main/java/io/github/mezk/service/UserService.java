package io.github.mezk.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import io.github.mezk.model.User;
import io.github.mezk.repository.UserRepository;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public User getById(Integer userId) {
        return userRepository.findOne(userId);
    }
}
