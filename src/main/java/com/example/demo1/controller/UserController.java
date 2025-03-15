package com.example.demo1.controller;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.demo1.common.Result;
import com.example.demo1.entity.Todo;
import com.example.demo1.entity.User;
import com.example.demo1.mapper.TodoMapper;
import jakarta.annotation.Resource;
import com.example.demo1.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {

    @Resource
    UserMapper userMapper;

    @Autowired
    private TodoMapper todoMapper;

    @PostMapping
    public Result<?> save(@RequestBody User user) {
        if(user.getPassword()==null){
            user.setPassword("123456");
        }
        userMapper.insert(user);
        return Result.success();
    }

    @PostMapping("/login")
    public Result<?> login(@RequestBody User user) {
        User res = userMapper.selectOne(Wrappers.<User>lambdaQuery().eq(User::getUsername, user.getUsername()).eq(User::getPassword, user.getPassword()));
        if(res==null){
            return Result.error("-1","用户名密码错误");
        }
        return Result.success(res);
    }

    @PostMapping("/register")
    public Result<?> register(@RequestBody User user) {
        User res = userMapper.selectOne(Wrappers.<User>lambdaQuery().eq(User::getUsername, user.getUsername()));
        if(res!=null){
            return Result.error("-1","用户名重复");
        }
        userMapper.insert(user);
        return Result.success();
    }

    @PutMapping
    public Result<?> update(@RequestBody User user) {
        userMapper.updateById(user);
        return Result.success();
    }

    @GetMapping
    public Result<?> findPage(@RequestParam(defaultValue = "1") Integer pageNum,
                              @RequestParam(defaultValue = "10") Integer pageSize,
                               @RequestParam(defaultValue = "") String search) {
        try{
            LambdaQueryWrapper<User> queryWrapper = Wrappers.<User>lambdaQuery();
            if(StrUtil.isNotBlank(search)){
                queryWrapper.like(User::getNickName,search);
            }
            Page<User> userPage=userMapper.selectPage(new Page<>(pageNum, pageSize),queryWrapper );
            return Result.success(userPage);
        }catch (Exception e) {
            // 捕获异常并返回错误信息
            return Result.error("500", "Failed to fetch users: " + e.getMessage());
        }

    }
    @DeleteMapping("/{id}")
    public Result<?> delete(@PathVariable Integer id) {
        userMapper.deleteById(id);
        return Result.success();
    }

    // 查询用户及其待办事件
    @GetMapping("/{userId}/todos")
    public Result<?> getUserWithTodos(@PathVariable Integer userId) {
        // 查询用户信息
        User user = userMapper.selectById(userId);
        if (user != null) {
            // 查询用户的待办事件
            List<Todo> todos = todoMapper.findByUserId(userId);
            user.setTodos(todos);
        }
        return Result.success(user);
    }
    // 全局异常处理
    @ExceptionHandler(Exception.class)
    public Result<?> handleException(Exception e) {
        return Result.error("500", "Internal Server Error: " + e.getMessage());
    }
}
