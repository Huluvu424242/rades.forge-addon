package com.github.funthomas424242.rades.project.domain;

import com.fasterxml.jackson.annotation.JsonInclude;

import javax.validation.constraints.Pattern;

public class RadesProject {

    protected String groupID;

    protected String artifactID;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    protected String classifier;

    @Pattern(regexp=".+\\..+", message="Invalid version format!")
    protected String version;

    protected RadesProject(){};

}
