package com.github.funthomas424242.rades.core.resources;

import org.jboss.forge.addon.resource.DirectoryResource;
import org.jboss.forge.addon.resource.FileResource;
import org.jboss.forge.addon.resource.Resource;
import org.jboss.forge.addon.resource.ResourceFactory;
import org.jboss.forge.addon.shell.Shell;
import org.jboss.forge.addon.ui.context.UIContext;

import javax.inject.Inject;
import java.io.File;

public class UIResourceHelper {

    @Inject
    protected ResourceFactory resourceFactory;

    public UIResourceHelper(){}

    public FileResource getFileResourceFromCurrentDir(final UIContext uiContext, final String fileName) {
        final File curDirFile = getCurrentDirectory(uiContext);
        final Resource<File> currentDirectoryFileResource = resourceFactory.create(curDirFile);
        final DirectoryResource currentDirectory = currentDirectoryFileResource.reify(DirectoryResource.class);
        final Resource<?> fileResource = currentDirectory.getChild(fileName);
        return fileResource.reify(FileResource.class);
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

    public void setCurrentDirectoryTo(UIContext uiContext, final DirectoryResource directory) {
        final Shell myShell= (Shell) uiContext.getProvider();
        myShell.setCurrentResource(directory);
    }

}
