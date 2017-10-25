package com.github.funthomas424242.rades.project.domain;

import com.fasterxml.jackson.annotation.JsonInclude;

public class RadesProject {

    protected String groupID;

    protected String artifactID;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    protected String classifier;

    protected String version;

    protected RadesProject(){};

}
