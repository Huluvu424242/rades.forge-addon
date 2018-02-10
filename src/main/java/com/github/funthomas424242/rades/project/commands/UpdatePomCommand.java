package com.github.funthomas424242.rades.project.commands;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.funthomas424242.flowdesign.Integration;
import com.github.funthomas424242.rades.core.resources.UIResourceHelper;
import com.github.funthomas424242.rades.project.RadesProject;
import com.github.funthomas424242.rades.project.RadesProjectBuilder;
import org.apache.maven.model.*;
import org.apache.maven.model.io.xpp3.MavenXpp3Reader;
import org.apache.maven.model.io.xpp3.MavenXpp3Writer;
import org.jboss.forge.addon.resource.FileResource;
import org.jboss.forge.addon.ui.context.UIContext;
import org.jboss.forge.addon.ui.context.UIExecutionContext;
import org.jboss.forge.addon.ui.input.UIPrompt;
import org.jboss.forge.addon.ui.metadata.UICommandMetadata;
import org.jboss.forge.addon.ui.result.Result;
import org.jboss.forge.addon.ui.result.Results;
import org.jboss.forge.addon.ui.util.Categories;
import org.jboss.forge.addon.ui.util.Metadata;

import javax.inject.Inject;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class UpdatePomCommand extends AbstractProjectUICommand {

    public static final String COMMAND_NAME = "rades-pom-update";

    @Inject
    protected UIResourceHelper commandHelper;

    @Override
    public UICommandMetadata getMetadata(UIContext context) {
        return Metadata.forCommand(UpdatePomCommand.class)
                .name(COMMAND_NAME)
                .description("Update pom.xml of a RADES project.")
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
        final RadesProject radesProject = new ObjectMapper().readValue(jsonTxt, RadesProjectBuilder.RadesProjectImpl.class);

        // Replace info in pom.xml
        final FileResource pomXML = commandHelper.getFileResourceFromCurrentDir(uiContext, "pom.xml");

        if (!pomXML.exists() || pomXML.getContents(StandardCharsets.UTF_8).isEmpty()) {
            // create new pom.xml
            final Model pomModel = new Model();
            copyRadesProjectToPomModel(radesProject, pomModel);
            writeModelToPomXml(pomModel, pomXML);
        } else {
            // Update existing pom.xml
            final boolean shouldOverride = prompt.promptBoolean("Soll die aktuelle pom.xml ersetzt werden?", false);
            if (shouldOverride) {
                final MavenXpp3Reader pomReader = new MavenXpp3Reader();
                final Model pomModel = pomReader.read(pomXML.getResourceInputStream());
                copyRadesProjectToPomModel(radesProject, pomModel);
                writeModelToPomXml(pomModel, pomXML);
            }
        }
        return Results
                .success("Kommando '" + COMMAND_NAME + "' wurde erfolgreich ausgeführt.");
    }

    protected void writeModelToPomXml(final Model pomModel, final FileResource pomXML) throws IOException {
        final MavenXpp3Writer writer = new MavenXpp3Writer();
        final OutputStream ostream = pomXML.getResourceOutputStream();
        writer.write(ostream, pomModel);
    }

    protected void copyRadesProjectToPomModel(final RadesProject radesProject, final Model pomModel) {
        pomModel.setModelVersion("4.0.0");
        pomModel.setModelEncoding("UTF-8");
        pomModel.addProperty("project.build.sourceEncoding","UTF-8");
        pomModel.addProperty("maven.compiler.source","1.8");
        pomModel.addProperty("maven.compiler.target","1.8");

        // projekt maven coordinaten + beschreibung
        pomModel.setGroupId(radesProject.getGroupID());
        pomModel.setArtifactId(radesProject.getArtifactID());
        pomModel.setVersion(radesProject.getVersion());
        pomModel.setDescription(radesProject.getProjectDescription());

        // license
        final License license = new License();
        license.setUrl("./LICENSE");
        pomModel.setLicenses(Arrays.asList(license));

        // add github support
        if (hasFullGithubSupportInfo(radesProject)) {

            // ci support
            final CiManagement ciManagement = new CiManagement();
            ciManagement.setSystem("Travis");
            ciManagement.setUrl("https://travis-ci.org/"+radesProject.getGithubUsername()+"/"
                    +radesProject.getGithubRepositoryname());
            pomModel.setCiManagement(ciManagement);

            // issues
            final IssueManagement isssueManagement=new IssueManagement();
            isssueManagement.setSystem("GitHub");
            isssueManagement.setUrl("https://github.com/"
                            +radesProject.getGithubUsername()+"/"
                            +radesProject.getGithubRepositoryname()+"/issues/new");
            pomModel.setIssueManagement(isssueManagement);

            // scm
            final Scm scm = new Scm();
            scm.setUrl("https://github.com/" + radesProject.getGithubUsername() + "/"
                    + radesProject.getGithubRepositoryname());
            scm.setConnection("scm:git:https://github.com/"
                    + radesProject.getGithubUsername() + "/"
                    + radesProject.getGithubRepositoryname() + ".git");
            scm.setDeveloperConnection("scm:git:git@github.com:"
                    + radesProject.getGithubUsername() + "/"
                    + radesProject.getGithubRepositoryname() + ".git"
            );
            pomModel.setScm(scm);
        }

        //add bintray support
        if (hasFullBintraySupportInfo(radesProject)) {
            final DistributionManagement distributionManagement = new DistributionManagement();
            final DeploymentRepository deploymentRepository = new DeploymentRepository();
            deploymentRepository.setId("bintray-" + radesProject.getBintrayUsername() + "-" + radesProject.getBintrayRepositoryname());
            deploymentRepository.setName(radesProject.getBintrayUsername() + "-" + radesProject.getBintrayRepositoryname());
            deploymentRepository.setUrl("https://api.bintray.com/maven/"
                    + radesProject.getBintrayUsername() + "/"
                    + radesProject.getBintrayRepositoryname() + "/"
                    + radesProject.getBintrayPackagename() + "/;publish=1"
            );
            distributionManagement.setRepository(deploymentRepository);
            pomModel.setDistributionManagement(distributionManagement);
        }
    }

    protected boolean hasFullGithubSupportInfo(RadesProject radesProject) {
        return (radesProject.getGithubUsername() != null)
                && (radesProject.getGithubRepositoryname() != null);
    }

    protected boolean hasFullBintraySupportInfo(final RadesProject radesProject) {
        return (radesProject.getBintrayUsername() != null)
                && (radesProject.getBintrayRepositoryname() != null)
                && (radesProject.getBintrayPackagename() != null);
    }
}
