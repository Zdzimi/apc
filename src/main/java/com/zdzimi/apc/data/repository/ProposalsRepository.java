package com.zdzimi.apc.data.repository;

import com.zdzimi.apc.data.entity.Proposal;
import java.time.LocalDateTime;
import java.util.Collection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ProposalsRepository extends JpaRepository<Proposal, Long> {

  @Query("select p from Proposal p join fetch p.contractor where p.dateTime >= ?1 order by p.dateTime desc")
  Collection<Proposal> findLastProposals(LocalDateTime dateTime);

  Collection<Proposal> findByContractorId(Long contractorId);

  @Query("select p from Proposal p join fetch p.contractor where p.barcode = ?1 order by p.dateTime desc")
  Collection<Proposal> findByBarcode(String barcode);

  @Query("select p from Proposal p join fetch p.contractor where p.adminNotification = true")
  Collection<Proposal> findAllToSendToAdmins();

  @Query("select p from Proposal p join fetch p.contractor where p.consentToNotification = true")
  Collection<Proposal> findAllWaiting();

}
