package com.atguigu.jpa.domain;

import lombok.Data;
import lombok.ToString;

import javax.persistence.*;

@Data
@ToString
@Entity
@Table(name = "user")
public class User {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;

    @Column(name = "username")
    String username;

    @Column(name = "password")
    String password;
}