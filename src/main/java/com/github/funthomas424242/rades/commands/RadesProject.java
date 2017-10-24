package com.github.funthomas424242.rades.commands;

import com.fasterxml.jackson.annotation.JsonInclude;

public class RadesProject {

    protected String groupID;

    protected String artifactID;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    protected String classifier;

    protected String version;


    public RadesProject(String groupID, String artifactID, String classifier, String version) {
        this.groupID = groupID;
        this.artifactID = artifactID;
        this.classifier = classifier;
        this.version = version;
    }
}
