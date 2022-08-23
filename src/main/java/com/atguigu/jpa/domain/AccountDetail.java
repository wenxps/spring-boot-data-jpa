package com.atguigu.jpa.domain;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "account_details")
public class AccountDetail {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;

    @Column(name = "address")
    String address;

    @Column(name = "email")
    String email;
}