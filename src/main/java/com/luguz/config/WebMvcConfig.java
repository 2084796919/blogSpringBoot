package com.luguz.config;


import com.luguz.storage.server.LocalStorage;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Component
public class WebMvcConfig implements WebMvcConfigurer {
    /**
     * 添加静态资源文件，外部可以直接访问地址
     *
     * @param registry
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/static/**").addResourceLocations("classpath:/static/");

//        registry.addResourceHandler("/static/storage/**").addResourceLocations("file:" +"///F:/blog/blogSpringBoot-master/src/main/resources/static/storage/");
//        registry.addResourceHandler("/static/storage/**").addResourceLocations("file:" + System.getProperty("user.dir") +"/src/main/resources/static/storage/");
        String path = System.getProperty("user.dir") +"/src/main/resources/static/storage/";
//        获取操作系统类型
        String os = System.getProperty("os.name");
        if (os.toLowerCase().startsWith("win")) {
            registry.addResourceHandler("/static/storage/**").
                    addResourceLocations("file:"+path);
        }else{//linux和mac系统 可以根据逻辑再做处理
            registry.addResourceHandler("/static/storage/**").
                    addResourceLocations("file:"+path);
        }
    }

}