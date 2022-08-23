package com.atguigu.jpa.domain;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "account")
public class Account {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;

    @Column(name = "username")
    String username;

    @Column(name = "password")
    String password;

    /**
     * 外键关联字段
     * 一对一：懒加载
     */
    @JoinColumn(name = "detail_id")
    @OneToOne(fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    AccountDetail accountDetail;
}