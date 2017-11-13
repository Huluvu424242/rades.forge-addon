package com.github.funthomas424242.rades.project.commands;

import com.github.funthomas424242.flowdesign.Integration;
import com.github.funthomas424242.rades.core.resources.UIResourceHelper;
import com.github.funthomas424242.rades.project.generators.NewLibraryProjectFacetsGenerator;
import com.github.funthomas424242.rades.project.generators.NewProjectReadmeFileGenerator;
import com.github.funthomas424242.rades.project.generators.NewTravisFileGenerator;
import com.github.funthomas424242.rades.project.validationrules.ProjectDirname;
import org.jboss.forge.addon.resource.DirectoryResource;
import org.jboss.forge.addon.resource.FileResource;
import org.jboss.forge.addon.ui.command.CommandFactory;
import org.jboss.forge.addon.ui.command.UICommand;
import org.jboss.forge.addon.ui.context.UIBuilder;
import org.jboss.forge.addon.ui.context.UIContext;
import org.jboss.forge.addon.ui.context.UIExecutionContext;
import org.jboss.forge.addon.ui.input.UIInput;
import org.jboss.forge.addon.ui.input.UIPrompt;
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

public class ProjectNewCommand extends AbstractProjectUICommand {

    public static final String COMMAND_NAME = "rades-project-new";

//    protected static final List<String> MAVEN_REPO_LIST = Arrays.asList(
//            "https://mvnrepository.com/artifact", "https://jcenter.bintray.com/");


    @Inject
    CommandFactory commandFactory;

//    @Inject
//    CommandControllerFactory commandControllerFactory;

    @Inject
    protected NewLibraryProjectFacetsGenerator newLibProjectGenerator;


//    @Inject
//    protected NewProjectReadmeFileGenerator newProjectReadmeFileGeneratorGenerator;

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


    @Override
    public UICommandMetadata getMetadata(UIContext context) {
        return Metadata.forCommand(ProjectNewCommand.class)
                .name(COMMAND_NAME)
                .description("Creates a RADES lib project.")
                .category(Categories.create(CATEGORY_RADES_PROJECT));
    }

    @Override
    public boolean isEnabled(UIContext context) {
        final boolean isEnabled = super.isEnabled(context);
        final FileResource radesProjectDescription = getRadesProjectDescriptionfileAsResource(context);
        return isEnabled && !radesProjectDescription.exists();
    }

    @Override
    public void initializeUI(UIBuilder builder) throws Exception {

        builder.add(projectDirName);

        final UIContext uiContext = builder.getUIContext();

        final UICommand updateRadesCommand = commandFactory.getCommandByName(uiContext, ProjectUpdateRadesCommand.COMMAND_NAME);
        if (updateRadesCommand.isEnabled(uiContext)) {
            updateRadesCommand.initializeUI(builder);
        }

        final UICommand updateGithubCommand = commandFactory.getCommandByName(uiContext, ProjectUpdateGithubCommand.COMMAND_NAME);
//        if (updateGithubCommand.isEnabled(uiContext)) {
        updateGithubCommand.initializeUI(builder);
//        }


        final UICommand updateBintrayCommand = commandFactory.getCommandByName(uiContext, ProjectUpdateBintrayCommand.COMMAND_NAME);
//        if (updateBintrayCommand.isEnabled(uiContext)) {
        updateBintrayCommand.initializeUI(builder);
//        }


        // Auswahlen initialisieren
//        repositories.setValueChoices(MAVEN_REPO_LIST);

//        githubRepositoryname.setDefaultValue(
//                new Callable<String>() {
//                    @Override
//                    public String call() {
//                        if (projectDirName.getValue() == null) {
//                            return "myGithubReponame";
//                        }
//                        return projectDirName.getValue();
//                    }
//                });

        // add the inputs
//        builder.add(githubUsername);
//        builder.add(githubRepositoryname);
//        builder.add(bintrayUsername);
//        builder.add(bintrayRepositoryname);
//        builder.add(bintrayPackagename);
//        builder.add(repositories);
    }

    @Override
    @Integration
    public Result execute(UIExecutionContext context) throws Exception {

        final UIContext uiContext = context.getUIContext();
        final UIOutput log = uiContext.getProvider().getOutput();
        final UIPrompt prompt = context.getPrompt();

        setProjectDirToSubdirectory(uiContext, log, projectDirName.getValue());

        final UICommand updateRadesCommand = commandFactory.getCommandByName(uiContext, ProjectUpdateRadesCommand.COMMAND_NAME);
        log.info(log.out(), "updateRadesCommand: " + updateRadesCommand);
        if (updateRadesCommand.isEnabled(uiContext)) {
            updateRadesCommand.execute(context);
        }

        final UICommand updateGithubCommand = commandFactory.getCommandByName(uiContext, ProjectUpdateGithubCommand.COMMAND_NAME);
        log.info(log.out(), "updateGithubCommand: " + updateGithubCommand);
        if (updateGithubCommand.isEnabled(uiContext)) {
            updateGithubCommand.execute(context);
        }

        final UICommand updateBintrayCommand = commandFactory.getCommandByName(uiContext, ProjectUpdateBintrayCommand.COMMAND_NAME);
        log.info(log.out(), "updateBintrayCommand: " + updateBintrayCommand);
        if (updateBintrayCommand.isEnabled(uiContext)) {
            updateBintrayCommand.execute(context);
        }

        final UICommand updateProjectCommand = commandFactory.getCommandByName(uiContext, ProjectUpdateCommand.COMMAND_NAME);
        log.info(log.out(), "updateProjectCommand: " + updateProjectCommand);
        if (updateProjectCommand.isEnabled(uiContext)) {
            updateProjectCommand.execute(context);
        }


        return Results
                .success("Kommando " + COMMAND_NAME + " wurde erfolgreich ausgeführt.");
    }


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


}