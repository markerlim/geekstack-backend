package com.geekstack.cards.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;

@Configuration
public class DateTimeConverterConfig implements WebMvcConfigurer {
    
    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverter(new Converter<Date, ZonedDateTime>() {
            @Override
            public ZonedDateTime convert(Date source) {
                return source.toInstant().atZone(ZoneId.systemDefault());
            }
        });
        
        registry.addConverter(new Converter<ZonedDateTime, Date>() {
            @Override
            public Date convert(ZonedDateTime source) {
                return Date.from(source.toInstant());
            }
        });
    }
}