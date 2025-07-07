package com.syedwajahat01.PasswordManagerProject.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration for OpenAPI (Swagger) documentation.
 * This setup defines the API information and configures security so that
 * the Swagger UI can work with the application's session-based authentication.
 */
@Configuration
public class SwaggerConfig {

        @Bean
        public OpenAPI customOpenAPI() {
                // This is a reference name for the security scheme.
                final String securitySchemeName = "cookieAuth";

                return new OpenAPI()
                        // 1. Define general API information.
                        .info(new Info()
                                .title("Password Manager API")
                                .version("v1")
                                .description("API for a secure personal password manager. " +
                                        "Most endpoints require authentication via a session cookie, " +
                                        "which is set after a successful call to `/api/unlock`.")
                        )
                        // 2. Apply the security requirement globally to all endpoints.
                        .addSecurityItem(new SecurityRequirement().addList(securitySchemeName))
                        // 3. Define the security scheme component.
                        .components(new Components()
                                .addSecuritySchemes(securitySchemeName, new SecurityScheme()
                                        .name("JSESSIONID") // The name of the cookie to be sent
                                        .type(SecurityScheme.Type.APIKEY) // The type used for cookie-based auth
                                        .in(SecurityScheme.In.COOKIE)
                                        .description("Session cookie set after successful login via `/api/unlock`.")
                                )
                        );
        }
}