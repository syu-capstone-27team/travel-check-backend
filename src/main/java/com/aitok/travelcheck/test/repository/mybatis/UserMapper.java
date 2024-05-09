package com.aitok.travelcheck.test.repository.mybatis;

import com.aitok.travelcheck.test.dto.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Optional;

@Mapper
public interface UserMapper {
    User findById(@Param("id") int id);
}
