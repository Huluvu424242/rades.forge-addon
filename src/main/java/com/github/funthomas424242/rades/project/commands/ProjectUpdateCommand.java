package com.github.funthomas424242.rades.project.commands;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.funthomas424242.flowdesign.Integration;
import com.github.funthomas424242.rades.core.resources.UIResourceHelper;
import com.github.funthomas424242.rades.project.RadesProject;
import com.github.funthomas424242.rades.project.RadesProjectBuilder;
import com.github.funthomas424242.rades.project.generators.NewRadesProjectDescriptionFileGenerator;
import com.github.funthomas424242.rades.project.validationrules.ProjectDescription;
import org.apache.maven.model.Model;
import org.apache.maven.model.io.xpp3.MavenXpp3Reader;
import org.apache.maven.model.io.xpp3.MavenXpp3Writer;
import org.jboss.forge.addon.resource.FileResource;
import org.jboss.forge.addon.ui.context.UIContext;
import org.jboss.forge.addon.ui.context.UIExecutionContext;
import org.jboss.forge.addon.ui.input.UIInput;
import org.jboss.forge.addon.ui.input.UIPrompt;
import org.jboss.forge.addon.ui.metadata.UICommandMetadata;
import org.jboss.forge.addon.ui.metadata.WithAttributes;
import org.jboss.forge.addon.ui.result.Result;
import org.jboss.forge.addon.ui.result.Results;
import org.jboss.forge.addon.ui.util.Categories;
import org.jboss.forge.addon.ui.util.Metadata;

import javax.inject.Inject;
import java.io.OutputStream;

public class ProjectUpdateCommand extends AbstractProjectUICommand {

    public static final String COMMAND_NAME = "rades-project-update";

    @Inject
    protected UIResourceHelper commandHelper;

    @Inject
    protected NewRadesProjectDescriptionFileGenerator radesProjectInfoGenerator;


    @Inject
    @WithAttributes(label = "Projektverzeichnis:", required = true)
    @ProjectDescription
    protected UIInput<String> projectDescription;

    @Override
    public UICommandMetadata getMetadata(UIContext context) {
        return Metadata.forCommand(ProjectUpdateCommand.class)
                .name(COMMAND_NAME)
                .description("Change a RADES project.")
                .category(Categories.create(CATEGORY_RADES_PROJECT));
    }

    @Override
    public boolean isEnabled(UIContext context) {
        final boolean isEnabled = super.isEnabled(context);
        final FileResource radesProjectDescription = commandHelper.getFileResourceFromCurrentDir(context, RADES_PROJECTDESCRIPTION_FILE);
        return isEnabled && radesProjectDescription.exists();
    }

    @Override
    @Integration
    public Result execute(UIExecutionContext context) throws Exception {

        final UIContext uiContext = context.getUIContext();
        final UIPrompt prompt = context.getPrompt();

        final FileResource radesProjectDescriptionFile = commandHelper.getFileResourceFromCurrentDir(uiContext, RADES_PROJECTDESCRIPTION_FILE);
        final String jsonTxt = radesProjectDescriptionFile.getContents(CHARSET_UTF_8);
        final RadesProject oldRadesProject = new ObjectMapper().readValue(jsonTxt, RadesProjectBuilder.RadesProjectImpl.class);

        final String projectDescription = oldRadesProject.getProjectDescription();
        final boolean shouldOverride = prompt.promptBoolean("Soll die aktuelle Projektbeschreibung: " + projectDescription + " ersetzt werden?", false);
        if (shouldOverride) {
            final String newProjectDescription = prompt.prompt("Bitte neue Projektbeschreibung eingeben:");

            // Replace in rades.json
            final RadesProject radesProject = new RadesProjectBuilder(oldRadesProject)
                    .withProjectDescription(newProjectDescription)
                    .build();

            // Replace info im rades.json
            this.radesProjectInfoGenerator.saveRadesProjectInfo(radesProjectDescriptionFile, radesProject);


            // Replace info in pom.xml
            final FileResource pomXML = commandHelper.getFileResourceFromCurrentDir(uiContext, "pom.xml");

            final MavenXpp3Reader pomReader = new MavenXpp3Reader();
            final Model pomModel = pomReader.read(pomXML.getResourceInputStream());
            pomModel.setDescription(newProjectDescription);
            final MavenXpp3Writer writer = new MavenXpp3Writer();
            final OutputStream ostream = pomXML.getResourceOutputStream();
            writer.write(ostream, pomModel);
        }
        return Results
                .success("Kommando '" + COMMAND_NAME + "' wurde erfolgreich ausgeführt.");
    }
}
