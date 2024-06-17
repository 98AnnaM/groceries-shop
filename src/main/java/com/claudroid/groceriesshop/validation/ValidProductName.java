package com.claudroid.groceriesshop.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Constraint(validatedBy = ProductNameValidator.class)
public @interface ValidProductName {
    String message() default "This name is occupied.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}