package com.example.demo1.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.demo1.entity.User;


public interface UserMapper extends BaseMapper<User> {
    Page<User> findPage(Page<User> page);
}
