package com.zdzimi.apc.service;

import com.zdzimi.apc.data.CommodityProposal;
import com.zdzimi.apc.data.Summary;
import com.zdzimi.apc.data.entity.Proposal;
import com.zdzimi.apc.data.repository.ProposalsRepository;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProposalService {

  private final ProposalsRepository repository;

  public void saveProposal(CommodityProposal cp) {
    Proposal proposal = new Proposal();
    proposal.setContractorId(cp.getContractorId());
    proposal.setBarcode(cp.getBarcode());
    proposal.setDateTime(LocalDateTime.now());
    proposal.setAdminNotification(true);
    proposal.setConsentToNotification(cp.isConsentToNotification());
    repository.save(proposal);
  }

  public void delete(long id) {
    repository.delete(repository.findById(id).orElseThrow());
  }

  public Collection<Proposal> getLastMonthProposals() {
    return repository.findLastProposals(LocalDateTime.now().minusMonths(1));
  }

  public Collection<Summary> getLastSummary(Integer days) {
    Collection<Proposal> proposals = days == null ? repository.findAll() : repository.findLastProposals(LocalDateTime.now().minusDays(days));

    Map<String, Integer> map = new TreeMap<>();
    for (Proposal proposal : proposals) {
      if (map.containsKey(proposal.getBarcode())) {
        map.put(proposal.getBarcode(), map.get(proposal.getBarcode()) + 1);
      } else {
        map.put(proposal.getBarcode(), 1);
      }
    }

    List<Summary> result = new ArrayList<>();
    for (String barcode : map.keySet()) {
      Summary summary = new Summary();
      summary.setBarcode(barcode);
      summary.setAmount(map.get(barcode));
      result.add(summary);
    }
    result.sort((o1, o2) -> o2.getAmount() - o1.getAmount());
    return result;
  }

  public void deleteAll(Long contractorId) {
    repository.deleteAll(repository.findByContractorId(contractorId));
  }

  public Collection<Proposal> getAll(String barcode) {
    return repository.findByBarcode(barcode);
  }

}
