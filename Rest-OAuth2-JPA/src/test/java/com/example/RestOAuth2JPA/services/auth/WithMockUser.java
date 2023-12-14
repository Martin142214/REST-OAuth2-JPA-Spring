package com.example.RestOAuth2JPA.services.auth;

import java.lang.annotation.Documented;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.lang.annotation.ElementType;
import java.lang.annotation.RetentionPolicy;

@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface WithMockUser {
    String value() default "user";

    String username() default "";

    String email() default "doctor1@abv.bg";

    String[] roles() default {"DOCTOR"};

    String password() default "doctor1";
}
