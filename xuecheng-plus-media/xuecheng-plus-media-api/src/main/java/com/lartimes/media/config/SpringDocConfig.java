package com.lartimes.media.config;

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

//    http://localhost:63040/content/swagger-ui/index.html
//   uri/{context-path}/swagger-ui/index.html
    @Bean
    public OpenAPI springShopOpenAPI() {
        return new OpenAPI()
                .info(new Info().title("学成在线媒资管理系统")
                        .description("媒资管理")
                        .version("v0.0.1")
                        .license(new License().name("Apache 2.0").url("https://github.com/macrozheng/mall-learning")))
                        .externalDocs(new ExternalDocumentation()
                            .description("SpringShop Wiki Documentation")
                            .url("https://springshop.wiki.github.org/docs"));
    }




}
