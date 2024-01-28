package com.zdzimi.apc.data.entity;

import jakarta.persistence.*;
import java.util.Collection;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "kontrahent")
public class Contractor {

  @Id
  @Column(name = "kontrid")
  private long id;

  @Column(name = "nazwa")
  private String name;

  @Column(name = "email")
  private String email;

  @Column(name = "kodkarty")
  private String cardCode;

  @OneToOne
  @JoinColumn(name = "kontrid")
  private Code code;

  @OneToMany
  @JoinColumn(name = "kontrid")
  private Collection<Proposal> proposals;

}
