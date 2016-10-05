package com.springmvc.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.google.common.collect.Sets;
import com.springmvc.models.User;
import com.springmvc.repositories.RoleRepository;
import com.springmvc.repositories.UserRepository;
import com.springmvc.utils.DateTimeUtil;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public void save(User user) {
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        user.setRoles(Sets.newHashSet(roleRepository.findAll()));
        user.setCreatedAt(DateTimeUtil.getCurrTimestamp());
        user.setUpdatedAt(DateTimeUtil.getCurrTimestamp());
        userRepository.save(user);
    }

    @Override
    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }
}