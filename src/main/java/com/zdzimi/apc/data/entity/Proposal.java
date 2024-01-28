package com.zdzimi.apc.data.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "propozycja")
@Getter
@Setter
public class Proposal {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;

  @Column(name = "kontrid")
  private long contractorId;

  @Column(name = "kodkreskowy")
  private String barcode;

  @Column(name = "datadodania")
  private LocalDateTime dateTime;

  @Column(name = "ilosc")
  private int amount;

  @Column(name = "zgodapowiadomienia")
  private boolean consentToNotification;

}
