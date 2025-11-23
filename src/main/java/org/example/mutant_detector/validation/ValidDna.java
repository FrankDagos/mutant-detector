package org.example.mutant_detector.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = DnaValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidDna {
    String message() default "Secuencia de ADN inválida (debe ser NxN y solo contener A, T, C, G)";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
