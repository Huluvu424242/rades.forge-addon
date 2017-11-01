package com.github.funthomas424242.rades.project.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.github.funthomas424242.rades.validationrules.*;

import javax.validation.constraints.NotNull;
import java.util.Set;


//@RadesAbstractDomainobject
public abstract class RadesProject implements RadesProjectAccessor{


    // MAVEN Koordinaten TODO extract into separate class with builder
    @ProjectGroupId
    protected String groupID;

    @ProjectArtifactId
    protected String artifactID;

    @PackageClassifier
    protected String classifier;

    @ProjectVersion
    protected String version;

    @ProjectDirname
    protected String projectDirName;

    // Repositories
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @NotNull
    protected Set<Repository> repositories;

    // social account names TODO extract to separate class

    @GithubUsername
    protected String githubUsername;

    @GithubRepositoryname
    protected String githubRepositoryname;

    @BintrayUsername
    protected String bintrayUsername;

    @BintrayRepositoryname
    protected String bintrayRepositoryname;

    @BintrayPackagename
    protected String bintrayPackagename;









}
