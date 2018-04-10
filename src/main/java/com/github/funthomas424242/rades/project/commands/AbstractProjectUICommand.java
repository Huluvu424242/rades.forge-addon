package com.github.funthomas424242.rades.project.commands;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.funthomas424242.flowdesign.Integration;
import com.github.funthomas424242.rades.core.commands.RadesAbstractUICommand;
import com.github.funthomas424242.rades.core.resources.UIResourceHelper;
import com.github.funthomas424242.rades.project.RadesProject;
import com.github.funthomas424242.rades.project.RadesProjectBuilder;
import org.jboss.forge.addon.resource.DirectoryResource;
import org.jboss.forge.addon.resource.FileResource;
import org.jboss.forge.addon.ui.command.UICommand;
import org.jboss.forge.addon.ui.context.UIContext;
import org.jboss.forge.addon.ui.output.UIOutput;

import javax.inject.Inject;

public abstract class AbstractProjectUICommand extends RadesAbstractUICommand implements UICommand {

    public static final String CATEGORY_RADES_PROJECT = "RADeS/Project";
    public static final String RADES_PROJECTDESCRIPTION_FILE = "rades.json";

    @Inject
    protected UIResourceHelper uiResourceHelper;

    public UIOutput getLogger(UIContext uiContext) {
        return uiContext.getProvider().getOutput();
    }

    public RadesProjectBuilder getRadesProjectBuilderFromFile(UIContext uiContext) throws java.io.IOException {
        final UIOutput log = getLogger(uiContext);
        final FileResource radesProjectDescriptionFile = uiResourceHelper.getFileResourceFromCurrentDir(uiContext, RADES_PROJECTDESCRIPTION_FILE);
        log.info(log.out(), "Radesfile" + radesProjectDescriptionFile);
        final RadesProjectBuilder builder;
        if (radesProjectDescriptionFile.exists()) {
            log.info(log.out(), "Radesfile exists");
            final String jsonContent = radesProjectDescriptionFile.getContents(CHARSET_UTF_8);
            final RadesProject oldRadesProject = new ObjectMapper().readValue(jsonContent, RadesProject.class);
            builder = new RadesProjectBuilder(oldRadesProject);
        } else {
            log.info(log.out(), "Radesfile does not exists");
            builder = new RadesProjectBuilder();
            log.info(log.out(), "Builder created");
        }
        return builder;
    }

    public DirectoryResource getCurrentDirectoryAsResource(UIContext uiContext) {
        return uiResourceHelper.getCurrentDirectoryResource(uiContext);
    }

    public FileResource getRadesProjectDescriptionfileAsResource(UIContext context) {
        return uiResourceHelper.getFileResourceFromCurrentDir(context, RADES_PROJECTDESCRIPTION_FILE);
    }

    public boolean existRadesProjectDescriptionfileAtCurrentDirectory(final UIContext uiContext) {
        return getRadesProjectDescriptionfileAsResource(uiContext).exists();
    }

    @Integration
    protected DirectoryResource getOrCreateSubdirAsResource(final UIContext uiContext, final String subDirName) {
        final DirectoryResource currentDirectoryResource = uiResourceHelper.getCurrentDirectoryResource(uiContext);
        return currentDirectoryResource.getOrCreateChildDirectory(subDirName);
    }

    public void setProjectDirToSubdirectory(final UIContext uiContext, final UIOutput log, final String subdirName) {
        final DirectoryResource projectDir = getOrCreateSubdirAsResource(uiContext, subdirName);
        log.info(log.out(), "Projektdirectory:" + projectDir.getFullyQualifiedName());
        uiResourceHelper.setCurrentDirectoryTo(uiContext, projectDir);
    }
}
