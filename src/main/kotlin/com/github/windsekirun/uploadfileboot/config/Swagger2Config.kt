package com.github.windsekirun.uploadfileboot.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import springfox.documentation.builders.ApiInfoBuilder
import springfox.documentation.builders.PathSelectors
import springfox.documentation.builders.RequestHandlerSelectors
import springfox.documentation.service.ApiInfo
import springfox.documentation.service.Contact
import springfox.documentation.spi.DocumentationType
import springfox.documentation.spring.web.plugins.Docket
import springfox.documentation.swagger2.annotations.EnableSwagger2

@Configuration
@EnableSwagger2
class Swagger2Config {

    @Bean
    fun api(): Docket {
        return Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.github.windsekirun.uploadfileboot.controller"))
                .paths(PathSelectors.regex("/.*"))
                .build()
                .apiInfo(apiInfo())
    }

    private fun apiInfo(): ApiInfo {
        return ApiInfoBuilder().apply {
            title("UploadFileBoot API")
            description("API Endpoint for serving file")
            contact(Contact("Pyxis", "https://uzuki.live", "pyxis@uzuki.live"))
            license("MIT License")
            licenseUrl("https://github.com/WindSekirun/UploadFileBoot/blob/master/LICENSE")
        }.build()
    }
}