package com.github.funthomas424242.rades.project;

import org.apache.commons.lang3.StringUtils;

import javax.annotation.Generated;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.HashSet;
import java.util.Set;


@Generated("com.github.funthomas424242.rades.builder.annotation.RadesAbstractDomainobject")
public class RadesProjectBuilder {

    protected String groupID;
    protected String artifactID;
    protected String classifier;
    protected String version;
    protected String projectDescription;
    protected String projectDirName;
    protected String githubUsername;
    protected String githubRepositoryname;
    protected String bintrayUsername;
    protected String bintrayRepositoryname;
    protected String bintrayPackagename;
    final protected Set<RepositoryDescription> repositories;

    public RadesProjectBuilder() {
        repositories = new HashSet<RepositoryDescription>();
    }

    public RadesProjectBuilder(final RadesProject radesProject) {
        this();
        this.groupID = radesProject.groupID;
        this.artifactID = radesProject.artifactID;
        this.classifier = radesProject.classifier;
        this.version = radesProject.version;
        this.projectDescription = radesProject.projectDescription;
        this.projectDirName = radesProject.projectDirName;
        this.githubUsername = radesProject.githubUsername;
        this.githubRepositoryname = radesProject.githubRepositoryname;
        this.bintrayUsername = radesProject.bintrayUsername;
        this.bintrayRepositoryname = radesProject.bintrayRepositoryname;
        this.bintrayPackagename = radesProject.bintrayPackagename;
        if (radesProject.repositories != null && !radesProject.repositories.isEmpty()) {
            this.repositories.addAll(radesProject.repositories);
        }
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

    public RadesProjectBuilder withProjectDescription(final String projectDescription) {
        this.projectDescription = projectDescription;
        return this;
    }

    public RadesProjectBuilder withProjectDirName(final String projectDirName) {
        this.projectDirName = projectDirName;
        return this;
    }

    public RadesProjectBuilder withGithubUsername(final String githubUsername) {
        this.githubUsername = githubUsername;
        return this;
    }

    public RadesProjectBuilder withGithubRepositoryname(final String githubRepositoryname) {
        this.githubRepositoryname = githubRepositoryname;
        return this;
    }

    public RadesProjectBuilder withBintrayUsername(final String bintrayUsername) {
        this.bintrayUsername = bintrayUsername;
        return this;
    }

    public RadesProjectBuilder withBintrayRepositoryname(final String bintrayRepositoryname) {
        this.bintrayRepositoryname = bintrayRepositoryname;
        return this;
    }

    public RadesProjectBuilder withBintrayPackagename(final String bintrayPackagename) {
        this.bintrayPackagename = bintrayPackagename;
        return this;
    }

    public RadesProject build() {
        final RadesProject radesProject = new RadesProjectImpl();
        radesProject.groupID = this.groupID;
        radesProject.artifactID = this.artifactID;
        radesProject.classifier = this.classifier;
        radesProject.version = this.version;
        radesProject.projectDescription = this.projectDescription;
        radesProject.projectDirName = this.projectDirName;
        radesProject.githubUsername = this.githubUsername;
        radesProject.githubRepositoryname = this.githubRepositoryname;
        radesProject.bintrayUsername = this.bintrayUsername;
        radesProject.bintrayRepositoryname = this.bintrayRepositoryname;
        radesProject.bintrayPackagename = this.bintrayPackagename;
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


    @Generated("com.github.funthomas424242.rades.builder.annotation.RadesAbstractDomainobject")
    static public class RadesProjectImpl extends RadesProject {

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
        public String getProjectDescription() {
            return projectDescription;
        }

        @Override
        public String getProjectDirName() {
            return projectDirName;
        }

        @Override
        public String getGithubUsername() {
            return githubUsername;
        }

        @Override
        public String getGithubRepositoryname() {
            return githubRepositoryname;
        }

        public String getBintrayUsername() {
            return bintrayUsername;
        }

        public String getBintrayRepositoryname() {
            return bintrayRepositoryname;
        }

        public String getBintrayPackagename() {
            return bintrayPackagename;
        }

    }
}