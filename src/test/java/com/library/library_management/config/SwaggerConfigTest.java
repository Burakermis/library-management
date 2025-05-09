package com.library.library_management.config;

import io.swagger.v3.oas.models.OpenAPI;
import org.junit.jupiter.api.Test;
import org.springdoc.core.models.GroupedOpenApi;

import static org.assertj.core.api.Assertions.assertThat;

class SwaggerConfigTest {

    private final SwaggerConfig swaggerConfig = new SwaggerConfig();

    @Test
    void testApiInfo() {
        OpenAPI openAPI = swaggerConfig.apiInfo();
        assertThat(openAPI).isNotNull();
        assertThat(openAPI.getInfo()).isNotNull();
        assertThat(openAPI.getInfo().getTitle()).isEqualTo("Library Management System API");
        assertThat(openAPI.getInfo().getDescription()).isEqualTo("Kütüphane Yönetimi REST API dokümantasyonu");
        assertThat(openAPI.getInfo().getVersion()).isEqualTo("v1.0");
    }

    @Test
    void testPublicApi() {
        GroupedOpenApi groupedOpenApi = swaggerConfig.publicApi();
        assertThat(groupedOpenApi).isNotNull();
        assertThat(groupedOpenApi.getGroup()).isEqualTo("library-management");
        assertThat(groupedOpenApi.getPathsToMatch()).containsExactly("/api/**");
    }
}