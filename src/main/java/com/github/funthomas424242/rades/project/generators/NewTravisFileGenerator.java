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

public class NewTravisFileGenerator {

    final public static String TRAVIS_FILE_NAME = ".travis.yml";

    public void generate(final UIPrompt prompt, final UIOutput log, final DirectoryResource projectDir, final RadesProject radesProject) throws IOException {

        final FileResource<?> travisFile
                = new NewFileResourceFactory(prompt, log).newFileResource(projectDir, TRAVIS_FILE_NAME);
        if (!travisFile.exists()) {
            return;
        }

        final OutputStream outStream = travisFile.getResourceOutputStream(false);
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
