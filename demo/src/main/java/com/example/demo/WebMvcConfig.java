package com.example.demo;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.annotation.Bean;
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
//		registry.addConverter(new CustomDateConverter());
	}

	@Bean
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

	/**
	 * 这个会覆盖掉默认的converts, mvc debug 到有8个converts
	 *
	 * Spring Boot如果启用了WebMvcAutoConfiguration，则这个方法是在那10个默认converts后之后添加，
	 * 如果有多个MappingJackson2HttpMessageConverter，转换时ArrayList是用先找到的那个
	 *
	 * <p>
	 * <p>
	 * Configure the {@link HttpMessageConverter}s to use for reading or writing
	 * to the body of the request or response. If no converters are added, a
	 * default list of converters is registered.
	 * <p><strong>Note</strong> that adding converters to the list, turns off
	 * default converter registration. To simply add a converter without impacting
	 * default registration, consider using the method
	 * {@link #extendMessageConverters(List)} instead.
	 *
	 * @param converters initially an empty list of converters
	 */
//	@Override
//	public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
//		converters.add(customJackson2HttpMessageConverter());
//	}

	/**
	 * 已经存在MappingJackson2HttpMessageConverter, 修改这个的convert
	 * <p>
	 * A hook for extending or modifying the list of converters after it has been
	 * configured. This may be useful for example to allow default converters to
	 * be registered and then insert a custom converter through this method.
	 *
	 * @param converters the list of configured converters to extend.
	 * @since 4.1.3
	 */
	@Override
	public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
		for (HttpMessageConverter<?> converter : converters) {
			if (converter instanceof MappingJackson2HttpMessageConverter) {
				ObjectMapper objectMapper = new ObjectMapper();
				SimpleModule simpleModule = new SimpleModule();
				simpleModule.addDeserializer(Date.class, new CustomerDateDeserializer());
				objectMapper.registerModule(simpleModule);
				((MappingJackson2HttpMessageConverter) converter).setObjectMapper(objectMapper);
			}
		}
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
