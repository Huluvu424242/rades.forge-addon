package com.github.funthomas424242.rades.project.generator;

import com.github.funthomas424242.rades.project.domain.RadesProject;
import org.jboss.forge.addon.resource.DirectoryResource;
import org.jboss.forge.addon.resource.FileResource;
import org.jboss.forge.addon.ui.input.UIPrompt;
import org.jboss.forge.addon.ui.output.UIOutput;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;

public class NewTravisFileGenerator {

    final public static String TRAVIS_FILE_NAME=".travis.yml";
    final public static String CANCEL_PROJECT_GENERATION= "Warning: Creating of project canceled!";

    public void generate(final UIPrompt prompt, final UIOutput log, final DirectoryResource projectDir, final RadesProject radesProject) throws IOException {

        final FileResource<?> projectReadmeFile = projectDir.getChild(TRAVIS_FILE_NAME).reify(FileResource.class);

        if (projectReadmeFile.exists()) {

            final boolean shouldOverride = prompt.promptBoolean("Override the "+TRAVIS_FILE_NAME+"?", true);
            if (!shouldOverride) {

                log.info(log.out(), CANCEL_PROJECT_GENERATION);
                return;
            } else {
                projectReadmeFile.delete();
            }
        }

        projectReadmeFile.refresh();
        boolean isCreated = projectReadmeFile.createNewFile();


        final OutputStream outStream = projectReadmeFile.getResourceOutputStream(false);
        final PrintWriter writer = new PrintWriter(outStream);

        // Java Language
        writer.println("language: java");
        writer.println();
        writer.println("jdk:\n" +
                "  - oraclejdk8\n" +
                "  #- openjdk9");
        writer.println();
        writer.println("script: \"mvn clean install\"");
        writer.println();
        writer.println("after_success:\n" +
                "  - bash <(curl -s https://codecov.io/bash)");

        writer.flush();
        writer.close();
    }


}
