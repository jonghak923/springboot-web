package com.jonghak.springbootweb.config;

import org.apache.tika.Tika;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.util.UrlPathHelper;

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
}
