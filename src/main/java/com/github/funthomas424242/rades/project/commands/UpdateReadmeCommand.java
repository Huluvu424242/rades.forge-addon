package com.github.funthomas424242.rades.project.commands;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.funthomas424242.flowdesign.Integration;
import com.github.funthomas424242.rades.core.resources.UIResourceHelper;
import com.github.funthomas424242.rades.project.RadesProject;
import com.github.funthomas424242.rades.project.RadesProjectBuilder;
import io.github.swagger2markup.markup.builder.MarkupDocBuilder;
import io.github.swagger2markup.markup.builder.MarkupDocBuilders;
import io.github.swagger2markup.markup.builder.MarkupLanguage;
import io.github.swagger2markup.markup.builder.MarkupTableColumn;
import org.asciidoctor.Asciidoctor;
import org.asciidoctor.ast.ContentPart;
import org.asciidoctor.ast.StructuredDocument;
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
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UpdateReadmeCommand extends AbstractProjectUICommand {

    public static final String COMMAND_NAME = "rades-readme-update";

    @Inject
    protected UIResourceHelper commandHelper;

    @Override
    public UICommandMetadata getMetadata(UIContext context) {
        return Metadata.forCommand(UpdateReadmeCommand.class)
                .name(COMMAND_NAME)
                .description("Update README.adoc of a RADES project.")
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
        final RadesProjectBuilder.RadesProjectImpl radesProject = new ObjectMapper().readValue(jsonTxt, RadesProjectBuilder.RadesProjectImpl.class);

        // Read README.asciidoc
        final FileResource readmeFileResource = commandHelper.getFileResourceFromCurrentDir(uiContext, "README.adoc");

        if (!readmeFileResource.exists() || readmeFileResource.getContents(StandardCharsets.UTF_8).isEmpty()) {
            // create new README.adoc
            copyContentTo(readmeFileResource, radesProject);
        } else {
            final Asciidoctor asciidoctor = Asciidoctor.Factory.create();
            final Map options = new HashMap<String, Object>();
            final StructuredDocument adocDocument = asciidoctor.readDocumentStructure(readmeFileResource.getContents(), options);
//            final DocumentHeader header = adocDocument.getHeader();
//            System.out.println("####DOCHeader:" + header.toString());

//            final List<ContentPart> parts = adocDocument.getParts();
//            for (ContentPart part : parts) {
//                showPart("####PART:", part);
//            }
            final ContentPart part0 = adocDocument.getPartById("status");

            // TODO Ersetzung parts aus Readme.adoc

            // Update existing pom.xml
            final boolean shouldOverride = prompt.promptBoolean("Soll die aktuelle README.adoc ersetzt werden?", false);
            if (shouldOverride) {
                copyContentTo(readmeFileResource, radesProject);
            }
        }
        return Results
                .success("Kommando '" + COMMAND_NAME + "' wurde erfolgreich ausgeführt.");
    }

    public void copyContentTo(FileResource readmeFileResource, final RadesProject radesProject) {
        final MarkupDocBuilder builder = MarkupDocBuilders.documentBuilder(MarkupLanguage.ASCIIDOC);
        final List<MarkupTableColumn> tableRowsInPSV = new ArrayList<>();
        final MarkupTableColumn column = new MarkupTableColumn("Header 1 | Header 2 | Header2", true, 1);

        final String projectDescription = radesProject.getProjectDescription();
        final MarkupDocBuilder document = builder
                .textLine("[#status]")
                .textLine("image:https://api.bintray.com/packages/" + radesProject.getBintrayUsername()
                        + "/" + radesProject.getBintrayRepositoryname()
                        + "/" + radesProject.getBintrayPackagename()
                        + "/images/download.svg[link=\"https://bintray.com/"
                        + radesProject.getBintrayUsername()
                        + "/" + radesProject.getBintrayRepositoryname()
                        + "/" + radesProject.getBintrayPackagename() + "/_latestVersion")
                .textLine("image:https://travis-ci.org"
                        + "/" + radesProject.getGithubUsername()
                        + "/" + radesProject.getGithubRepositoryname() + ".svg?branch=master[\"Build Status\", link=\"https://travis-ci.org"
                        + "/" + radesProject.getGithubUsername()
                        + "/" + radesProject.getGithubRepositoryname())
                .textLine("image:https://api.codacy.com/project/badge/Grade/64f23754fdc1426a9216521cf5362d71[\"Codacy code quality\", link=\"https://www.codacy.com/app"
                        + "/" + radesProject.getGithubUsername()
                        + "/" + radesProject.getGithubRepositoryname() + "?utm_source=github.com&utm_medium=referral&utm_content="
                        + radesProject.getGithubUsername()
                        + "/" + radesProject.getGithubRepositoryname() + "&utm_campaign=Badge_Grade")
                .textLine("image:https://codecov.io/gh"
                        + "/" + radesProject.getGithubUsername()
                        + "/" + radesProject.getGithubRepositoryname() + "/branch/master/graph/badge.svg[link=\"https://codecov.io/gh"
                        + "/" + radesProject.getGithubUsername()
                        + "/" + radesProject.getGithubRepositoryname())
                .textLine("image:https://badge.waffle.io"
                        + "/" + radesProject.getGithubUsername()
                        + "/" + radesProject.getGithubRepositoryname() + ".svg?columns=all[\"Waffle.io - Columns and their card count\", link=\"https://waffle.io"
                        + "/" + radesProject.getGithubUsername()
                        + "/" + radesProject.getGithubRepositoryname())
                .newLine()
                .textLine("[#main]")
                .documentTitle(radesProject.getGithubRepositoryname())
                .newLine();

        if (projectDescription != null) {
            builder.paragraph(radesProject.getProjectDescription());
        }
        builder.writeToFile(Paths.get(readmeFileResource.getParent().getFullyQualifiedName(), "README"), StandardCharsets.UTF_8);
    }

}
