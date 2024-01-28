package com.zdzimi.apc.service;

import com.zdzimi.apc.data.entity.Code;
import com.zdzimi.apc.data.repository.CodesRepository;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CodesService {

  private final CodesRepository repository;

  public Code createNewCode(long id) {
    Code code = new Code();
    code.setContractorId(id);
    return updateCode(code);
  }

  public Code updateCode(Code code) {
    code.setCreationDateTime(LocalDateTime.now());
    code.setCode(createCode(code.getContractorId(), code.getCreationDateTime()));
    return repository.save(code);
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

}
