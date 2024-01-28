package com.zdzimi.apc.data;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommodityProposal {

  private long contractorId;
  private String barcode;
  private int neededAmount;
  private boolean consentToNotification;

}
