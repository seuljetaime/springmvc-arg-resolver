

# 说明

此项目用于分析研究、调试Spring Web解析转换Web参数的流程。

从Spring Framework v5.0.9.RELEASE 复制过来，并且丢弃了git的历史来减少仓库的大小

其他版本的Spring请见[Spring的官方git地址](https://github.com/spring-projects/spring-framework)

此项目只增加自定义日志输出，打印出流程，用于理解分析代码流程



# 参考资料

[Spring-framework 5.0.2源码导入idea步骤及demo开发](https://zhouxiaowu.coding.me/2018/07/21/Spring-framework-5-0-2%E6%BA%90%E7%A0%81%E5%AF%BC%E5%85%A5idea%E6%AD%A5%E9%AA%A4%E5%8F%8Ademo%E5%BC%80%E5%8F%91/)

[spring-5-embedded-tomcat-8-gradle-tutorial](https://auth0.com/blog/spring-5-embedded-tomcat-8-gradle-tutorial/)

[深入Spring Boot：显式配置 @EnableWebMvc 导致静态资源访问失败](http://hengyunabc.github.io/spring-boot-enablewebmvc-static-404/)

# 步骤

1. 在Intellij IDEA Spring源码根目录中，右键新建Module，选择Gradle java和Web， ArtifactId随意。**此项目用demo表示ArtifactId**

2. 修改demo项目的build.gradle，在dependencies中增加

   ```java
   compile(project(":spring-webmvc"))
   ```

3. 在demo项目中的java新建package及controller，编写一个post API，代码见ApiController

4. 项目增加tomcat，具体请见 https://github.com/bmuschko/gradle-tomcat-plugin

5. 参考[spring-5-embedded-tomcat-8-gradle-tutorial](https://auth0.com/blog/spring-5-embedded-tomcat-8-gradle-tutorial/) 增加spring web支持

6. 已在master分支中调试出以下两个类

   HandlerMethodArgumentResolverComposite

   ServletInvocableHandlerMethod

7. 增加log4j2日志输出

   有warning日志，删除build.gradle中 compileJava.options 的-Werror设置，不然会启动不了

8. 使用demo gradle中的tomcatRun debug或者run运行项目

9. 使用curl或者postman发起post user json请求

   ```bash
   curl -X POST http://localhost:8080/demo/user -d '{"username": "123", "birthday": "2018-10-10 11:12:11"}' -H "Content-Type: application/json"
   ```

   IDE控制台会打印出log4j2日志

   ```
   16:39:11.345 [http-nio2-8080-exec-2] INFO  org.springframework.web.servlet.mvc.method.annotation.ServletInvocableHandlerMethod - invokeAndHandle
   16:39:11.346 [http-nio2-8080-exec-2] INFO  org.springframework.web.method.support.InvocableHandlerMethod - 判断是否支持解析参数
   16:39:11.356 [http-nio2-8080-exec-2] INFO  org.springframework.web.method.support.HandlerMethodArgumentResolverComposite - 使用class org.springframework.web.servlet.mvc.method.annotation.RequestResponseBodyMethodProcessor解析参数
   16:39:11.356 [http-nio2-8080-exec-2] INFO  org.springframework.web.method.support.InvocableHandlerMethod - 参数解析器准备解析参数
   16:39:11.356 [http-nio2-8080-exec-2] INFO  org.springframework.web.method.support.HandlerMethodArgumentResolverComposite - 使用class org.springframework.web.servlet.mvc.method.annotation.RequestResponseBodyMethodProcessor解析参数
   16:39:11.360 [http-nio2-8080-exec-2] INFO  org.springframework.web.servlet.mvc.method.annotation.RequestResponseBodyMethodProcessor - 准备转换参数
   16:39:11.366 [http-nio2-8080-exec-2] INFO  org.springframework.web.servlet.mvc.method.annotation.AbstractMessageConverterMethodArgumentResolver - Content-Type：application/json
   16:39:16.007 [http-nio2-8080-exec-2] INFO  org.springframework.web.servlet.mvc.method.annotation.AbstractMessageConverterMethodArgumentResolver - 使用class org.springframework.http.converter.json.MappingJackson2HttpMessageConverter Converter转换，hashcode为：233921890
   16:39:16.017 [http-nio2-8080-exec-2] INFO  org.springframework.http.converter.json.AbstractJackson2HttpMessageConverter - 使用objectMapper读取转换json成[simple type, class com.example.demo.UserBean]
   
   ```


# Spring Boot

spring boot项目configureMessageConverters 新增convert后，debug发现其他默认converts还存在

而此项目中的configureMessageConverters则只有一个convert



### Spring Boot MVC Auto-configuration 会自动配置默认的converts

https://docs.spring.io/spring-boot/docs/current/reference/htmlsingle/#boot-features-spring-mvc-auto-configuration

> Spring Boot provides auto-configuration for Spring MVC that works well with most applications.

> The auto-configuration adds the following features on top of Spring’s defaults:
> ......
> · Automatic registration of Converter, GenericConverter, and Formatter beans.
> · Support for HttpMessageConverters (covered later in this document).
> ......

> If you want to keep Spring Boot MVC features and you want to add additional MVC configuration (interceptors, formatters, view controllers, and other features), you can add your own @Configuration class of type WebMvcConfigurer but without @EnableWebMvc. If you wish to provide custom instances of RequestMappingHandlerMapping, RequestMappingHandlerAdapter, or ExceptionHandlerExceptionResolver, you can declare a WebMvcRegistrationsAdapter instance to provide such components.

> If you want to take complete control of Spring MVC, you can add your own @Configuration annotated with @EnableWebMvc.



https://docs.spring.io/spring-boot/docs/current/reference/htmlsingle/#howto-customize-the-responsebody-rendering

### 76.4 Customize the @ResponseBody Rendering

Spring uses `HttpMessageConverters` to render `@ResponseBody` (or responses from `@RestController`). You can contribute additional converters by adding beans of the appropriate type in a Spring Boot context. If a bean you add is of a type that would have been included by default anyway (such as `MappingJackson2HttpMessageConverter` for JSON conversions), it replaces the default value. A convenience bean of type `HttpMessageConverters` is provided and is always available if you use the default MVC configuration. It has some useful methods to access the default and user-enhanced message converters (For example, it can be useful if you want to manually inject them into a custom `RestTemplate`).

As in normal MVC usage, any `WebMvcConfigurer` beans that you provide can also contribute converters by overriding the `configureMessageConverters` method. However, unlike with normal MVC, you can supply only additional converters that you need (because Spring Boot uses the same mechanism to contribute its defaults). Finally, if you opt out of the Spring Boot default MVC configuration by providing your own `@EnableWebMvc` configuration, you can take control completely and do everything manually by using `getMessageConverters` from `WebMvcConfigurationSupport`.

See the [`WebMvcAutoConfiguration`](https://github.com/spring-projects/spring-boot/tree/v2.0.5.RELEASE/spring-boot-project/spring-boot-autoconfigure/src/main/java/org/springframework/boot/autoconfigure/web/servlet/WebMvcAutoConfiguration.java) source code for more details.



### 76.3 Customize the Jackson ObjectMapper



## 自定义json转换

### HttpMessageConverters

Spring MVC uses the `HttpMessageConverter` interface to convert HTTP requests and responses. Sensible defaults are included out of the box. For example, objects can be automatically converted to JSON (by using the Jackson library) or XML (by using the Jackson XML extension, if available, or by using JAXB if the Jackson XML extension is not available). By default, strings are encoded in `UTF-8`.

If you need to add or customize converters, you can use Spring Boot’s `HttpMessageConverters` class, as shown in the following listing:

```java
import org.springframework.boot.autoconfigure.web.HttpMessageConverters;
import org.springframework.context.annotation.*;
import org.springframework.http.converter.*;

@Configuration
public class MyConfiguration {

	@Bean
	public HttpMessageConverters customConverters() {
		HttpMessageConverter<?> additional = ...
		HttpMessageConverter<?> another = ...
		return new HttpMessageConverters(additional, another);
	}

}
```

Any `HttpMessageConverter` bean that is present in the context is added to the list of converters. You can also override default converters in the same way.

### Custom JSON Serializers and Deserializers

If you use Jackson to serialize and deserialize JSON data, you might want to write your own `JsonSerializer` and `JsonDeserializer` classes. Custom serializers are usually [registered with Jackson through a module](https://github.com/FasterXML/jackson-docs/wiki/JacksonHowToCustomSerializers), but Spring Boot provides an alternative `@JsonComponent` annotation that makes it easier to directly register Spring Beans.

You can use the `@JsonComponent` annotation directly on `JsonSerializer` or `JsonDeserializer` implementations. You can also use it on classes that contain serializers/deserializers as inner classes, as shown in the following example:

```java
import java.io.*;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import org.springframework.boot.jackson.*;

@JsonComponent
public class Example {

	public static class Serializer extends JsonSerializer<SomeObject> {
		// ...
	}

	public static class Deserializer extends JsonDeserializer<SomeObject> {
		// ...
	}

}
```

All `@JsonComponent` beans in the `ApplicationContext` are automatically registered with Jackson. Because `@JsonComponent` is meta-annotated with `@Component`, the usual component-scanning rules apply.

Spring Boot also provides [`JsonObjectSerializer`](https://github.com/spring-projects/spring-boot/tree/v2.0.5.RELEASE/spring-boot-project/spring-boot/src/main/java/org/springframework/boot/jackson/JsonObjectSerializer.java) and [`JsonObjectDeserializer`](https://github.com/spring-projects/spring-boot/tree/v2.0.5.RELEASE/spring-boot-project/spring-boot/src/main/java/org/springframework/boot/jackson/JsonObjectDeserializer.java) base classes that provide useful alternatives to the standard Jackson versions when serializing objects. See [`JsonObjectSerializer`](https://docs.spring.io/spring-boot/docs/2.0.5.RELEASE/api/org/springframework/boot/jackson/JsonObjectSerializer.html) and [`JsonObjectDeserializer`](https://docs.spring.io/spring-boot/docs/2.0.5.RELEASE/api/org/springframework/boot/jackson/JsonObjectDeserializer.html) in the Javadoc for details.






