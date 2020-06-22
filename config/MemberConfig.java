package com.optum.micro.config;

import org.springframework.context.annotation.Bean;
import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import org.springframework.validation.beanvalidation.SpringValidatorAdapter;

public class MemberConfig {
    @Bean
    public OpenAPI planOpenAPISpecification() {
        return new OpenAPI()
                .info(new Info().title("RXCLAIM-MEMBER-SEARCH-API")
                        .description("Rxclaim Member Search Api")
                        .version("v1.0")
                        .license(new License().name("optumrx").url("https://github.optum.com/pages/optumrx-microservices/hemiapiportal/")))
                .externalDocs(new ExternalDocumentation()
                        .description("OptumRx Docusaurus Documentation")
                        .url("https://github.optum.com/pages/optumrx-microservices/hemiapiportal/docs/planfg/"));
    }
}
