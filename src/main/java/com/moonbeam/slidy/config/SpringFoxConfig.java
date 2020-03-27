package com.moonbeam.slidy.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.AuthorizationScope;
import springfox.documentation.service.BasicAuth;
import springfox.documentation.service.SecurityReference;
import springfox.documentation.service.SecurityScheme;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.Arrays;

@Configuration
@EnableSwagger2
public class SpringFoxConfig {
    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
            .select()
            .apis(RequestHandlerSelectors.basePackage("com.moonbeam.slidy.web.rest"))
            .paths(PathSelectors.any())
            .build()
            .groupName("all")
            .securitySchemes(Arrays.asList(securityScheme()))
            .securityContexts(Arrays.asList(securityContext()));
    }

    private SecurityScheme securityScheme() {
        return new BasicAuth("swagger-basic-auth");
    }

    private SecurityContext securityContext() {
        return SecurityContext.builder()
            .securityReferences(Arrays.asList(
                new SecurityReference("swagger-basic-auth", new AuthorizationScope[0])))
            .forPaths(PathSelectors.regex("/api.*")).build();
    }
}
