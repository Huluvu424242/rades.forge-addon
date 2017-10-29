package com.github.funthomas424242.rades.project.commands;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.funthomas424242.rades.project.domain.RadesProject;
import com.github.funthomas424242.rades.project.domain.RadesProjectBuilder;
import com.github.funthomas424242.rades.project.generator.NewLibraryProjectGenerator;
import org.jboss.forge.addon.maven.projects.MavenBuildSystem;
import org.jboss.forge.addon.maven.projects.MavenPluginFacet;
import org.jboss.forge.addon.parser.java.facets.JavaCompilerFacet;
import org.jboss.forge.addon.parser.java.facets.JavaSourceFacet;
import org.jboss.forge.addon.projects.Project;
import org.jboss.forge.addon.projects.ProjectFacet;
import org.jboss.forge.addon.projects.ProjectFactory;
import org.jboss.forge.addon.projects.facets.DependencyFacet;
import org.jboss.forge.addon.projects.facets.MetadataFacet;
import org.jboss.forge.addon.projects.facets.ResourcesFacet;
import org.jboss.forge.addon.resource.DirectoryResource;
import org.jboss.forge.addon.resource.FileResource;
import org.jboss.forge.addon.resource.ResourceFactory;
import org.jboss.forge.addon.resource.util.ResourceUtil;
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
import org.jboss.forge.addon.ui.util.Metadata;
import org.jboss.forge.addon.ui.util.Categories;
import org.jboss.forge.addon.ui.result.Result;
import org.jboss.forge.addon.ui.result.Results;
import org.jboss.forge.addon.resource.Resource;

import javax.inject.Inject;
import java.io.File;
import java.io.IOException;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RadesNewLibraryProject extends AbstractUICommand implements UICommand {


//    protected static final List<String> DEP_STARTER_LIST = Arrays.asList(
//            "spring-boot-starter-parent", "spring-boot-starter-batch",
//            "spring-boot-starter-jetty", "spring-boot-starter-tomcat",
//            "spring-boot-starter-logging", "spring-boot-starter-aop",
//            "spring-boot-starter-jpa", "spring-boot-starter-jetty-jdbc",
//            "spring-boot-starter-thymeleaf", "spring-boot-starter-web",
//            "spring-boot-starter-actuator", "spring-boot-starter-security",
//            "spring-boot-starter-test");


    protected static final List<String> MAVEN_REPO_LIST = Arrays.asList(
            "https://mvnrepository.com/artifact", "https://jcenter.bintray.com/");

    @Inject
    protected ResourceFactory resourceFactory;

    @Inject
    protected NewLibraryProjectGenerator libProjectGenerator;


    // /////////////////////////////////////////////////////////////////////////
    //
    // Definition of interactive inputs (parameters)
    //
    // /////////////////////////////////////////////////////////////////////////

//    @Inject
//    @WithAttributes(label = "Specific Name in spring-boot-starter-specificname:", required = true)
//    protected UIInput<String> specificName;

    // Eine Pflichteingabe ohne Default ist notwendig um in den interaktiven Modus zu kommen

    @Inject
    @WithAttributes(label = "Group ID:", required = true, defaultValue = "com.github.funthomas424242")
    protected UIInput<String> groupId;

    @Inject
    @WithAttributes(label = "Artifact ID:", required = true, defaultValue = "test")
    protected UIInput<String> artifactId;

    @Inject
    @WithAttributes(label = "Version:", required = true, defaultValue = "1.0.0-SNAPSHOT")
    protected UIInput<String> version;

    @Inject
    @WithAttributes(label = "Bintray Username:", required = true, defaultValue = "funthomas424242")
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

        // add the inputs
        builder.add(groupId);
        builder.add(artifactId);
        builder.add(version);
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
            projectDir = location.getOrCreateChildDirectory("testProject");
        }

        // Actions
        generateProjectDescriptionFile(prompt, log, projectDir);
        log.info(log.out(), "Generator:" + libProjectGenerator);
        libProjectGenerator.generate(prompt, log, projectDir);

        return Results
                .success("Command 'rades-new-libproject' successfully executed!");
    }

    // TODO extract to separate generator class
    protected void generateProjectDescriptionFile(final UIPrompt prompt, final UIOutput log, DirectoryResource projectDir) throws IOException {

        final FileResource<?> radesProjectFile = projectDir.getChild("rades.json").reify(FileResource.class);

        if (radesProjectFile.exists()) {

            final boolean shouldOverride = prompt.promptBoolean("Override the rades.json?", true);
            if (!shouldOverride) {
                log.info(log.out(), "Warning: Creating of project canceled!");
                return;
            } else {
                radesProjectFile.delete();
            }
        }

        radesProjectFile.refresh();
        boolean isCreated = radesProjectFile.createNewFile();

        // projektFile befüllen
        final String projectGroupId = groupId.getValue();
        final String projectArtifactId = artifactId.getValue();
        final String projectVersion = version.getValue();

        final RadesProject radesProject = new RadesProjectBuilder()
                .withGroupID(projectGroupId)
                .withArtifactID(projectArtifactId)
                .withClassifier(null)
                .withVersion(projectVersion)
                .build();
        final PipedOutputStream pipeOut = new PipedOutputStream();
        final PipedInputStream pipeIn = new PipedInputStream(pipeOut);
        final ObjectMapper objMapper = new ObjectMapper();
        objMapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
        objMapper.writer().writeValue(pipeOut, radesProject);
        radesProjectFile.setContents(pipeIn);
        pipeOut.flush();
        pipeOut.close();
        pipeIn.close();
    }

}