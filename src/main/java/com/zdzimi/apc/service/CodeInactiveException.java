package com.zdzimi.apc.service;

public class CodeInactiveException extends RuntimeException {

  public CodeInactiveException(String code) {
    super(String.format("Kod: %s jest nieaktywny.", code));
  }

}
