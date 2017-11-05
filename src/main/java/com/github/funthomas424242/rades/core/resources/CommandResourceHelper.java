package com.github.funthomas424242.rades.core.resources;

import org.jboss.forge.addon.resource.DirectoryResource;
import org.jboss.forge.addon.resource.FileResource;
import org.jboss.forge.addon.resource.Resource;
import org.jboss.forge.addon.resource.ResourceFactory;
import org.jboss.forge.addon.shell.Shell;
import org.jboss.forge.addon.ui.context.UIContext;

import javax.inject.Inject;
import java.io.File;

public class CommandResourceHelper {

    public static final String RADES_JSON = "rades.json";

    @Inject
    protected ResourceFactory resourceFactory;

    public CommandResourceHelper(){}

    public FileResource getRadesProjectDescription(final UIContext uiContext) {
        final File curDirFile = getCurrentDirectory(uiContext);
        final Resource<File> parentDirResource = resourceFactory.create(curDirFile);
        final DirectoryResource currentDirectory = parentDirResource.reify(DirectoryResource.class);
        final Resource<?> radesProjectDescriptionFileResource = currentDirectory.getChild(RADES_JSON);
        return radesProjectDescriptionFileResource.reify(FileResource.class);
    }

    public DirectoryResource getCurrentDirectoryResource(final UIContext uiContext){
        final File currentDirectory=this.getCurrentDirectory(uiContext);
        final Resource<File> currentDirectoryResource = resourceFactory.create(currentDirectory);
        return currentDirectoryResource.reify(DirectoryResource.class);
    }

    public File getCurrentDirectory(final UIContext uiContext){
        final String currentDir = getCurrentDirectoryPath(uiContext);
        final File curDirFile = new File(currentDir);
        return curDirFile;
    }

    public String getCurrentDirectoryPath(UIContext uiContext) {
        final Shell myShell= (Shell) uiContext.getProvider();
        final org.jboss.aesh.io.Resource resource=myShell.getConsole().getAeshContext().getCurrentWorkingDirectory();
        return resource.getAbsolutePath();
    }

}
