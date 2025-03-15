package com.example.demo1.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import net.sf.jsqlparser.expression.DateTimeLiteralExpression;

import java.util.Date;

@Data
@TableName("Todo") // 指定数据库表名
public class Todo {
    @TableId(type = IdType.AUTO) // 主键自增
    private Integer id;

    private String title;       // 待办事件标题
    private String description; // 待办事件描述
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date dueDate;       // 截止日期
    private Boolean completed;  // 是否完成

    //@TableField("user_id") // 数据库字段名
    private Integer userId;     // 关联的用户ID

    @TableField(exist = false) // 非数据库字段
    private User user;          // 关联的用户信息
}
