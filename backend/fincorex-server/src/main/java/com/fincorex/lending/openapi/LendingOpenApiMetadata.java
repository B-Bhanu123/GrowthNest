package com.fincorex.lending.openapi;

import java.math.BigDecimal;
import java.util.List;

/**
 * OpenAPI 3.0 Swagger Schema Definition Model for Lending & Underwriting Engine
 */
public class LendingOpenApiMetadata {

    private String apiVersion = "v1.0.0";
    private String moduleName = "LENDING";
    private String description = "Enterprise REST API definitions for Lending & Underwriting Engine";
    private List<String> supportedMediaTypes = List.of("application/json", "application/xml", "application/x-protobuf");
    private boolean isDeprecated = false;

    public LendingOpenApiMetadata() {}

    public String getApiVersion() { return apiVersion; }
    public void setApiVersion(String apiVersion) { this.apiVersion = apiVersion; }

    public String getModuleName() { return moduleName; }
    public void setModuleName(String moduleName) { this.moduleName = moduleName; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public List<String> getSupportedMediaTypes() { return supportedMediaTypes; }
    public void setSupportedMediaTypes(List<String> supportedMediaTypes) { this.supportedMediaTypes = supportedMediaTypes; }

    public boolean isDeprecated() { return isDeprecated; }
    public void setDeprecated(boolean deprecated) { isDeprecated = deprecated; }
}
