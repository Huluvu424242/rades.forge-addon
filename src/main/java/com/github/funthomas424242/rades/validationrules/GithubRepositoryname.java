package com.github.funthomas424242.rades.validationrules;

import com.fasterxml.jackson.annotation.JacksonAnnotationsInside;
import com.fasterxml.jackson.annotation.JsonInclude;

import javax.validation.constraints.NotNull;
import java.lang.annotation.*;

@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@NotNull
@JsonInclude(JsonInclude.Include.NON_NULL)
@JacksonAnnotationsInside
public @interface GithubRepositoryname {

}
