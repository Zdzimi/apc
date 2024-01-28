package com.zdzimi.apc.service;

import com.zdzimi.apc.data.entity.Contractor;
import com.zdzimi.apc.data.repository.ContractorsRepository;
import java.time.LocalDateTime;
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
      contractor.setCode(codesService.createNewCode(contractor.getId()));
    } else {
      codesService.updateCode(contractor.getCode());
    }

    return contractor;
  }

  public Contractor getContractorByTenMinuteCode(String code) {
    return repository.getByTenMinuteCode(code, LocalDateTime.now().minusMinutes(10))
        .orElseThrow(() -> new CodeInactiveException(code));
  }

}
