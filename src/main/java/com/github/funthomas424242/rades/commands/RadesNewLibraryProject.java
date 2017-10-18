package com.github.funthomas424242.rades.commands;

import org.jboss.forge.addon.ui.command.AbstractUICommand;
import org.jboss.forge.addon.ui.context.UIBuilder;
import org.jboss.forge.addon.ui.context.UIContext;
import org.jboss.forge.addon.ui.context.UIExecutionContext;
import org.jboss.forge.addon.ui.input.UIInput;
import org.jboss.forge.addon.ui.input.UISelectOne;
import org.jboss.forge.addon.ui.metadata.UICommandMetadata;
import org.jboss.forge.addon.ui.metadata.WithAttributes;
import org.jboss.forge.addon.ui.util.Metadata;
import org.jboss.forge.addon.ui.util.Categories;
import org.jboss.forge.addon.ui.result.Result;
import org.jboss.forge.addon.ui.result.Results;

import javax.inject.Inject;

public class RadesNewLibraryProject extends AbstractUICommand {


//    protected static final List<String> DEP_STARTER_LIST = Arrays.asList(
//            "spring-boot-starter-parent", "spring-boot-starter-batch",
//            "spring-boot-starter-jetty", "spring-boot-starter-tomcat",
//            "spring-boot-starter-logging", "spring-boot-starter-aop",
//            "spring-boot-starter-jpa", "spring-boot-starter-jetty-jdbc",
//            "spring-boot-starter-thymeleaf", "spring-boot-starter-web",
//            "spring-boot-starter-actuator", "spring-boot-starter-security",
//            "spring-boot-starter-test");

    // /////////////////////////////////////////////////////////////////////////
    //
    // Definition of interactive inputs (parameters)
    //
    // /////////////////////////////////////////////////////////////////////////

//    @Inject
//    @WithAttributes(label = "Specific Name in spring-boot-starter-specificname:", required = true)
//    protected UIInput<String> specificName;

    @Inject
    @WithAttributes(label = "Group ID:", required = true, defaultValue = "com.github.funthomas424242")
    protected UIInput<String> groupId;

    @Inject
    @WithAttributes(label = "Artifact ID:", required = true, defaultValue = "mylib")
    protected UIInput<String> artifactId;

    @Inject
    @WithAttributes(label = "Version:", required = true, defaultValue = "1.0.0-SNAPSHOT")
    protected UIInput<String> version;

    @Inject
    @WithAttributes(label = "Bintray Username:", required = true, defaultValue = "funthomas424242")
    protected UIInput<String> bintrayUsername;

//    @Inject
//    @WithAttributes(label = "Dependencies", required = true)
//    protected UISelectMany<String> dependencies;

	@Override
	public UICommandMetadata getMetadata(UIContext context) {
		return Metadata.forCommand(RadesNewLibraryProject.class)
				.name("rades-new-libproject")
                .description("Creates a RADES lib project.")
				.category(Categories.create("Rades"));
	}

	@Override
	public void initializeUI(UIBuilder builder) throws Exception {
        // add the inputs
        builder.add(groupId);
        builder.add(artifactId);
        builder.add(version);
        builder.add(bintrayUsername);
	}

	@Override
	public Result execute(UIExecutionContext context) throws Exception {
		return Results
				.success("Command 'rades-new-libproject' successfully executed!");
	}
}