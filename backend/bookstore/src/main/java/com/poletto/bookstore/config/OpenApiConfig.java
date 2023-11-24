package com.poletto.bookstore.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;

@Configuration
public class OpenApiConfig {
	
	@Bean
	OpenAPI testOpenAPIDefinition() {
		
	    final String securitySchemeName = "bearerAuth";

	    return new OpenAPI()
	    		.addSecurityItem(
	    			new SecurityRequirement().addList(securitySchemeName)
	    		)
	            .components(
            		new Components()
            			.addSecuritySchemes(
        					securitySchemeName,
        					new SecurityScheme()
	            				.name(securitySchemeName)
	            				.type(SecurityScheme.Type.HTTP)
	            				.scheme("bearer")
	                            .bearerFormat("JWT"))
            	)
	            .info(new Info()
            		.title("Poletto Bookstore API")
                    .contact(new Contact()
	                    		.name("Poletto Software")
	                    		.email("polettobookstore@gmail.com"))
                    .description("API que sustenta o backend e transição de dados da Poletto Bookstore, "
	                    	   + "armazenando os dados das entidades abstraídas da área de negócio "
	                    	   + "como livros, reservas, usuários e etc.")
                    .version("v3.1.4")
                 )
	            .externalDocs(
            		new ExternalDocumentation()
	            		.description("Postman Doc")
	                    .url("https://documenter.getpostman.com/view/25349262/2s9YJjSz2J")
	            );
	}

}
