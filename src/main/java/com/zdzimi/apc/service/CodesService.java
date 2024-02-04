package com.zdzimi.apc.service;

import com.zdzimi.apc.data.entity.Code;
import com.zdzimi.apc.data.entity.Contractor;
import com.zdzimi.apc.data.repository.CodesRepository;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CodesService {

  private final static int CONTRACTOR_EXPIRATION_TIME = 10;
  private final static int ADMIN_EXPIRATION_TIME = 30;

  private final CodesRepository repository;

  public Code createNewCode(Contractor contractor) {
    Code code = new Code();
    code.setContractorId(contractor.getId());
    return updateCode(contractor, code);
  }

  public Code updateCode(Contractor contractor, Code code) {
    LocalDateTime now = LocalDateTime.now();
    code.setExpirationDateTime(
        isAdmin(contractor) ? now.plusMinutes(ADMIN_EXPIRATION_TIME) : now.plusMinutes(CONTRACTOR_EXPIRATION_TIME)
    );
    code.setCode(createCode(code.getContractorId(), now));
    return repository.save(code);
  }

  private boolean isAdmin(Contractor contractor) {
    return contractor.getCardCode().equals("0");
  }

  private String createCode(long contractorId, LocalDateTime creationDateTime) {
    return Long.toHexString(contractorId) +
        Integer.toHexString(creationDateTime.getYear()) +
        Integer.toHexString(creationDateTime.getMonthValue()) +
        Integer.toHexString(creationDateTime.getDayOfMonth()) +
        Integer.toHexString(creationDateTime.getHour()) +
        Integer.toHexString(creationDateTime.getMinute()) +
        Integer.toHexString(creationDateTime.getSecond());
  }

  public void updateAdminExpirationDate(Code code) {
    code.setExpirationDateTime(LocalDateTime.now().plusMinutes(ADMIN_EXPIRATION_TIME));
    repository.save(code);
  }
}
