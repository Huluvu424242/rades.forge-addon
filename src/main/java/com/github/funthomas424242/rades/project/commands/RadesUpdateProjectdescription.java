package com.github.funthomas424242.rades.project.commands;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.funthomas424242.rades.core.resources.CommandResourceHelper;
import com.github.funthomas424242.rades.flowdesign.Integration;
import com.github.funthomas424242.rades.project.RadesProject;
import com.github.funthomas424242.rades.project.RadesProjectBuilder;
import com.github.funthomas424242.rades.validationrules.ProjectDescription;
import org.jboss.forge.addon.maven.projects.MavenBuildSystem;
import org.jboss.forge.addon.projects.ProjectFactory;
import org.jboss.forge.addon.resource.FileResource;
import org.jboss.forge.addon.resource.ResourceFactory;
import org.jboss.forge.addon.ui.command.AbstractUICommand;
import org.jboss.forge.addon.ui.command.UICommand;
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
import java.nio.charset.Charset;

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
        final boolean isEnabled= super.isEnabled(context);
        final FileResource radesProjectDescription=commandHelper.getRadesProjectDescription(context);
        return isEnabled && radesProjectDescription.exists();
    }

    @Override
    public void initializeUI(UIBuilder builder) throws Exception {

    }


    @Override
    @Integration
    public Result execute(UIExecutionContext context) throws Exception {

        final UIContext uiContext = context.getUIContext();
        final UIOutput log = uiContext.getProvider().getOutput();
        final UIPrompt prompt = context.getPrompt();

        final FileResource radesProjectDescriptionFile=commandHelper.getRadesProjectDescription(uiContext);
        final String jsonTxt = radesProjectDescriptionFile.getContents(Charset.forName(ENCODING_UTF8));
        final RadesProject radesProject = new ObjectMapper().readValue(jsonTxt, RadesProjectBuilder.RadesProjectImpl.class);
        log.info(log.out(),"JSON:"+radesProject.toString());


//
//
//
//
//        final List<Class<? extends ProjectFacet>> facets = new ArrayList<>();
//        facets.add(ResourcesFacet.class);
//        facets.add(MetadataFacet.class);
//        facets.add(JavaSourceFacet.class);
//        facets.add(JavaCompilerFacet.class);
//        facets.add(MavenPluginFacet.class);
//        facets.add(DependencyFacet.class);
//        final Project project = projectFactory.createProject(projectDir,
//                buildSystem, facets);

//        final MetadataFacet facet = project.getFacet(MetadataFacet.class);
//        facet.setProjectGroupName(radesProject.getGroupID());
//        facet.setProjectName(radesProject.getArtifactID());
//        facet.setProjectVersion(radesProject.getVersion());


//        final boolean shouldOverride = prompt.promptBoolean("Soll ich bestehende Datei " + fileName + " überschreiben?", true);
//        if (!shouldOverride) {
//            log.warn(log.out(), "Erstellung der Datei " + fileName + " auf Nutzerwunsch abgebrochen.");
//            log.warn(log.out(), "Datei " + fileName + " im Ordner " + parentDirectory.getName() + " muss manuell angepasst werden!");
//            return fileResource;
//        } else {
//            fileResource.delete();
//            fileResource.refresh();
//        }


        return Results
                .success("Kommando '" + COMMANDLINE_COMMAND + "' wurde erfolgreich ausgeführt.");
    }
}
