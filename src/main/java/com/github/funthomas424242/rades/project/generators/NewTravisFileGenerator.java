package com.github.funthomas424242.rades.project.generators;

import com.github.funthomas424242.rades.core.resources.FileResourceFactory;
import com.github.funthomas424242.rades.core.resources.UserVetoException;
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

        log.info(log.out(), "Generiere Travis-CI Steuerdatei " +
                TRAVIS_FILE_NAME +
                " im Ordner " + projectDir.getName());

        FileResource<?> travisFile = null;
        try {
            travisFile = new FileResourceFactory(prompt, log).createFileResourceInteractive
                    (projectDir, TRAVIS_FILE_NAME);
        } catch (UserVetoException e) {
            return;
        } finally {
            if (travisFile == null || !travisFile.exists()) {
                return;
            }
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
