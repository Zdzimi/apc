package com.zdzimi.apc.data.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "towar")
public class Commodity {

  @Id
  @Column(name = "towid")
  private long id;

  @Column(name = "nazwa")
  private String name;

  @Column(name = "kod")
  private String code;

  @OneToOne
  @JoinColumn(name = "towid")
  private Amount amount;

}
