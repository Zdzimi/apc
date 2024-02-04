package com.zdzimi.apc.data;

import com.zdzimi.apc.data.validation.Barcode;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommodityProposal {

  private long contractorId;
  @Barcode
  @NotBlank(message = "Podaj kod kreskowy")
  private String barcode;
  private boolean consentToNotification;

}
