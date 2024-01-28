package com.zdzimi.apc.data.repository;

import com.zdzimi.apc.data.entity.Code;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CodesRepository extends JpaRepository<Code, Long> {

}
