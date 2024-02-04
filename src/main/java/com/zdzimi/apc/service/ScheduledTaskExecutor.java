package com.zdzimi.apc.service;

import com.zdzimi.apc.data.entity.Contractor;
import com.zdzimi.apc.data.entity.Proposal;
import com.zdzimi.apc.data.repository.ProposalsRepository;
import java.util.Collection;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ScheduledTaskExecutor {

  private final static long FIXED_RATE = 600_000;

  private final ProposalsRepository proposalsRepository;
  private final ContractorsService contractorsService;
  private final CommodityService commodityService;
  private final EmailService emailService;

  @Scheduled(fixedRate = FIXED_RATE)
  public void sendMailsToAdmin() {
    Collection<Proposal> proposals = proposalsRepository.findAllToSendToAdmins();
    if (!proposals.isEmpty()) {
      Collection<Contractor> admins = contractorsService.findAdmins();
      for (Contractor admin : admins) {
        emailService.sendMailsToAdmin(admin, proposals);
      }
      for (Proposal proposal : proposals) {
        proposal.setAdminNotification(false);
        proposalsRepository.save(proposal);
      }
    }
  }

  @Scheduled(fixedRate = FIXED_RATE)
  public void checkIfDelivered() {
    Collection<Proposal> proposals = proposalsRepository.findAllWaiting();
    for (Proposal proposal : proposals) {
      if (isDelivered(proposal)) {
        emailService.sendAlreadyDeliveredMail(proposal);
        proposal.setConsentToNotification(false);
        proposalsRepository.save(proposal);
      }
    }
  }

  private boolean isDelivered(Proposal proposal) {
    return commodityService.checkProductAvailability(proposal.getBarcode()).isPresent();
  }

}
