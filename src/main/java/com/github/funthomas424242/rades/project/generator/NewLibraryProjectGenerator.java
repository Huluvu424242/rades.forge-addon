package com.github.funthomas424242.rades.project.generator;

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
import org.jboss.forge.addon.resource.Resource;
import org.jboss.forge.addon.resource.ResourceFactory;
import org.jboss.forge.addon.ui.input.UIPrompt;
import org.jboss.forge.addon.ui.output.UIOutput;

import javax.inject.Inject;
import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystem;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.FileAttribute;
import java.util.ArrayList;
import java.util.List;

public class NewLibraryProjectGenerator {

    @Inject
    protected ResourceFactory resourceFactory;

    @Inject
    protected ProjectFactory projectFactory;

    @Inject
    protected MavenBuildSystem buildSystem;


    public void generate(final UIPrompt prompt, UIOutput log, final DirectoryResource projectDir) throws IOException {
        generateProject(prompt, log, projectDir);

    }


    protected void generateProject(final UIPrompt prompt, final UIOutput log, final DirectoryResource projectDir) throws IOException {

//        final DirectoryResource projectDir;
//         /* create projectFileResource reference */
//        {
//            final File projectDirFile = new File(Paths.get(".").toFile().getAbsolutePath());
//            File parentFile = projectDirFile.getParentFile();
//            parentFile = new File(parentFile.getParentFile(),parentFile.getName());
////            projectDirFile = new File(projectDirFile.getParentFile(),projectDirFile.getAbsolutePath());
//            log.info(log.out(), "parent:" + parentFile.getParent());
//            log.info(log.out(), "subDir:" + parentFile.getAbsolutePath());
//            log.info(log.out(), "isDir:" + parentFile.isDirectory());
//            log.info(log.out(), "resourceFactory:" + resourceFactory);
//
//
//            final Resource<File> parentDirResource = resourceFactory.create(parentFile);
//            final DirectoryResource location = parentDirResource.reify(DirectoryResource.class);
//            location.getOrCreateChildDirectory("testProject");
////
////            final Resource<File> projectDirResource = resourceFactory.create(parentFile);
////            projectDirResource.getOrCreateChildDirectory("test2");
////            projectDir = projectDirResource.reify(DirectoryResource.class);
//        }


        final List<Class<? extends ProjectFacet>> facets = new ArrayList<>();
        facets.add(ResourcesFacet.class);
        facets.add(MetadataFacet.class);
        facets.add(JavaSourceFacet.class);
        facets.add(JavaCompilerFacet.class);
        facets.add(MavenPluginFacet.class);
        facets.add(DependencyFacet.class);
        final Project project = projectFactory.createProject(projectDir,
                buildSystem, facets);

    }


    // final UIOutput log = context.getUIContext().getProvider().getOutput();
    //        log.info(log.out(), "Verwende als Projektverzeichnis " + projectDir);
    // final DirectoryResource location = projectDir.reify(
    // DirectoryResource.class).getOrCreateChildDirectory("test2");
    // System.out.println("Location directory" + location);
    //        generateReadme(project);
    //        generateLicense(project);
    //        final Optional<Stack> metadata = project.getStack<ProjectFacet>();
    // add project coordinates
    //        metadata.setProjectName(projectArtifactId);
    //        metadata.setProjectGroupName(projectGroupId);
    //        metadata.setProjectVersion(projectVersion);

//    protected void generateProject(final UIPrompt prompt, final UIOutput log) throws IOException {
//
//        final File dir = new File("testProject");
//        dir.mkdirs();
//        final Resource<File> projectDir = resourceFactory.create(dir);
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
//
//    }


}
