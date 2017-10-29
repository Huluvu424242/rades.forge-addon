package com.github.funthomas424242.rades.project.domain;

import com.fasterxml.jackson.annotation.JsonInclude;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import javax.validation.constraints.Pattern;


//@RadesAbstractDomainobject
public abstract class RadesProject implements RadesProjectAccessor{

    @NotNull
    protected String groupID;
    @NotNull
    protected String artifactID;

    @Null
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    protected String classifier;

    @NotNull
    @Pattern(regexp=".+\\..+", message="Invalid version format!")
    protected String version;


}
