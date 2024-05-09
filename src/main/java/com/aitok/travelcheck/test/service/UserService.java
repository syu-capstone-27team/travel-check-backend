package com.aitok.travelcheck.test.service;

import com.aitok.travelcheck.test.dto.User;
import com.aitok.travelcheck.test.repository.mybatis.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserMapper userMapper;


    public Optional<User> findById(int id) {
        return Optional.ofNullable(userMapper.findById(id));
    }
}
