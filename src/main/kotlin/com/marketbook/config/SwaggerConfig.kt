package com.marketbook.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer
import springfox.documentation.builders.ApiInfoBuilder
import springfox.documentation.builders.PathSelectors
import springfox.documentation.builders.RequestHandlerSelectors
import springfox.documentation.spi.DocumentationType
import springfox.documentation.spring.web.plugins.Docket
import springfox.documentation.swagger2.annotations.EnableSwagger2

@Profile("!prod")
@Configuration
@EnableSwagger2
class SwaggerConfig : WebMvcConfigurer {

    @Bean()
    fun api(): Docket = Docket(DocumentationType.SWAGGER_2)
        .select()
        .apis(RequestHandlerSelectors.basePackage("com.marketbook.controller"))
        .paths(PathSelectors.any())
        .build()
        .apiInfo(
            ApiInfoBuilder()
                .title("Market Book")
                .description("API Market Book")
                .build()
        )

}