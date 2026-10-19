
package com.example.banking.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI bankingOpenAPI() {

        return new OpenAPI()
                .info(new Info()
                        .title("Banking Microservice API")
                        .version("1.0.0")
                        .description(
                                "API REST de gestion des comptes " +
                                        "bancaires developpee avec Spring Boot 4, " +
                                        "Spring Data JPA et H2."
                        )
                        .contact(new Contact()
                                .name("Banking Microservice Team")
                        )
                        .license(new License()
                                .name("API de demonstration pedagogique")
                        )
                );
    }
}
