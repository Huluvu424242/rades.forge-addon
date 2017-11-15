package com.github.funthomas424242.rades.core.resources;

import org.jboss.forge.addon.resource.DirectoryResource;
import org.jboss.forge.addon.resource.FileResource;
import org.jboss.forge.addon.ui.input.UIPrompt;
import org.jboss.forge.addon.ui.output.UIOutput;

public class FileResourceFactory {

    final UIPrompt prompt;
    final UIOutput log;

    public FileResourceFactory(final UIPrompt prompt, final UIOutput log) {
        this.prompt = prompt;
        this.log = log;
    }

    public FileResource<?> createFileResourceInteractive(final DirectoryResource parentDirectory
            , final String fileName) throws UserVetoException {
        return createFileResource(parentDirectory, fileName, true);
    }

    public FileResource<?> createFileResourceForceOverride(final DirectoryResource parentDirectory
            , final String fileName) throws UserVetoException {
        return createFileResource(parentDirectory, fileName, false);
    }

    public FileResource<?> createFileResource(final DirectoryResource parentDirectory
            , final String fileName
            , final boolean interactiveMode) throws UserVetoException {

        final FileResource<?> fileResource = parentDirectory.getChild(fileName).reify(FileResource.class);

        if (fileResource.exists()) {

            final boolean shouldOverride;
            if (interactiveMode) {
                shouldOverride = prompt.promptBoolean("Soll ich bestehende Datei " + fileName + " anpassen?", true);
            } else {
                shouldOverride = true;
            }
            if (!shouldOverride) {
                log.warn(log.out(), "Erstellung der Datei " + fileName + " auf Nutzerwunsch abgebrochen.");
                log.warn(log.out(), "Datei " + fileName + " im Ordner " + parentDirectory.getName() + " muss manuell angepasst werden!");
                throw new UserVetoException("Erstellung der Datei " + fileName + " auf Nutzerwunsch abgebrochen.");
            } else {
                fileResource.delete();
                fileResource.refresh();
            }
        }
        boolean isCreated = fileResource.createNewFile();
        if (isCreated) {
            log.info(log.out(), "Datei " + fileName + " wurde erstellt im Ordner " + parentDirectory.getName());
        }
        return fileResource;
    }

}
