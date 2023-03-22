package com.algaworks.algafood.core.springdoc;

import java.util.Arrays;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;

@Configuration
@SecurityScheme(
		  type = SecuritySchemeType.HTTP,
		  name = "basicAuth",
		  scheme = "basic")
public class SpringDocConfig {
	
    @Bean
    public OpenAPI openAPI() {
        Server localServer = new Server();
        localServer.setDescription("local");
        localServer.setUrl("http://localhost:8080");

        Server testServer = new Server();
        testServer.setDescription("test");
        testServer.setUrl("http://127.0.0.1:8080");

        OpenAPI openAPI = new OpenAPI();
        openAPI.info(new Info()
                .title("Tutorial Rest API")
                .description(
                "Documenting Spring Boot REST API with SpringDoc and OpenAPI 3 spec")
                .version("1.0.0")
                 .contact(new Contact().name("Marone").
                        url("https://wstutorial.com").email("test@wstutorial.com")));;
        openAPI.setServers(Arrays.asList(localServer, testServer));

        return openAPI;
    }

}

