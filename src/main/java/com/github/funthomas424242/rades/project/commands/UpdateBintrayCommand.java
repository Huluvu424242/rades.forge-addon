package com.github.funthomas424242.rades.project.commands;

import com.github.funthomas424242.rades.project.RadesProject;
import com.github.funthomas424242.rades.project.RadesProjectBuilder;
import com.github.funthomas424242.rades.project.generators.NewRadesProjectDescriptionFileGenerator;
import com.github.funthomas424242.rades.project.validationrules.*;
import org.jboss.forge.addon.resource.DirectoryResource;
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

public class UpdateBintrayCommand extends AbstractProjectUICommand {

    public static final String COMMAND_NAME = "rades-project-updatebintray";

    @Inject
    protected NewRadesProjectDescriptionFileGenerator newRadesProjectDescriptionFileGeneratorGenerator;



    @Inject
    @WithAttributes(label = "Bintray Username:", defaultValue = "myBintrayUsername")
    @BintrayUsername
    protected UIInput<String> bintrayUsername;

    @Inject
    @WithAttributes(label = "Bintray Repositoryname:", required = true, description = "myBintrayRepositoryname")
    @BintrayRepositoryname
    protected UIInput<String> bintrayRepositoryname;

    @Inject
    @WithAttributes(label = "Bintray Packagename:", defaultValue = "myBintrayPackagename")
    @BintrayPackagename
    protected UIInput<String> bintrayPackagename;

    @Override
    public UICommandMetadata getMetadata(UIContext context) {
        return Metadata.forCommand(UpdateBintrayCommand.class)
                .name(COMMAND_NAME)
                .description("Add Bintray Infos to rades project")
                .category(Categories.create(CATEGORY_RADES_PROJECT));
    }

    @Override
    public boolean isEnabled(UIContext context) {
        final boolean isEnabled = super.isEnabled(context);
        return isEnabled && existRadesProjectDescriptionfileAtCurrentDirectory(context);
    }


    @Override
    public void initializeUI(UIBuilder builder) throws Exception {
        super.initializeUI(builder);
        // add the inputs
        builder.add(bintrayUsername);
        builder.add(bintrayRepositoryname);
        builder.add(bintrayPackagename);
    }


    @Override
    public Result execute(UIExecutionContext context) throws Exception {

        final UIContext uiContext = context.getUIContext();
        final UIOutput log = getLogger(uiContext);
        final UIPrompt prompt = context.getPrompt();

        final RadesProjectBuilder builder = getRadesProjectBuilderFromFile(uiContext);
        // Create RadesProjectDescription
        final RadesProject radesProject = builder
                .withBintrayUsername(bintrayUsername.getValue())
                .withBintrayRepositoryname(bintrayRepositoryname.getValue())
                .withBintrayPackagename(bintrayPackagename.getValue())
                .build();

        final DirectoryResource projectDirectoryResource = getCurrentDirectoryAsResource(uiContext);
        newRadesProjectDescriptionFileGeneratorGenerator.generateProjectDescriptionFile(prompt, log, projectDirectoryResource, radesProject,COMMAND_NAME);

        return Results
                .success("Kommando " + COMMAND_NAME + " wurde erfolgreich ausgeführt.");
    }

}
