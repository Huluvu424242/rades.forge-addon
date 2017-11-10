package com.github.funthomas424242.rades.project.commands;

import com.github.funthomas424242.flowdesign.Integration;
import com.github.funthomas424242.rades.core.resources.UIResourceHelper;
import com.github.funthomas424242.rades.project.generators.NewLibraryProjectFacetsGenerator;
import com.github.funthomas424242.rades.project.generators.NewProjectReadmeFileGenerator;
import com.github.funthomas424242.rades.project.generators.NewTravisFileGenerator;
import com.github.funthomas424242.rades.project.validationrules.*;
import org.jboss.forge.addon.resource.DirectoryResource;
import org.jboss.forge.addon.resource.FileResource;
import org.jboss.forge.addon.resource.ResourceFactory;
import org.jboss.forge.addon.ui.command.AbstractUICommand;
import org.jboss.forge.addon.ui.command.CommandFactory;
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
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Callable;

public class RadesProjectNewCommand extends AbstractUICommand implements RadesUICommand {

    public static final String COMMAND_NAME = "rades-project-new";

    protected static final List<String> MAVEN_REPO_LIST = Arrays.asList(
            "https://mvnrepository.com/artifact", "https://jcenter.bintray.com/");


    @Inject
    CommandFactory commandFactory;

//    @Inject
//    CommandControllerFactory commandControllerFactory;

    @Inject
    protected ResourceFactory resourceFactory;

    @Inject
    protected NewLibraryProjectFacetsGenerator newLibProjectGenerator;


    @Inject
    protected NewProjectReadmeFileGenerator newProjectReadmeFileGeneratorGenerator;

    @Inject
    protected UIResourceHelper uiResourceHelper;

    @Inject
    protected NewTravisFileGenerator newTravisFileGenerator;




    // /////////////////////////////////////////////////////////////////////////
    //
    // Definition of interactive inputs (parameters)
    //
    // /////////////////////////////////////////////////////////////////////////


    // Eine Pflichteingabe ohne Default ist notwendig um in den interaktiven Modus zu kommen
    @Inject
    @WithAttributes(label = "Projektverzeichnis:", required = true)
    @ProjectDirname
    protected UIInput<String> projectDirName;


    @Inject
    @WithAttributes(label = "Github Username:", required = true, defaultValue = "myGithubUsername")
    @GithubUsername
    protected UIInput<String> githubUsername;

    @Inject
    @WithAttributes(label = "Github Repositoryname:", required = true)
    @GithubRepositoryname
    protected UIInput<String> githubRepositoryname;

    @Inject
    @WithAttributes(label = "Bintray Username:", defaultValue = "myBintrayUsername")
    @BintrayUsername
    protected UIInput<String> bintrayUsername;

    @Inject
    @WithAttributes(label = "Bintray Repositoryname:", defaultValue = "myBintrayRepositoryname")
    @BintrayRepositoryname
    protected UIInput<String> bintrayRepositoryname;

    @Inject
    @WithAttributes(label = "Bintray Packagename:", defaultValue = "myBintrayPackagename")
    @BintrayPackagename
    protected UIInput<String> bintrayPackagename;

    @Inject
    @WithAttributes(label = "Maven Repos", required = true, description = "Auswahl der zu verwendenden Maven Repositories")
    protected UISelectMany<String> repositories;

    @Override
    public UICommandMetadata getMetadata(UIContext context) {
        return Metadata.forCommand(RadesProjectNewCommand.class)
                .name(COMMAND_NAME)
                .description("Creates a RADES lib project.")
                .category(Categories.create(CATEGORY_RADES_PROJECT));
    }

    @Override
    public boolean isEnabled(UIContext context) {
        final boolean isEnabled = super.isEnabled(context);
        final FileResource radesProjectDescription = uiResourceHelper.getFileResourceFromCurrentDir(context, RADES_PROJECTDESCRIPTION_FILE);
        return isEnabled && !radesProjectDescription.exists();
    }

    @Override
    public void initializeUI(UIBuilder builder) throws Exception {

        builder.add(projectDirName);

        final UIContext uiContext = builder.getUIContext();

        final UICommand newProjectDescriptionfileCommand = commandFactory.getCommandByName(uiContext, RadesProjectNewDescriptionFileCommand.COMMAND_NAME);
        if (newProjectDescriptionfileCommand.isEnabled(uiContext)) {
            newProjectDescriptionfileCommand.initializeUI(builder);
        }
        // Auswahlen initialisieren
        repositories.setValueChoices(MAVEN_REPO_LIST);
//        projectDirName.setDefaultValue(
//                new Callable<String>() {
//                    @Override
//                    public String call() {
//                        if (artifactId.getValue() == null) {
//                            return "myproject";
//                        }
//                        return artifactId.getValue();
//                    }
//                });

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
        builder.add(githubUsername);
        builder.add(githubRepositoryname);
        builder.add(bintrayUsername);
        builder.add(bintrayRepositoryname);
        builder.add(bintrayPackagename);
        builder.add(repositories);
    }

    @Override
    @Integration
    public Result execute(UIExecutionContext context) throws Exception {

        final UIContext uiContext = context.getUIContext();
        final UIOutput log = uiContext.getProvider().getOutput();
        final UIPrompt prompt = context.getPrompt();

        final DirectoryResource projectDir = getProjectDirectory(uiContext);
        log.info(log.out(),"Projektdirectory:"+projectDir.getFullyQualifiedName());
        // Current Dir is project dir
        uiResourceHelper.setCurrentDirectoryTo(uiContext, projectDir);

//        // Create RadesProjectDescription
//        final RadesProject radesProject = new RadesProjectBuilder()
//                .withGroupID(groupId.getValue())
//                .withArtifactID(artifactId.getValue())
//                .withVersion(version.getValue())
//                .withProjectDescription("TODO: Kurze Beschreibung zum Projekt eintragen.")
//                .withProjectDirName(projectDirName.getValue())
//                .withGithubUsername(githubUsername.getValue())
//                .withGithubRepositoryname(githubRepositoryname.getValue())
//                .withBintrayUsername(bintrayUsername.getValue())
//                .withBintrayRepositoryname(bintrayRepositoryname.getValue())
//                .withBintrayPackagename(bintrayPackagename.getValue())
//                .build();
//
//        // Apply Generators
//        newLibProjectGenerator.generate(prompt, log, projectDir, radesProject);
//        newProjectReadmeFileGeneratorGenerator.generate(prompt, log, projectDir, radesProject);
//        newTravisFileGenerator.generate(prompt, log, projectDir, radesProject);

        final UICommand newProjectDescriptionfileCommand = commandFactory.getCommandByName(uiContext, RadesProjectNewDescriptionFileCommand.COMMAND_NAME);
        log.info(log.out(),"newProjectDescriptionfileCommand: "+newProjectDescriptionfileCommand);
        if (newProjectDescriptionfileCommand.isEnabled(uiContext)) {
            newProjectDescriptionfileCommand.execute(context);
        }


        final UICommand updateProjectCommand = commandFactory.getCommandByName(uiContext, RadesProjectUpdateCommand.COMMAND_NAME);
        if (updateProjectCommand.isEnabled(uiContext)) {
            updateProjectCommand.execute(context);
        }

        return Results
                .success("Kommando " + COMMAND_NAME + " wurde erfolgreich ausgeführt.");
    }

    @Integration
    protected DirectoryResource getProjectDirectory(UIContext uiContext) {
        final DirectoryResource currentDirectoryResource = uiResourceHelper.getCurrentDirectoryResource(uiContext);
        return currentDirectoryResource.getOrCreateChildDirectory(projectDirName.getValue());
    }


}