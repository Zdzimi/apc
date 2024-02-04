package com.zdzimi.apc.service;

import com.zdzimi.apc.data.entity.Contractor;
import com.zdzimi.apc.data.repository.ContractorsRepository;
import java.time.LocalDateTime;
import java.util.Collection;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ContractorsService {

  private final ContractorsRepository repository;
  private final CodesService codesService;

  public Contractor getContractorByCardCode(String cardCode) {
    Contractor contractor = repository.getByCardCode(cardCode)
        .orElseThrow(() -> new ContractorNotFoundException(cardCode));

    if (contractor.getCode() == null) {
      contractor.setCode(codesService.createNewCode(contractor));
    } else {
      codesService.updateCode(contractor, contractor.getCode());
    }

    return contractor;
  }

  public Contractor getContractorByCode(String code) {
    return repository.getByCode(code, LocalDateTime.now())
        .orElseThrow(() -> new CodeInactiveException(code));
  }

  public void updateAdminCode(Contractor contractor) {
    codesService.updateAdminExpirationDate(contractor.getCode());
  }

  public Collection<Contractor> findAdmins() {
    Collection<Contractor> admins = repository.findAdmins();

    for (Contractor admin : admins) {
      if (admin.getCode() == null) {
        admin.setCode(codesService.createNewCode(admin));
      } else {
        codesService.updateCode(admin, admin.getCode());
      }
    }

    return admins;
  }

}
