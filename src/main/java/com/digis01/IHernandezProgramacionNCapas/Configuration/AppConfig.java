package com.digis01.IHernandezProgramacionNCapas.Configuration;

import jakarta.servlet.http.HttpSession;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class AppConfig 
{
    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder, HttpSession session)
    {
        RestTemplate restTemplate = builder
                .additionalInterceptors((request, body, execution) -> 
                {
                    String token = (String) session.getAttribute("token");
                    if (token != null && !token.isEmpty()) 
                    {
                        request.getHeaders().add("Authorization", "Bearer " + token);
                    }
                    return execution.execute(request, body);
        }).build();
        return restTemplate;
    }
}