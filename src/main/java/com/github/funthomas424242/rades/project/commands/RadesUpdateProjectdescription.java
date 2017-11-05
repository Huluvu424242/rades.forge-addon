package com.github.funthomas424242.rades.project.commands;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.xml.JacksonXmlModule;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.dataformat.xml.ser.ToXmlGenerator;
import com.github.funthomas424242.rades.core.resources.CommandResourceHelper;
import com.github.funthomas424242.rades.flowdesign.Integration;
import com.github.funthomas424242.rades.project.RadesProject;
import com.github.funthomas424242.rades.project.RadesProjectBuilder;
import com.github.funthomas424242.rades.validationrules.ProjectDescription;
import org.apache.maven.pom._4_0.Model;
import org.jboss.forge.addon.maven.projects.MavenBuildSystem;
import org.jboss.forge.addon.projects.ProjectFactory;
import org.jboss.forge.addon.resource.FileResource;
import org.jboss.forge.addon.resource.ResourceFactory;
import org.jboss.forge.addon.ui.command.AbstractUICommand;
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
import java.io.OutputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.nio.charset.Charset;

import static java.nio.charset.StandardCharsets.UTF_8;

public class RadesUpdateProjectdescription extends AbstractUICommand implements RadesUICommand {

    public static final String COMMANDLINE_COMMAND = "rades-update-projectdescription";

    @Inject
    protected ResourceFactory resourceFactory;

    @Inject
    protected ProjectFactory projectFactory;

    @Inject
    protected MavenBuildSystem buildSystem;

    @Inject
    protected CommandResourceHelper commandHelper;


    @Inject
    @WithAttributes(label = "Projektverzeichnis:", required = true)
    @ProjectDescription
    protected UIInput<String> projectDescription;

    @Override
    public UICommandMetadata getMetadata(UIContext context) {
        return Metadata.forCommand(RadesNewLibraryProject.class)
                .name(COMMANDLINE_COMMAND)
                .description("Change a RADES project.")
                .category(Categories.create(CATEGORY_RADES_PROJECT));
    }

    @Override
    public boolean isEnabled(UIContext context) {
        final boolean isEnabled = super.isEnabled(context);
        final FileResource radesProjectDescription = commandHelper.getFileResourceFromCurrentDir(context, RADES_JSON);
        return isEnabled && radesProjectDescription.exists();
    }

    @Override
    @Integration
    public Result execute(UIExecutionContext context) throws Exception {

        final UIContext uiContext = context.getUIContext();
        final UIOutput log = uiContext.getProvider().getOutput();
        final UIPrompt prompt = context.getPrompt();

        final FileResource radesProjectDescriptionFile = commandHelper.getFileResourceFromCurrentDir(uiContext, RADES_JSON);
        final String jsonTxt = radesProjectDescriptionFile.getContents(Charset.forName(ENCODING_UTF8));
        final RadesProject oldRadesProject = new ObjectMapper().readValue(jsonTxt, RadesProjectBuilder.RadesProjectImpl.class);

        final String projectDescription = oldRadesProject.getProjectDescription();
        final boolean shouldOverride = prompt.promptBoolean("Soll die aktuelle Projektbeschreibung: " + projectDescription + " ersetzt werden?", false);
        if (shouldOverride) {
            final String newProjectDescription = prompt.prompt("Bitte neue Projektbeschreibung eingeben:");

            // Replace in rades.json
            final RadesProject radesProject = new RadesProjectBuilder(oldRadesProject)
                    .withProjectDescription(newProjectDescription)
                    .build();
            final PipedOutputStream pipeOut = new PipedOutputStream();
            final PipedInputStream pipeIn = new PipedInputStream(pipeOut);
            final ObjectMapper objMapper = new ObjectMapper();
            objMapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
            objMapper.writer().writeValue(pipeOut, radesProject);
            radesProjectDescriptionFile.setContents(pipeIn);
            pipeOut.flush();
            pipeOut.close();
            pipeIn.close();

            // Replace in pom.xml
            final FileResource pomXML = commandHelper.getFileResourceFromCurrentDir(uiContext, "pom.xml");

            final JacksonXmlModule module = new JacksonXmlModule();
            module.setDefaultUseWrapper(true);
            XmlMapper xmlMapper = new XmlMapper(module);
            xmlMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            xmlMapper.configure(ToXmlGenerator.Feature.WRITE_XML_DECLARATION, true);
//            xmlMapper.configure(SerializationFeature.WRAP_ROOT_VALUE, true);
            final String xml = pomXML.getContents(UTF_8);
            final Model pomModel = xmlMapper.readValue(xml, Model.class);
            pomModel.setDescription(newProjectDescription);
            final OutputStream ostream = pomXML.getResourceOutputStream();
            xmlMapper.writeValue(ostream, pomModel);
        }
        return Results
                .success("Kommando '" + COMMANDLINE_COMMAND + "' wurde erfolgreich ausgeführt.");
    }
}
