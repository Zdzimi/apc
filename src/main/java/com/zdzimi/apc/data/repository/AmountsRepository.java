package com.zdzimi.apc.data.repository;

import com.zdzimi.apc.data.entity.Amount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AmountsRepository extends JpaRepository<Amount, Long> {

}
