package com.zdzimi.apc.data.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class BarcodeValidator implements ConstraintValidator<Barcode, String> {

  @Override
  public boolean isValid(String value, ConstraintValidatorContext context) {
    for (char sign : value.toCharArray()) {
      if (sign < '0' || sign > '9') {
        return false;
      }
    }
    return true;
  }

}
