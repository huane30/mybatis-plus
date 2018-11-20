package com.baomidou.mybatisplus.core.test;

import com.baomidou.mybatisplus.annotation.TableField;


public class Role {

    private Integer id;

    @TableField("roleName")
    private String name;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
