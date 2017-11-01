package com.github.funthomas424242.rades.project;

import org.apache.commons.lang3.StringUtils;

import javax.annotation.Generated;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.HashSet;
import java.util.Set;

@Generated("com.github.funthomas424242.rades.builder.annotation.RadesAbstractDomainobject")
class RepositoryDescriptionImpl extends RepositoryDescription {

    public String getId() {
        return id;
    }

    public String getUrl() {
        return url;
    }

    public String getType() {
        return type;
    }
}

@Generated("com.github.funthomas424242.rades.builder.annotation.RadesAbstractDomainobject")
public class RepositoryDescriptionBuilder {

    protected String id;
    protected String url;
    protected String type;

    public RepositoryDescriptionBuilder() {
    }

    public RepositoryDescriptionBuilder withId(String id) {
        this.id = id;
        return this;
    }

    public RepositoryDescriptionBuilder withUrl(String url) {
        this.url = url;
        return this;
    }

    public RepositoryDescriptionBuilder withType(String type) {
        this.type = type;
        return this;
    }

    public RepositoryDescription buildNewInstance() {
        final RepositoryDescription project = new RepositoryDescriptionImpl();
        project.id = this.id;
        project.url = this.url;
        project.type = this.type;

        final ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        final Validator validator = factory.getValidator();
        final Set<ConstraintViolation<RepositoryDescription>> constraintViolations  =validator.validate(project);

        if (constraintViolations.size() > 0) {
            final Set<String> violationMessages = new HashSet<String>();

            for (ConstraintViolation<RepositoryDescription> constraintViolation : constraintViolations) {
                violationMessages.add(constraintViolation.getPropertyPath() + ": " + constraintViolation.getMessage());
            }

            throw new RuntimeException("RepositoryDescription is not valid:\n" + StringUtils.join(violationMessages, "\n"));
        }


        return project;
    }

}
