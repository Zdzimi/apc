package com.zdzimi.apc.data.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "istw")
public class Amount {

  @Id
  @Column(name = "towid")
  private long commodityId;

  @Column(name = "stanmag")
  private double amount;

}
