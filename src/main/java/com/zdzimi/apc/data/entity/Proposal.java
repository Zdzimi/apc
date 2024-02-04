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

  @Column(name = "powiadomienieadmina")
  private boolean adminNotification;

  @Column(name = "zgodapowiadomienia")
  private boolean consentToNotification;

  @ManyToOne
  @JoinColumn(name = "kontrid", updatable = false, insertable = false)
  private Contractor contractor;

}
