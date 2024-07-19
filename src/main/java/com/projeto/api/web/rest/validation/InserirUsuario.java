package com.projeto.api.web.rest.validation;


import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


@Constraint(validatedBy = InserirUsuarioValidator.class)
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface InserirUsuario {
    String message() default "Erro na criação do usuario";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}