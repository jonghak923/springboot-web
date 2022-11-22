package com.jonghak.springbootweb.config;

import org.apache.tika.Tika;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.util.UrlPathHelper;

import java.util.List;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    /**
     * URI패턴의 @MatrixVariable을 사용하기 위해 URI의 ";" 제거 설정을 false로 변경한다.
     */
    @Override
    public void configurePathMatch(PathMatchConfigurer configurer) {
        UrlPathHelper urlPathHelper = new UrlPathHelper();
        urlPathHelper.setRemoveSemicolonContent(false);
        configurer.setUrlPathHelper(urlPathHelper);
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new VisitTimeInterceptor());
    }

    /**
     * - 공통으로 사용할 라이브러리 bean으로 등록
     * - controller 및 service 에서 사용 방법
     *      @Autowired
     *      Tika tika;
     *
     */
    @Bean
    public Tika setConfigTika(){
        return new Tika();
    }

    /**
     * - HttpMessageConverter
     *  ● 스프링 MVC 설정 (WebMvcConfigurer)에서 설정할 수 있다.
     *  ● configureMessageConverters: 기본 메시지 컨버터 대체
     *  ● extendMessageConverters: 메시지 컨버터에 추가 (기본 메세지 컨버터 + )
     *  ● 기본 컨버터
     *      ○ WebMvcConfigurationSupport.addDefaultHttpMessageConverters
     */
    @Override
    public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
        WebMvcConfigurer.super.extendMessageConverters(converters);
    }

    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        WebMvcConfigurer.super.configureMessageConverters(converters);
    }
}
