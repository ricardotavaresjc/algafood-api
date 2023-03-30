package com.algaworks.algafood.core.openapi;

import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.algaworks.algafood.api.exceptionhandller.Problem;
import com.algaworks.algafood.core.openapi.model.PageableModelOpenApi;
import com.fasterxml.classmate.TypeResolver;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import springfox.bean.validators.configuration.BeanValidatorPluginsConfiguration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RepresentationBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.builders.ResponseBuilder;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.service.Response;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.json.JacksonModuleRegistrar;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
@Import(BeanValidatorPluginsConfiguration.class)
public class SpringFoxConfig implements WebMvcConfigurer{

	
	@Bean
	public Docket apiDocket() {
		var typeResolver = new TypeResolver();
	    return new Docket(DocumentationType.OAS_30)
	        .select()
	          .apis(RequestHandlerSelectors.basePackage("com.algaworks.algafood.api"))
	          .paths(PathSelectors.any())
	          .build()
	          .useDefaultResponseMessages(false)
	          .globalResponses(HttpMethod.GET, globalGetResponseMessages())
	          .globalResponses(HttpMethod.POST, globalPostResponseMessages())
	          .globalResponses(HttpMethod.PUT, globalPutResponseMessages())
	          .globalResponses(HttpMethod.DELETE, globalDeleteResponseMessages())
	          .additionalModels(typeResolver.resolve(Problem.class))
	          .directModelSubstitute(Pageable.class, PageableModelOpenApi.class)
	         .apiInfo(apiInfo());
	  }
	
	private List<Response> globalGetResponseMessages(){
		return Arrays.asList(
				new ResponseBuilder()
				.code(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()))
				.description("Internal Server Error")
                .representation(MediaType.APPLICATION_JSON )
                .apply(getProblemaModelReference())
				.build(),
				new ResponseBuilder()
				.code(String.valueOf(HttpStatus.NOT_ACCEPTABLE.value()))
				.description("Recurso não possui representação que pode ser aceita pelo consumidor")
				.build()
				);
				
	}
	
	private List<Response> globalPostResponseMessages(){
		return Arrays.asList(
				new ResponseBuilder()
				.code(String.valueOf(HttpStatus.BAD_REQUEST.value()))
				.description("Bad Request")
                .representation(MediaType.APPLICATION_JSON )
                .apply(getProblemaModelReference())
				.build(),
				new ResponseBuilder()
				.code(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()))
				.description("Internal Server Error")
                .representation(MediaType.APPLICATION_JSON )
                .apply(getProblemaModelReference())
				.build(),
				new ResponseBuilder()
				.code(String.valueOf(HttpStatus.NOT_ACCEPTABLE.value()))
				.description("Not Acceptable")
				.build(),
				new ResponseBuilder()
				.code(String.valueOf(HttpStatus.UNSUPPORTED_MEDIA_TYPE.value()))
				.description("Unsupported Media Type")
                .representation(MediaType.APPLICATION_JSON )
                .apply(getProblemaModelReference())
				.build()
				);
	}
	
	private List<Response> globalPutResponseMessages(){
		return Arrays.asList(
				new ResponseBuilder()
				.code(String.valueOf(HttpStatus.BAD_REQUEST.value()))
				.description("Bad Request")
                .representation(MediaType.APPLICATION_JSON )
                .apply(getProblemaModelReference())
				.build(),
				new ResponseBuilder()
				.code(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()))
				.description("Internal Server Error")
                .representation(MediaType.APPLICATION_JSON )
                .apply(getProblemaModelReference())
				.build(),
				new ResponseBuilder()
				.code(String.valueOf(HttpStatus.NOT_ACCEPTABLE.value()))
				.description("Not Acceptable")
				.build(),
				new ResponseBuilder()
				.code(String.valueOf(HttpStatus.UNSUPPORTED_MEDIA_TYPE.value()))
				.description("Unsupported Media Type")
                .representation(MediaType.APPLICATION_JSON )
                .apply(getProblemaModelReference())
				.build()
				);
	}
	
	private List<Response> globalDeleteResponseMessages(){
		return Arrays.asList(
				new ResponseBuilder()
				.code(String.valueOf(HttpStatus.BAD_REQUEST.value()))
				.description("Bad Request")
                .representation(MediaType.APPLICATION_JSON )
                .apply(getProblemaModelReference())
				.build(),
				new ResponseBuilder()
				.code(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()))
				.description("Internal Server Error")
                .representation(MediaType.APPLICATION_JSON )
                .apply(getProblemaModelReference())
				.build()
				);
	}
	
	private ApiInfo apiInfo() {
		return new ApiInfoBuilder()
				.title("Algafood API")
				.description("Api aberta para clientes e restaurantes")
				.version("1")
				.contact(new Contact("Algaworks", "https://www.algaworks.com", "contato@algaworks.com"))
				.build();
	}
	
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("swagger-ui.html")
			.addResourceLocations("classpath:/META-INF/resources/");
		
		registry.addResourceHandler("/webjars/**")
			.addResourceLocations("classpath:/META-INF/resources/webjars/");
	}
	
	@Bean
	public JacksonModuleRegistrar springFoxJacksonConfig() {
		return objectMapper -> objectMapper.registerModule(new JavaTimeModule());
	}
	
	private Consumer<RepresentationBuilder> getProblemaModelReference() {
	    return r -> r.model(m -> m.name("Problema")
	            .referenceModel(ref -> ref.key(k -> k.qualifiedModelName(
	                    q -> q.name("Problema").namespace("com.algaworks.algafood.api.exceptionhandller")))));
	}
}
