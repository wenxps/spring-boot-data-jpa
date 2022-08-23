package com.atguigu.jpa.domain;

import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Data
@Entity //这是实体类
@Table(name = "authority") //对应哪张表
public class Authority {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String name; //权限名

    @ManyToMany(mappedBy = "authorityList")
    private List<Users> userList;
}