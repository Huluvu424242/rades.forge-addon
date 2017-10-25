package com.github.funthomas424242.rades.project.domain;

import org.apache.commons.lang3.StringUtils;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import java.util.HashSet;
import java.util.Set;

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

        final ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        final Validator validator = factory.getValidator();
        final Set<ConstraintViolation<RadesProject>> constraintViolations  =validator.validate(project);

        if (constraintViolations.size() > 0) {
            final Set<String> violationMessages = new HashSet<String>();

            for (ConstraintViolation<RadesProject> constraintViolation : constraintViolations) {
                violationMessages.add(constraintViolation.getPropertyPath() + ": " + constraintViolation.getMessage());
            }

            throw new RuntimeException("RadesProject is not valid:\n" + StringUtils.join(violationMessages, "\n"));
        }


        return project;
    }
}