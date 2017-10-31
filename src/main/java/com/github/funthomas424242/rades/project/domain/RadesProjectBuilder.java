package com.github.funthomas424242.rades.project.domain;

import org.apache.commons.lang3.StringUtils;

import javax.annotation.Generated;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.HashSet;
import java.util.Set;

@Generated("com.github.funthomas424242.rades.builder.annotation.RadesAbstractDomainobject")
class RadesProjectImpl extends RadesProject {

    @Override
    public String getGroupID() {
        return groupID;
    }

    @Override
    public String getArtifactID() {
        return artifactID;
    }

    @Override
    public String getClassifier() {
        return classifier;
    }

    @Override
    public String getVersion() {
        return version;
    }

    @Override
    public String getProjectDirName() {
        return projectDirName;
    }
}

@Generated("com.github.funthomas424242.rades.builder.annotation.RadesAbstractDomainobject")
public class RadesProjectBuilder {

    protected String groupID;
    protected String artifactID;
    protected String classifier;
    protected String version;
    protected String projectDirName;
    final protected Set<Repository> repositories;

    public RadesProjectBuilder() {
        repositories = new HashSet<Repository>();
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

    public RadesProjectBuilder withProjectDirName(final String projectDirName) {
        this.projectDirName = projectDirName;
        return this;
    }

    public RadesProject build() {
        final RadesProject radesProject = new RadesProjectImpl();
        radesProject.groupID = this.groupID;
        radesProject.artifactID = this.artifactID;
        radesProject.classifier = this.classifier;
        radesProject.version = this.version;
        radesProject.projectDirName = this.projectDirName;
        radesProject.repositories = this.repositories;

        final ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        final Validator validator = factory.getValidator();
        final Set<ConstraintViolation<RadesProject>> constraintViolations = validator.validate(radesProject);

        if (constraintViolations.size() > 0) {
            final Set<String> violationMessages = new HashSet<String>();

            for (ConstraintViolation<RadesProject> constraintViolation : constraintViolations) {
                violationMessages.add(constraintViolation.getPropertyPath() + ": " + constraintViolation.getMessage());
            }

            throw new RuntimeException("RadesProject is not valid:\n" + StringUtils.join(violationMessages, "\n"));
        }


        return radesProject;
    }
}