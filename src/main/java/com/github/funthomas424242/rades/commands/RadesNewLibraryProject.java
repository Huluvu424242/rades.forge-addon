package com.github.funthomas424242.rades.commands;

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
import org.jboss.forge.addon.projects.stacks.Stack;
import org.jboss.forge.addon.projects.stacks.StackFacet;
import org.jboss.forge.addon.resource.ResourceFactory;
import org.jboss.forge.addon.ui.command.AbstractUICommand;
import org.jboss.forge.addon.ui.command.UICommand;
import org.jboss.forge.addon.ui.context.UIBuilder;
import org.jboss.forge.addon.ui.context.UIContext;
import org.jboss.forge.addon.ui.context.UIExecutionContext;
import org.jboss.forge.addon.ui.input.UIInput;
import org.jboss.forge.addon.ui.input.UISelectMany;
import org.jboss.forge.addon.ui.input.UISelectOne;
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

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
    protected ProjectFactory projectFactory;

    @Inject
    protected MavenBuildSystem buildSystem;


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
    @WithAttributes(label = "Artifact ID:",  required = true, defaultValue = "test")
    protected UIInput<String> artifactId;

    @Inject
    @WithAttributes(label = "Version:", required = true, defaultValue = "1.0.0-SNAPSHOT")
    protected UIInput<String> version;

    @Inject
    @WithAttributes(label = "Bintray Username:", required = true, defaultValue = "funthomas424242")
    protected UIInput<String> bintrayUsername;

    @Inject
    @WithAttributes(label = "Maven Repos", required = true, description="Auswahl der zu verwendenden Maven Repositories")
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

        final String projectGroupId = groupId.getValue();

        final UIOutput log = context.getUIContext().getProvider().getOutput();


        final File dir = new File("testProject");
        dir.mkdirs();
        final Resource<File> projectDir = resourceFactory.create(dir);
//        log.info(log.out(), "Verwende als Projektverzeichnis " + projectDir);

        // final DirectoryResource location = projectDir.reify(
        // DirectoryResource.class).getOrCreateChildDirectory("test2");
        // System.out.println("Location directory" + location);

        List<Class<? extends ProjectFacet>> facets = new ArrayList<>();
        facets.add(ResourcesFacet.class);
        facets.add(MetadataFacet.class);
        facets.add(JavaSourceFacet.class);
        facets.add(JavaCompilerFacet.class);
        facets.add(MavenPluginFacet.class);
        facets.add(DependencyFacet.class);
        final Project project = projectFactory.createProject(projectDir,
                buildSystem, facets);

//        generateReadme(project);
//        generateLicense(project);

//        final Optional<Stack> metadata = project.getStack<ProjectFacet>();
        // add project coordinates
//        metadata.setProjectName(projectArtifactId);
//        metadata.setProjectGroupName(projectGroupId);
//        metadata.setProjectVersion(projectVersion);

        return Results
                .success("Command 'rades-new-libproject' successfully executed!");
    }
}