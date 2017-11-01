package com.github.funthomas424242.rades.project.generator;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.funthomas424242.rades.project.domain.RadesProject;
import org.jboss.forge.addon.resource.DirectoryResource;
import org.jboss.forge.addon.resource.FileResource;
import org.jboss.forge.addon.ui.input.UIPrompt;
import org.jboss.forge.addon.ui.output.UIOutput;

import java.io.*;

public class NewProjectReadmeFile {

    public void generate(final UIPrompt prompt, final UIOutput log, final DirectoryResource projectDir, final RadesProject radesProject) throws IOException {

        final FileResource<?> projectReadmeFile = projectDir.getChild("README.md").reify(FileResource.class);

        if (projectReadmeFile.exists()) {

            final boolean shouldOverride = prompt.promptBoolean("Override the README.md?", true);
            if (!shouldOverride) {
                log.info(log.out(), "Warning: Creating of project canceled!");
                return;
            } else {
                projectReadmeFile.delete();
            }
        }

        projectReadmeFile.refresh();
        boolean isCreated = projectReadmeFile.createNewFile();

        final String githubUsername = radesProject.getGithubUsername();
        final String githubRepositoryname = radesProject.getGithubRepositoryname();
        final String githubProjectDescription="Kurze Beschreibung des Projektes in einem Satz.";

        final OutputStream outStream = projectReadmeFile.getResourceOutputStream(false);
        final PrintWriter writer = new PrintWriter(outStream);
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

        writer.println("# "+githubRepositoryname);
        writer.println(githubProjectDescription);

        writer.flush();
        writer.close();
    }


}
