package com.github.funthomas424242.rades.project.generators;

import com.github.funthomas424242.flowdesign.Integration;
import com.github.funthomas424242.flowdesign.Operation;
import com.github.funthomas424242.rades.core.resources.FileResourceFactory;
import com.github.funthomas424242.rades.core.resources.UserVetoException;
import com.github.funthomas424242.rades.project.RadesProjectAccessor;
import org.apache.maven.model.Model;
import org.apache.maven.model.io.xpp3.MavenXpp3Writer;
import org.jboss.forge.addon.maven.projects.MavenBuildSystem;
import org.jboss.forge.addon.projects.ProjectFactory;
import org.jboss.forge.addon.resource.DirectoryResource;
import org.jboss.forge.addon.resource.FileResource;
import org.jboss.forge.addon.resource.ResourceFactory;
import org.jboss.forge.addon.ui.input.UIPrompt;
import org.jboss.forge.addon.ui.output.UIOutput;

import javax.inject.Inject;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

public class NewLibraryProjectFacetsGenerator {

    public static final String POM_XML = "pom.xml";

    @Inject
    protected ResourceFactory resourceFactory;

    @Inject
    protected ProjectFactory projectFactory;

    @Inject
    protected MavenBuildSystem buildSystem;

    @Integration
    public void generate(final UIPrompt prompt, UIOutput log, final DirectoryResource projectDir, final RadesProjectAccessor radesProject) throws Exception {

        log.info(log.out(), "Generiere Projektfacetten wie pom.xml und ähnliches im Ordner " + projectDir.getName());

        FileResource<?> pomXML = null;
        try {
            pomXML = new FileResourceFactory(prompt, log).createFileResourceInteractive(projectDir, POM_XML);
        } catch (UserVetoException e) {
            return;
        } finally {
            if (pomXML == null || !pomXML.exists()) {
                return;
            }
        }
        initializeIfEmpty(pomXML, radesProject);
    }

    @Operation
    protected void initializeIfEmpty(FileResource<?> pomXML
            , final RadesProjectAccessor radesProject) throws Exception {

        if (pomXML.getContents(StandardCharsets.UTF_8).isEmpty()) {
            final Model pomModel = new Model();
            pomModel.setModelVersion("4.0.0");
            pomModel.setGroupId(radesProject.getGroupID());
            pomModel.setArtifactId(radesProject.getArtifactID());
            pomModel.setVersion(radesProject.getVersion());
            pomModel.setDescription(radesProject.getProjectDescription());
            final MavenXpp3Writer writer = new MavenXpp3Writer();
            final OutputStream ostream = pomXML.getResourceOutputStream();
            writer.write(ostream, pomModel);
        }
    }

}
