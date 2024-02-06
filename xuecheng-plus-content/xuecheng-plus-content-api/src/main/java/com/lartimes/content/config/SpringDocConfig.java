package com.lartimes.content.config;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Lartimes
 * @version 1.0
 * @description:
 * @since 2024/2/6 12:41
 */
@Configuration
public class SpringDocConfig {

    @Bean
    public OpenAPI springShopOpenAPI() {
        return new OpenAPI()
                .info(new Info().title("学成在线内容管理系统")
                        .description("内容系统管理系统对课程相关信息进行管理")
                        .version("v0.0.1")
                        .license(new License().name("Apache 2.0").url("https://github.com/macrozheng/mall-learning")))
                        .externalDocs(new ExternalDocumentation()
                            .description("SpringShop Wiki Documentation")
                            .url("https://springshop.wiki.github.org/docs"));
    }




}
