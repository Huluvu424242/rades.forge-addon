package com.github.funthomas424242.rades.project.generators;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.funthomas424242.rades.core.resources.NewFileResourceFactory;
import com.github.funthomas424242.rades.core.resources.UserVetoException;
import com.github.funthomas424242.rades.project.RadesProject;
import org.jboss.forge.addon.resource.DirectoryResource;
import org.jboss.forge.addon.resource.FileResource;
import org.jboss.forge.addon.ui.input.UIPrompt;
import org.jboss.forge.addon.ui.output.UIOutput;

import java.io.IOException;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;

public class NewRadesProjectDescriptionFileGenerator {

    public static final String RADES_JSON = "rades.json";

    public void generateProjectDescriptionFile(final UIPrompt prompt, final UIOutput log, final DirectoryResource projectDir, final RadesProject radesProject) throws IOException {

        log.info(log.out(), "Generiere RadesDescriptionfile " +
                RADES_JSON +
                " im Ordner " + projectDir.getName());

        FileResource<?> radesProjectFile = null;
        try {
            radesProjectFile = new NewFileResourceFactory(prompt, log).tryCreateFileResourceInteractive(projectDir, RADES_JSON);
        } catch (UserVetoException e) {
            return;
        } finally {
            if (radesProjectFile == null || !radesProjectFile.exists()) {
                return;
            }
        }

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
