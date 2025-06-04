package com.sfk.hodolog.config;

import org.springframework.security.test.context.support.WithSecurityContext;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@WithSecurityContext(factory = HodologMockSecurityContext.class)
public @interface HodologMockUser {

    String name() default "user";

    String email() default "a@gmail.com";

    String password() default "";

    String role() default "ROLE_ADMIN";
}
