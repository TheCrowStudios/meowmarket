package com.thecrowstudios.meowmarket;

import java.io.File;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.resource.PathResourceResolver;

import com.thecrowstudios.meowmarket.metrics.VisitTrackingInterceptor;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Autowired
    private VisitTrackingInterceptor visitTrackingInterceptor;

    @Value("${upload.path}")
    private String uploadPath;

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**")
                .allowedOrigins("*")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*");
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        String absolutePath = new File(uploadPath).getAbsolutePath();
        registry.addResourceHandler("/upload-dir/**")
                .addResourceLocations("file:" + absolutePath + "/")
                .setCachePeriod(86400 * 30)
                .resourceChain(true)
                .addResolver(new PathResourceResolver());

        registry.addResourceHandler("/images/**")
        .addResourceLocations("classpath:/static/images/")
        .setCachePeriod(86400 * 30)
        .resourceChain(false)
        .addResolver(new PathResourceResolver());
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(visitTrackingInterceptor).addPathPatterns("/**").excludePathPatterns("/actuator/**",
                "/metrics/**", "/images/**", "/scripts/**", "/upload-dir/**", "/style.css");
    }
}
