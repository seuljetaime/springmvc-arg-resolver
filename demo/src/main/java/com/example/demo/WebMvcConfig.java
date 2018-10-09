package com.example.demo;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * 在这里进行Spring MVC的配置, 如 interceptor, messageConverter, formatter, viewResolver等
 *
 * @author <a href="mailto:ningyaobai@gzkit.com.cn">bernix</a>
 * 星期二, 九月 19, 2017
 * @version 1.0
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

	private final Log log = LogFactory.getLog("demo");

	@Override
	public void addFormatters(FormatterRegistry registry) {
		registry.addConverter(new CustomDateConverter());
	}

	//	@Bean
	public MappingJackson2HttpMessageConverter customJackson2HttpMessageConverter() {
		MappingJackson2HttpMessageConverter jsonConverter = new MappingJackson2HttpMessageConverter();
		ObjectMapper objectMapper = new ObjectMapper();
		SimpleModule simpleModule = new SimpleModule();
		simpleModule.addDeserializer(Date.class, new CustomerDateDeserializer());
		objectMapper.registerModule(simpleModule);
		jsonConverter.setObjectMapper(objectMapper);
		jsonConverter.setPrettyPrint(true);
		log.info("自定义的MappingJackson2HttpMessageConverter hashcode为： " + jsonConverter.hashCode());
		return jsonConverter;
	}

//	@Override
//	protected void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
//		converters.add(customJackson2HttpMessageConverter());
//		super.addDefaultHttpMessageConverters(converters);
//	}

	@Override
	public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
		converters.add(customJackson2HttpMessageConverter());
	}

	class CustomerDateDeserializer extends JsonDeserializer<Date> {
		@Override
		public Date deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String date = jsonParser.getText();
			try {
				return format.parse(date);
			} catch (ParseException e) {
				e.printStackTrace();
				throw new RuntimeException("json日期转换错误，值为： " + date);
			}
		}
	}
}
