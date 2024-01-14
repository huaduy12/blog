package com.example.blog;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@OpenAPIDefinition(
        info = @Info(
                title = "Spring boot rest api title blog app",
                description = "Spring boot rest api description blog app",
                version = "v1.0",
                contact = @Contact(
                        name = "Hứa Như Duy",
                        email = "Huaduy2k2@gmail.com",
                        url = "https://www.facebook.com/duyhua01"
                ),
                license = @License(
                        name = "Apache fake 2.0",
                        url = "https://www.huaduy.com/license"
                )
        ),
        externalDocs = @ExternalDocumentation(
                description = "Spring boot rest api external blog app",
                url = "https://github.com/huaduy12"
        )
)
public class BlogApplication {

    public static void main(String[] args) {
        SpringApplication.run(BlogApplication.class, args);
    }

}
