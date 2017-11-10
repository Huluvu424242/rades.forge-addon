package com.github.funthomas424242.rades.project.commands;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.funthomas424242.rades.core.resources.UIResourceHelper;
import com.github.funthomas424242.rades.project.RadesProject;
import com.github.funthomas424242.rades.project.RadesProjectBuilder;
import com.github.funthomas424242.rades.project.generators.NewRadesProjectDescriptionFileGenerator;
import com.github.funthomas424242.rades.project.validationrules.ProjectArtifactId;
import com.github.funthomas424242.rades.project.validationrules.ProjectGroupId;
import com.github.funthomas424242.rades.project.validationrules.ProjectVersion;
import org.jboss.forge.addon.resource.DirectoryResource;
import org.jboss.forge.addon.resource.FileResource;
import org.jboss.forge.addon.ui.command.AbstractUICommand;
import org.jboss.forge.addon.ui.context.UIBuilder;
import org.jboss.forge.addon.ui.context.UIContext;
import org.jboss.forge.addon.ui.context.UIExecutionContext;
import org.jboss.forge.addon.ui.input.UIInput;
import org.jboss.forge.addon.ui.input.UIPrompt;
import org.jboss.forge.addon.ui.metadata.UICommandMetadata;
import org.jboss.forge.addon.ui.metadata.WithAttributes;
import org.jboss.forge.addon.ui.output.UIOutput;
import org.jboss.forge.addon.ui.result.Result;
import org.jboss.forge.addon.ui.result.Results;
import org.jboss.forge.addon.ui.util.Categories;
import org.jboss.forge.addon.ui.util.Metadata;

import javax.inject.Inject;

public class RadesProjectNewDescriptionFileCommand extends AbstractUICommand implements RadesUICommand {

    public static final String COMMAND_NAME = "rades-project-addrades";

    @Inject
    protected UIResourceHelper uiResourceHelper;

    @Inject
    protected NewRadesProjectDescriptionFileGenerator newRadesProjectDescriptionFileGeneratorGenerator;


    @Inject
    @WithAttributes(label = "Group ID:", required = true, defaultValue = "com.github.myUsername")
    @ProjectGroupId
    protected UIInput<String> groupId;

    @Inject
    @WithAttributes(label = "Artifact ID:", required = true, defaultValue = "testProject")
    @ProjectArtifactId
    protected UIInput<String> artifactId;

    @Inject
    @WithAttributes(label = "Version:", required = true, defaultValue = "1.0.0-SNAPSHOT")
    @ProjectVersion
    protected UIInput<String> version;


    @Override
    public UICommandMetadata getMetadata(UIContext context) {
        return Metadata.forCommand(RadesProjectNewCommand.class)
                .name(COMMAND_NAME)
                .description("Add maven Koordinaten zur RADeS Projektbeschreibung")
                .category(Categories.create(CATEGORY_RADES_PROJECT));
    }



    @Override
    public void initializeUI(UIBuilder builder) throws Exception {
        super.initializeUI(builder);
        // add the inputs
        builder.add(groupId);
        builder.add(artifactId);
        builder.add(version);
    }


    @Override
    public Result execute(UIExecutionContext context) throws Exception {

        final UIContext uiContext = context.getUIContext();
        final UIOutput log = uiContext.getProvider().getOutput();
        final UIPrompt prompt = context.getPrompt();


        final FileResource radesProjectDescriptionFile = uiResourceHelper.getFileResourceFromCurrentDir(uiContext, RADES_PROJECTDESCRIPTION_FILE);
        final RadesProjectBuilder builder;
        if( radesProjectDescriptionFile.exists()){
            final String jsonContent = radesProjectDescriptionFile.getContents(CHARSET_UTF_8);
            final RadesProject oldRadesProject = new ObjectMapper().readValue(jsonContent, RadesProjectBuilder.RadesProjectImpl.class);
            builder=new RadesProjectBuilder(oldRadesProject);
        }else{
            builder=new RadesProjectBuilder();
        }

        // Create RadesProjectDescription
        final RadesProject radesProject = builder
                .withGroupID(groupId.getValue())
                .withArtifactID(artifactId.getValue())
                .withVersion(version.getValue())
                .withProjectDescription("TODO: Kurze Beschreibung zum Projekt eintragen.")
                .build();

        final DirectoryResource projectDirectoryResource = uiResourceHelper.getCurrentDirectoryResource(uiContext);
        newRadesProjectDescriptionFileGeneratorGenerator.generateProjectDescriptionFile(prompt, log, projectDirectoryResource, radesProject);

        return Results
                .success("Kommando " + COMMAND_NAME + " wurde erfolgreich ausgeführt.");
    }

}
