package com.github.funthomas424242.rades.core.resources;

import org.jboss.forge.addon.resource.DirectoryResource;
import org.jboss.forge.addon.resource.FileResource;
import org.jboss.forge.addon.ui.input.UIPrompt;
import org.jboss.forge.addon.ui.output.UIOutput;

public class NewFileResourceFactory {

    final UIPrompt prompt;
    final UIOutput log;

    public NewFileResourceFactory(final UIPrompt prompt, final UIOutput log) {
        this.prompt = prompt;
        this.log = log;
    }

    public FileResource<?> newFileResource(final DirectoryResource parentDirectory, final String fileName) {

        final FileResource<?> fileResource = parentDirectory.getChild(fileName).reify(FileResource.class);

        if (fileResource.exists()) {

            final boolean shouldOverride = prompt.promptBoolean("Soll ich bestehende Datei " + fileName + " überschreiben?", true);
            if (!shouldOverride) {
                log.warn(log.out(), "Erstellung der Datei " + fileName + " auf Nutzerwunsch abgebrochen.");
                log.warn(log.out(), "Datei " + fileName + " im Ordner " + parentDirectory.getName() + " muss manuell angepasst werden!");
                return fileResource;
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
