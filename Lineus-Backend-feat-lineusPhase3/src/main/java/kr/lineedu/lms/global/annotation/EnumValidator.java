package kr.lineedu.lms.global.annotation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import kr.lineedu.lms.global.annotation.impl.EnumValidatorImpl;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = EnumValidatorImpl.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface  EnumValidator {
    Class<? extends Enum<?>> enumClass();

    String message() default "must be any of the enum {enumClass} values";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
