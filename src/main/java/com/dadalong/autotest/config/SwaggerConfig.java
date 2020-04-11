package com.dadalong.autotest.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * Swagger2接口管理配置类
 * "@Api(value="/", description = "接口文档描述")"
 * "@ApiOperation(value="接口名称", httpMethod="请求方法", notes="接口详细描述")"
 */
@Configuration
@EnableSwagger2
public class SwaggerConfig {

    @Bean
    public Docket api() {

        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.dadalong.autotest"))
                .paths(PathSelectors.any())
                .build();
//        return new Docket(DocumentationType.SWAGGER_2)
//                .apiInfo(apiInfo())
//                .pathMapping("/")//根目录
//                .select()
//                .apis(RequestHandlerSelectors.basePackage("com.dadalong.autotest"))
//                .apis(RequestHandlerSelectors.any())// 对所有api进行监控
//                .paths(Predicates.not(PathSelectors.regex("/error.*")))//错误路径不监控
//                .paths(PathSelectors.regex("/.*"))//选择器，正则匹配访问的路径
//                .build();
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder().title("我的接口文档")//接口文档标题
                .contact(new Contact("dadalong","","yinlongnv@foxmail.com"))//创建者信息
                .description("这是大大龙的接口文档")//接口文档描述
                .version("1.0.0.0")//接口文档版本号
                .build();
    }
}
