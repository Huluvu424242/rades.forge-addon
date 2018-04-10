package com.github.funthomas424242.rades.project;

import javax.annotation.Generated;

@Generated("com.github.funthomas424242.rades.builder.annotation.RadesAbstractDomainobject")
public class RadesProjectAccessor {


    final protected RadesProject radesProject;

    public RadesProjectAccessor(final RadesProject radesProject){
        this.radesProject=radesProject;
    }

    public String getGroupID(){
        return radesProject.groupID;
    }

    public String getArtifactID(){
        return radesProject.artifactID;
    }

    String getClassifier(){
        return radesProject.classifier;
    }

    public String getVersion(){
        return radesProject.version;
    }

    public String getProjectDescription(){
        return radesProject.projectDescription;
    }

    public String getProjectDirName(){
        return radesProject.projectDirName;
    }

    public String getGithubUsername(){
        return radesProject.githubUsername;
    }

    public String getGithubRepositoryname(){
        return radesProject.githubRepositoryname;
    }

    public String getBintrayUsername(){
        return radesProject.bintrayUsername;
    }

    public String getBintrayRepositoryname(){
        return radesProject.bintrayRepositoryname;
    }

    public String getBintrayPackagename(){
        return radesProject.bintrayPackagename;
    }


}
