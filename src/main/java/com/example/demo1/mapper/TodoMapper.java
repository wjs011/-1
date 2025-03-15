package com.example.demo1.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.demo1.entity.Todo;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface TodoMapper extends BaseMapper<Todo> {
    @Select("SELECT * FROM Todo WHERE user_id = #{userId}")
    List<Todo> findByUserId(Integer userId);
}