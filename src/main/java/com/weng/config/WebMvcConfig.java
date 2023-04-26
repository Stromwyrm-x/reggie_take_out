package com.weng.config;

import com.weng.common.JacksonObjectMapper;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer
{
    @Override
    public void extendMessageConverters(List<HttpMessageConverter<?>> converters)
    {
        //创建消息转换器
        MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter=new MappingJackson2HttpMessageConverter();
        //设置对象转换器，底层使用Jackson将Java转换为json
        mappingJackson2HttpMessageConverter.setObjectMapper(new JacksonObjectMapper());
        //将上面的消息转换器对象追加到mvc框架的转换器集合中
        converters.add(0,mappingJackson2HttpMessageConverter);
    }
}
