package com.balanceformation.repository;

import com.balanceformation.entity.balance1.Balance1;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface Balance1Repository extends JpaRepository<Balance1, Long> {
}