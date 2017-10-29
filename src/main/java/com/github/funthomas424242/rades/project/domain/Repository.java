package com.github.funthomas424242.rades.project.domain;

import com.fasterxml.jackson.annotation.JsonInclude;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;

public abstract class Repository implements  RepositoryAccessor{

    @NotNull
    protected String id;

    @NotNull
    protected String url;

    // maven, gradle, m2, ...
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @Null
    protected String type;

}
