package com.example.demo1.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.demo1.common.Result;
import com.example.demo1.entity.Todo;
import com.example.demo1.service.TodoService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/todos")
public class TodoController {
    private final TodoService todoService;

    public TodoController(TodoService todoService) {
        this.todoService = todoService;
    }

    // 获取所有待办事项
//    @GetMapping
//    public Result<?> getAllTodos() {
//        List<Todo> todos = todoService.getAllTodos();
//        return Result.success(todos);
//    }
    // 分页查询待办事件及其对应的用户信息
    @GetMapping
    public Result<?> findTodoPage(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(defaultValue = "") String search
    ) {
        try {
            Page<Todo> todoPage = todoService.getTodoPageWithUser(pageNum, pageSize, search);
            return Result.success(todoPage);
        } catch (Exception e) {
            return Result.error("500", "Failed to fetch todos: " + e.getMessage());
        }
    }


    // 创建待办事项
    @PostMapping
    public Result<?> createTodo(@RequestBody Todo todo) {
        Todo createdTodo = todoService.createTodo(todo);
        return Result.success(createdTodo);
    }

    // 更新待办事项
    @PutMapping("/{id}")
    public Result<?> updateTodo(@PathVariable Integer id, @RequestBody Todo todo) {
        Todo updatedTodo = todoService.updateTodo(id, todo);
        return Result.success(updatedTodo);
    }

    // 删除待办事项
    @DeleteMapping("/{id}")
    public Result<?> deleteTodo(@PathVariable Integer id) {
        todoService.deleteTodo(id);
        return Result.success();
    }

    // 根据ID查询待办事件及其对应的用户信息
    @GetMapping("/{id}")
    public Result<?> getTodoById(@PathVariable Integer id) {
        Todo todo = todoService.getTodoWithUser(id);
        if (todo != null) {
            return Result.success(todo);
        } else {
            return Result.error("1", "待办事项不存在");
        }
    }

    @ExceptionHandler(Exception.class)
    public Result<?> handleException(Exception e) {
        return Result.error("500", e.getMessage());
    }
}
