package com.github.funthomas424242.rades.project.domain;

import com.fasterxml.jackson.annotation.JsonInclude;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.Set;


//@RadesAbstractDomainobject
public abstract class RadesProject implements RadesProjectAccessor{


    // MAVEN Koordinaten TODO extract into separate class with builder
    @NotNull
    protected String groupID;
    @NotNull
    protected String artifactID;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    protected String classifier;

    @NotNull
    @Pattern(regexp=".+\\..+", message="Invalid version format!")
    protected String version;

    @NotNull
    protected String projectDirName;

    // Repositories
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @NotNull
    protected Set<Repository> repositories;

    // social account names TODO extract to separate class

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    protected String githubUsername;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    protected String githubRepositoryname;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    protected String bintrayUsername;











}
