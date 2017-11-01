package com.github.funthomas424242.rades.project.generator;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.funthomas424242.rades.project.domain.RadesProject;
import org.jboss.forge.addon.resource.DirectoryResource;
import org.jboss.forge.addon.resource.FileResource;
import org.jboss.forge.addon.ui.input.UIPrompt;
import org.jboss.forge.addon.ui.output.UIOutput;

import java.io.IOException;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;

public class NewRadesProjectDescriptionFileGenerator {

    public void generateProjectDescriptionFile(final UIPrompt prompt, final UIOutput log, final DirectoryResource projectDir, final RadesProject radesProject) throws IOException {

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
