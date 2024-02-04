package com.zdzimi.apc.data.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = BarcodeValidator.class)
@Documented
public @interface Barcode {

  String message() default "Kod kreskowy noże zawierać tylko cyfry";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};

}
