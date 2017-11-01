package com.github.funthomas424242.rades.project.generators;

import com.github.funthomas424242.rades.core.resources.NewFileResourceFactory;
import com.github.funthomas424242.rades.project.RadesProject;
import org.jboss.forge.addon.resource.DirectoryResource;
import org.jboss.forge.addon.resource.FileResource;
import org.jboss.forge.addon.ui.input.UIPrompt;
import org.jboss.forge.addon.ui.output.UIOutput;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;

public class NewProjectReadmeFileGenerator {

    public static final String README_MD = "README.md";

    public void generate(final UIPrompt prompt, final UIOutput log, final DirectoryResource projectDir, final RadesProject radesProject) throws IOException {

        final FileResource<?> readmeFile
                = new NewFileResourceFactory(prompt, log).newFileResource(projectDir, README_MD);
        if (!readmeFile.exists()) {
            return;
        }

        final String githubUsername = radesProject.getGithubUsername();
        final String githubRepositoryname = radesProject.getGithubRepositoryname();
        final String githubProjectDescription = "Kurze Beschreibung des Projektes in einem Satz.";
        final String bintrayUsername = radesProject.getBintrayUsername();
        final String bintrayRepositoryname = radesProject.getBintrayRepositoryname();
        final String bintrayPackagename = radesProject.getBintrayPackagename();
        final String projectDirname = radesProject.getProjectDirName();


        final OutputStream outStream = readmeFile.getResourceOutputStream(false);
        final PrintWriter writer = new PrintWriter(outStream);

        // Bintray Download
        writer.println("[![Download](https://api.bintray.com/packages/" + bintrayUsername +
                "/" + bintrayRepositoryname +
                "/" + bintrayPackagename +
                "/images/download.svg) ](https://bintray.com/" + bintrayUsername +
                "/" + bintrayRepositoryname +
                "/" + bintrayPackagename +
                "/_latestVersion)");
        // Travis CI Badged
        writer.println("[![Build Status](https://travis-ci.org/" + githubUsername +
                "/" + githubRepositoryname +
                ".svg?branch=master)](https://travis-ci.org/" + githubUsername +
                "/" + githubRepositoryname +
                ")");
        // Codedev Badged
        writer.println("[![codecov](https://codecov.io/gh/" + githubUsername +
                "/" + githubRepositoryname +
                "/branch/master/graph/badge.svg)](https://codecov.io/gh/" + githubUsername +
                "/" + githubRepositoryname +
                ")");
        // Waffle Badged
        writer.println("[![Waffle.io - Columns and their card count](https://badge.waffle.io/" + githubUsername +
                "/" + githubRepositoryname +
                ".svg?columns=all)](https://waffle.io/" + githubUsername +
                "/" + githubRepositoryname +
                ")");

        writer.println("# " + githubRepositoryname);
        writer.println(githubProjectDescription);

        writer.flush();
        writer.close();
    }


}
