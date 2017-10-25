package com.github.funthomas424242.rades.project.domain;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;

public class RadesProjectBuilder {

    @NotNull
    protected String groupID;
    @NotNull
    protected String artifactID;
    @Null
    protected String classifier;
    @NotNull
    protected String version;

    public RadesProjectBuilder() {
    }


    public RadesProjectBuilder withGroupID(final String groupID) {
        this.groupID = groupID;
        return this;
    }

    public RadesProjectBuilder withArtifactID(final String artifactID) {
        this.artifactID = artifactID;
        return this;
    }

    public RadesProjectBuilder withClassifier(final String classifier) {
        this.classifier = classifier;
        return this;
    }

    public RadesProjectBuilder withVersion(final String version) {
        this.version = version;
        return this;
    }

    public RadesProject build() {
        final RadesProject project = new RadesProject();
        project.groupID = this.groupID;
        project.artifactID = this.artifactID;
        project.classifier = this.classifier;
        project.version = this.version;
        return project;
    }
}