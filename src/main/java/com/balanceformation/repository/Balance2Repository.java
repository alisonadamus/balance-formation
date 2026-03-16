package com.balanceformation.repository;

import com.balanceformation.entity.balance2.Balance2;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface Balance2Repository extends JpaRepository<Balance2, Long> {
}