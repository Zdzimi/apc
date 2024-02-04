package com.zdzimi.apc.data.repository;

import com.zdzimi.apc.data.entity.Contractor;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ContractorsRepository extends JpaRepository<Contractor, Long> {

  @Query("select c from Contractor c left join fetch c.code where c.cardCode = ?1")
  Optional<Contractor> getByCardCode(String cardCode);

  @Query("select c from Contractor c left join fetch c.code co left join fetch c.proposals where co.code = ?1 and co.expirationDateTime > ?2")
  Optional<Contractor> getByCode(String code, LocalDateTime dateTime);

  @Query("select c from Contractor c left join fetch c.code where c.cardCode = '0'")
  Collection<Contractor> findAdmins();

}
