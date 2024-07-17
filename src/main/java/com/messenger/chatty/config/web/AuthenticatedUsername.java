package com.messenger.chatty.config.web;

import io.swagger.v3.oas.annotations.Parameter;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.PARAMETER)
@Parameter(hidden = true)
@Retention(RetentionPolicy.RUNTIME)
public @interface AuthenticatedUsername {

}
