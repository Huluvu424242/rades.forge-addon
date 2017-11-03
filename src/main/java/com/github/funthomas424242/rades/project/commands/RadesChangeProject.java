package com.github.funthomas424242.rades.project.commands;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.*;
import com.github.funthomas424242.rades.flowdesign.Integration;
import com.github.funthomas424242.rades.project.RadesProject;
import com.github.funthomas424242.rades.project.RadesProjectBuilder;
import com.github.funthomas424242.rades.validationrules.ProjectDescription;
import org.jboss.forge.addon.environment.Environment;
import org.jboss.forge.addon.shell.*;
import org.jboss.forge.addon.maven.projects.MavenBuildSystem;
import org.jboss.forge.addon.projects.ProjectFactory;
import org.jboss.forge.addon.projects.facets.MetadataFacet;
import org.jboss.forge.addon.resource.DirectoryResource;
import org.jboss.forge.addon.resource.FileResource;
import org.jboss.forge.addon.resource.Resource;
import org.jboss.forge.addon.resource.ResourceFactory;
import org.jboss.forge.addon.shell.command.PwdCommand;
import org.jboss.forge.addon.ui.command.AbstractUICommand;
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
import java.io.File;
import java.nio.charset.Charset;
import java.nio.file.Paths;

public class RadesChangeProject extends AbstractUICommand implements UICommand {

    public static final String COMMANDLINE_COMMAND = "rades-change-project";

    @Inject
    protected ResourceFactory resourceFactory;

    @Inject
    protected ProjectFactory projectFactory;

    @Inject
    protected MavenBuildSystem buildSystem;

    @Inject
    protected Environment environment;


    @Inject
    @WithAttributes(label = "Projektverzeichnis:", required = true)
    @ProjectDescription
    protected UIInput<String> projectDescription;

    @Override
    public UICommandMetadata getMetadata(UIContext context) {
        return Metadata.forCommand(RadesNewLibraryProject.class)
                .name(COMMANDLINE_COMMAND)
                .description("Change a RADES project.")
                .category(Categories.create("Project/Generation"));
    }


    @Override
    public void initializeUI(UIBuilder builder) throws Exception {

    }


    @Override
    @Integration
    public Result execute(UIExecutionContext context) throws Exception {

        final UIOutput log = context.getUIContext().getProvider().getOutput();
        final UIPrompt prompt = context.getPrompt();

        final Shell myShell= (Shell) context.getUIContext().getProvider();
        final org.jboss.aesh.io.Resource resource=myShell.getConsole().getAeshContext().getCurrentWorkingDirectory();
        final String currentDir = resource.getAbsolutePath();
        log.info(log.out(),"###curDir:"+currentDir);


        final FileResource radesProjectDescriptionFile;
         /* create projectFileResource reference */
        {
            final File curDirFile = new File(currentDir);
//            final File parentFile = curDirFile.getParentFile();
            final Resource<File> parentDirResource = resourceFactory.create(curDirFile);
            final DirectoryResource currentDirectory = parentDirResource.reify(DirectoryResource.class);
            final Resource<?> radesProjectDescriptionFileResource = currentDirectory.getChild("rades.json");
            radesProjectDescriptionFile = radesProjectDescriptionFileResource.reify(FileResource.class);
        }

        final String jsonTxt = radesProjectDescriptionFile.getContents(Charset.forName("UTF-8"));
        final RadesProject radesProject = new ObjectMapper().readValue(jsonTxt, RadesProjectBuilder.RadesProjectImpl.class);
        log.info(log.out(),"JSON:"+radesProject.toString());


//        // radesProject befüllen
//        final RadesProject radesProject = new RadesProjectBuilder()
//                .withGroupID(groupId.getValue())
//                .withArtifactID(artifactId.getValue())
//                .withVersion(version.getValue())
//                .withProjectDirName(projectDirName.getValue())
//                .withGithubUsername(githubUsername.getValue())
//                .withGithubRepositoryname(githubRepositoryname.getValue())
//                .withBintrayUsername(bintrayUsername.getValue())
//                .withBintrayRepositoryname(bintrayRepositoryname.getValue())
//                .withBintrayPackagename(bintrayPackagename.getValue())
//                .build();
//
//
//
//
//
//        final List<Class<? extends ProjectFacet>> facets = new ArrayList<>();
//        facets.add(ResourcesFacet.class);
//        facets.add(MetadataFacet.class);
//        facets.add(JavaSourceFacet.class);
//        facets.add(JavaCompilerFacet.class);
//        facets.add(MavenPluginFacet.class);
//        facets.add(DependencyFacet.class);
//        final Project project = projectFactory.createProject(projectDir,
//                buildSystem, facets);

//        final MetadataFacet facet = project.getFacet(MetadataFacet.class);
//        facet.setProjectGroupName(radesProject.getGroupID());
//        facet.setProjectName(radesProject.getArtifactID());
//        facet.setProjectVersion(radesProject.getVersion());


//        final boolean shouldOverride = prompt.promptBoolean("Soll ich bestehende Datei " + fileName + " überschreiben?", true);
//        if (!shouldOverride) {
//            log.warn(log.out(), "Erstellung der Datei " + fileName + " auf Nutzerwunsch abgebrochen.");
//            log.warn(log.out(), "Datei " + fileName + " im Ordner " + parentDirectory.getName() + " muss manuell angepasst werden!");
//            return fileResource;
//        } else {
//            fileResource.delete();
//            fileResource.refresh();
//        }


        return Results
                .success("Kommando '" + COMMANDLINE_COMMAND + "' wurde erfolgreich ausgeführt.");
    }
}
