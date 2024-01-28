package com.zdzimi.apc.service;

public class ContractorNotFoundException extends RuntimeException {

  public ContractorNotFoundException(String cardCode) {
    super(String.format("Podany kod: %s jest niepoprawny.", cardCode));
  }

}
