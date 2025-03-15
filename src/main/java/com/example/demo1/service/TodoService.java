package com.example.demo1.service;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.demo1.entity.Todo;
import com.example.demo1.entity.User;
import com.example.demo1.mapper.TodoMapper;
import com.example.demo1.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TodoService {

    private final TodoMapper todoMapper;
    @Autowired
    private UserMapper userMapper;
    // 使用构造函数注入
    public TodoService(TodoMapper todoMapper) {
        this.todoMapper = todoMapper;
    }


    // 分页查询待办事件及其对应的用户信息
    public Page<Todo> getTodoPageWithUser(Integer pageNum, Integer pageSize, String search) {
        // 创建分页对象
        Page<Todo> page = new Page<>(pageNum, pageSize);

        // 创建查询条件
        LambdaQueryWrapper<Todo> queryWrapper = Wrappers.<Todo>lambdaQuery();

        // 如果有搜索条件，则添加模糊查询
        if (StrUtil.isNotBlank(search)) {
            queryWrapper.like(Todo::getTitle, search); // 假设搜索条件是待办事件的标题
        }

        // 执行分页查询
        page = todoMapper.selectPage(page, queryWrapper);

        // 为每条待办事件补充用户信息
        page.getRecords().forEach(todo -> {
            User user = userMapper.selectById(todo.getUserId());
            todo.setUser(user);
        });

        return page;
    }
    // 查询所有待办事件
    public List<Todo> getAllTodos() {
        return todoMapper.selectList(null);
    }


    // 创建待办事件
    public Todo createTodo(Todo todo) {
        todoMapper.insert(todo);
        return todo;
    }

    // 更新待办事件
    public Todo updateTodo(Integer id, Todo todo) {
        todo.setId(id);
        todoMapper.updateById(todo);
        return todo;
    }

    // 删除待办事件
    public void deleteTodo(Integer id) {
        todoMapper.deleteById(id);
    }

    // 查询待办事件及其对应的用户信息
    public Todo getTodoWithUser(Integer id) {
        Todo todo = todoMapper.selectById(id);
        if (todo != null) {
            // 查询对应的用户信息
            User user = userMapper.selectById(todo.getUserId());
            todo.setUser(user);
        }
        return todo;
    }
}