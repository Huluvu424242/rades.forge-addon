package com.github.funthomas424242.rades.project.commands;

import com.github.funthomas424242.rades.project.domain.RadesProject;
import com.github.funthomas424242.rades.project.domain.RadesProjectBuilder;
import com.github.funthomas424242.rades.project.generator.NewLibraryProjectGenerator;
import com.github.funthomas424242.rades.project.generator.NewProjectReadmeFileGenerator;
import com.github.funthomas424242.rades.project.generator.NewRadesProjectDescriptionFileGenerator;
import org.jboss.forge.addon.resource.DirectoryResource;
import org.jboss.forge.addon.resource.Resource;
import org.jboss.forge.addon.resource.ResourceFactory;
import org.jboss.forge.addon.ui.command.AbstractUICommand;
import org.jboss.forge.addon.ui.command.UICommand;
import org.jboss.forge.addon.ui.context.UIBuilder;
import org.jboss.forge.addon.ui.context.UIContext;
import org.jboss.forge.addon.ui.context.UIExecutionContext;
import org.jboss.forge.addon.ui.input.UIInput;
import org.jboss.forge.addon.ui.input.UIPrompt;
import org.jboss.forge.addon.ui.input.UISelectMany;
import org.jboss.forge.addon.ui.metadata.UICommandMetadata;
import org.jboss.forge.addon.ui.metadata.WithAttributes;
import org.jboss.forge.addon.ui.output.UIOutput;
import org.jboss.forge.addon.ui.result.Result;
import org.jboss.forge.addon.ui.result.Results;
import org.jboss.forge.addon.ui.util.Categories;
import org.jboss.forge.addon.ui.util.Metadata;

import javax.inject.Inject;
import java.io.File;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Callable;

public class RadesNewLibraryProject extends AbstractUICommand implements UICommand {

    protected static final List<String> MAVEN_REPO_LIST = Arrays.asList(
            "https://mvnrepository.com/artifact", "https://jcenter.bintray.com/");

    @Inject
    protected ResourceFactory resourceFactory;

    @Inject
    protected NewLibraryProjectGenerator newLibProjectGenerator;

    @Inject
    protected NewRadesProjectDescriptionFileGenerator newRadesProjectDescriptionFileGeneratorGenerator;

    @Inject
    protected NewProjectReadmeFileGenerator newProjectReadmeFileGeneratorGenerator;


    // /////////////////////////////////////////////////////////////////////////
    //
    // Definition of interactive inputs (parameters)
    //
    // /////////////////////////////////////////////////////////////////////////


    // Eine Pflichteingabe ohne Default ist notwendig um in den interaktiven Modus zu kommen
    @Inject
    @WithAttributes(label = "Group ID:", required = true, defaultValue = "com.github.myUsername")
    protected UIInput<String> groupId;

    @Inject
    @WithAttributes(label = "Artifact ID:", required = true, defaultValue = "testProject")
    protected UIInput<String> artifactId;

    @Inject
    @WithAttributes(label = "Version:", required = true, defaultValue = "1.0.0-SNAPSHOT")
    protected UIInput<String> version;

    @Inject
    @WithAttributes(label = "Projektverzeichnis:", required = true)
    protected UIInput<String> projectDirName;


    @Inject
    @WithAttributes(label = "Github Username:", required = true, defaultValue = "myGithubUsername")
    protected UIInput<String> githubUsername;

    @Inject
    @WithAttributes(label = "Github Repositoryname:", required = true)
    protected UIInput<String> githubRepositoryname;



    @Inject
    @WithAttributes(label = "Bintray Username:", required = true, defaultValue = "myBintrayUsername")
    protected UIInput<String> bintrayUsername;

    @Inject
    @WithAttributes(label = "Maven Repos", required = true, description = "Auswahl der zu verwendenden Maven Repositories")
    protected UISelectMany<String> repositories;

    @Override
    public UICommandMetadata getMetadata(UIContext context) {
        return Metadata.forCommand(RadesNewLibraryProject.class)
                .name("rades-new-libproject")
                .description("Creates a RADES lib project.")
                .category(Categories.create("Project/Generation"));
    }

    @Override
    public void initializeUI(UIBuilder builder) throws Exception {

        // Auswahlen initialisieren
        repositories.setValueChoices(MAVEN_REPO_LIST);
        projectDirName.setDefaultValue(
                new Callable<String>() {
                    @Override
                    public String call() {
                        if (artifactId.getValue() == null) {
                            return "myproject";
                        }
                        return artifactId.getValue();
                    }
                });

        githubRepositoryname.setDefaultValue(
                new Callable<String>() {
                    @Override
                    public String call() {
                        if (projectDirName.getValue() == null) {
                            return "myGithubReponame";
                        }
                        return projectDirName.getValue();
                    }
                });

        // add the inputs
        builder.add(groupId);
        builder.add(artifactId);
        builder.add(version);
        builder.add(projectDirName);
        builder.add(githubUsername);
        builder.add(githubRepositoryname);
        builder.add(bintrayUsername);
        builder.add(repositories);
    }

    @Override
    public Result execute(UIExecutionContext context) throws Exception {

        final UIOutput log = context.getUIContext().getProvider().getOutput();
        final UIPrompt prompt = context.getPrompt();

        final DirectoryResource projectDir;
         /* create projectFileResource reference */
        {
            final File curDirFile = Paths.get(".").toAbsolutePath().toFile();
            File parentFile = curDirFile.getParentFile();
            final Resource<File> parentDirResource = resourceFactory.create(parentFile);
            final DirectoryResource location = parentDirResource.reify(DirectoryResource.class);
            projectDir = location.getOrCreateChildDirectory(projectDirName.getValue());
        }

        // radesProject befüllen
        final RadesProject radesProject = new RadesProjectBuilder()
                .withGroupID(groupId.getValue())
                .withArtifactID(artifactId.getValue())
                .withVersion(version.getValue())
                .withProjectDirName(projectDirName.getValue())
                .withGithubUsername(githubUsername.getValue())
                .withGithubRepositoryname(githubRepositoryname.getValue())
                .build();


        // Actions
        log.info(log.out(), "Generiere RadesDescriptionfile: rades.json in "+projectDir.getName());
        newRadesProjectDescriptionFileGeneratorGenerator.generateProjectDescriptionFile(prompt, log, projectDir, radesProject);
        log.info(log.out(), "Generiere project facets such as pom.xml etc. in "+projectDir.getName());
        newLibProjectGenerator.generate(prompt, log, projectDir, radesProject);
        log.info(log.out(), "Generiere project README.md in "+projectDir.getName());
        newProjectReadmeFileGeneratorGenerator.generate(prompt, log, projectDir, radesProject);

        return Results
                .success("Command 'rades-new-libproject' successfully executed!");
    }


}