package com.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.entity.User;
import com.repository.UserRepository;
import com.service.UserService;

@Service
public class UserServiceImpl implements UserService {
    private UserRepository UserRepository;

    public UserServiceImpl(UserRepository UserRepository) {
        this.UserRepository = UserRepository;
        // TODO Auto-generated constructor stub
    }

    @Override
    public List<User> list() {
        // TODO Auto-generated method stub
        return UserRepository.findAll();
    }

    @Override
    public User create(User User) {
        return UserRepository.save(User);
    }

    @Override
    public User update(int id, User User) {
        // TODO Auto-generated method stub
        User.setId(id);
        return UserRepository.save(User);
    }

    @Override
    public User delete(int id) {
        User User = UserRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid Categry Id:" + id));
        UserRepository.delete(User);
        return User;
    }

    @Override
    public User findById(int id) {
        User User = UserRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid student Id:" + id));
        return User;
    }

    @Override
    public User findByUsername(String username) {
        // TODO Auto-generated method stub
        return UserRepository.findByUsername(username);
    }
}
