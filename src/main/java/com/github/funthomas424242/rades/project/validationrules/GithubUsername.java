package com.github.funthomas424242.rades.project.validationrules;

import com.fasterxml.jackson.annotation.JacksonAnnotationsInside;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.lang.annotation.*;

@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@JsonInclude(JsonInclude.Include.NON_NULL)
@JacksonAnnotationsInside
public @interface GithubUsername {

}
