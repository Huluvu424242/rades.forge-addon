package com.github.funthomas424242.rades.project;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.github.funthomas424242.rades.project.RepositoryDescriptionAccessor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;

public abstract class RepositoryDescription implements RepositoryDescriptionAccessor {

    @NotNull
    protected String id;

    @NotNull
    protected String url;

    // maven, gradle, m2, ...
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @Null
    protected String type;

}
