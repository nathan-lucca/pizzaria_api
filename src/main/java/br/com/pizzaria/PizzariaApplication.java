package br.com.pizzaria;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.RestController;
import org.springdoc.core.models.GroupedOpenApi;

@SpringBootApplication
@RestController
public class PizzariaApplication {

    public static void main(String[] args) {
        SpringApplication.run(PizzariaApplication.class, args);
    }

    @Bean
    public GroupedOpenApi groupedOpenApi() {
        return GroupedOpenApi.builder()
                .group("pizzaria")
                .packagesToScan("br.com.pizzaria")
                .build();
    }
}
