package com.zdzimi.apc.data.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "kontrahentkod")
public class Code {

  @Id
  @Column(name = "kontrid")
  private long contractorId;

  @Column(name = "kod")
  private String code;

  @Column(name = "datakodu")
  private LocalDateTime creationDateTime;

}
