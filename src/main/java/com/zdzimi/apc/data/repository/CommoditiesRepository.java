package com.zdzimi.apc.data.repository;

import com.zdzimi.apc.data.entity.Commodity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CommoditiesRepository extends JpaRepository<Commodity, Long> {

  @Query("select c from Commodity c left join fetch c.amount a where c.code = ?1 and a.amount >= 1")
  Optional<Commodity> findByBarcodeAtLeastAmount(String barcode);
  
}
