package com.atguigu.jpa.dao;

import com.atguigu.jpa.domain.AccountDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountDetailRepository extends JpaRepository<AccountDetail,Integer> {
}