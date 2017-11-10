package com.github.funthomas424242.rades.project.commands;

import org.jboss.forge.addon.resource.FileResource;
import org.jboss.forge.addon.ui.command.UICommand;
import org.jboss.forge.addon.ui.context.UIContext;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public interface RadesUICommand extends UICommand{

    static final Charset CHARSET_UTF_8 = StandardCharsets.UTF_8;
    static final String CATEGORY_RADES_PROJECT = "RADeS/Project";

    static final String RADES_PROJECTDESCRIPTION_FILE = "rades.json";

}
