package com.example.springbootmybatis.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.core.env.Profiles;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.service.VendorExtension;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;

@Configuration
@EnableSwagger2
public class SwaggerConfig {

//    配置了Swagger的Docket的bean实例

    @Bean
    public Docket docket1(){
        return new Docket(DocumentationType.SWAGGER_2).groupName("Default");
    }


    @Bean
    public Docket docket(Environment environment){

        Profiles profiles = Profiles.of("dev");
        boolean flag = environment.acceptsProfiles(profiles);
        System.out.println(flag);

        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .enable(flag)
                .groupName("cx")
                .select()
                //RequestHandlerSelectors, 配置要扫描接口的方式
                //basePackage: 指定要扫描的包
                //any(): 扫描全部
                //none(): 不扫描
                //withClassAnnotation: 扫描类上的注解, e.g.
                //withMethodAnnotation: 扫描方法上的注解 e.g. GetMapping.class
                .apis(RequestHandlerSelectors.basePackage("com.example.springbootmybatis.controller"))
                // paths 过滤路径https://github.com/lllarcolais
                .paths(PathSelectors.any())
                .build();
    }

    private ApiInfo apiInfo(){

        Contact contact = new Contact("cx", "", "xc2601@columbia.edu");
        return new ApiInfo("user服务的SwaggerAPI文档",
                "Api Documentation",
                "1.0", "urn:tos",
                contact,
                "Apache 2.0",
                "http://www.apache.org/licenses/LICENSE-2.0",
                new ArrayList<VendorExtension>());
    }


}
