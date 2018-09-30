# springmvc-arg-resolver
项目功能：增加日志，调试Spring MVC解析Date参数的功能

# 步骤
1. 增加日志输出
application.properties 增加spring的日志
logging.level.org.springframework.web=TRACE

2. 编写测试
DemoApplicationTests 编写mockMVC测试用例

3. 一些日志信息
```java
2018-09-30 17:27:33.354 DEBUG 2598 --- [           main] s.w.s.m.m.a.RequestMappingHandlerMapping : Looking up handler method for path /user
2018-09-30 17:27:33.355 TRACE 2598 --- [           main] s.w.s.m.m.a.RequestMappingHandlerMapping : Found 1 matching mapping(s) for [/user] : [{[/user],methods=[POST]}]
2018-09-30 17:27:33.356 DEBUG 2598 --- [           main] s.w.s.m.m.a.RequestMappingHandlerMapping : Returning handler method [public com.example.demo.UserBean com.example.demo.ApiController.postUser(com.example.demo.UserBean)]
2018-09-30 17:27:33.362 TRACE 2598 --- [           main] s.HandlerMethodArgumentResolverComposite : Testing if argument resolver [org.springframework.web.method.annotation.RequestParamMethodArgumentResolver@49aa766b] supports [class com.example.demo.UserBean]
2018-09-30 17:27:33.363 TRACE 2598 --- [           main] s.HandlerMethodArgumentResolverComposite : Testing if argument resolver [org.springframework.web.method.annotation.RequestParamMapMethodArgumentResolver@4d23015c] supports [class com.example.demo.UserBean]
2018-09-30 17:27:33.364 TRACE 2598 --- [           main] s.HandlerMethodArgumentResolverComposite : Testing if argument resolver [org.springframework.web.servlet.mvc.method.annotation.PathVariableMethodArgumentResolver@383f1975] supports [class com.example.demo.UserBean]
2018-09-30 17:27:33.364 TRACE 2598 --- [           main] s.HandlerMethodArgumentResolverComposite : Testing if argument resolver [org.springframework.web.servlet.mvc.method.annotation.PathVariableMapMethodArgumentResolver@26dcd8c0] supports [class com.example.demo.UserBean]
2018-09-30 17:27:33.364 TRACE 2598 --- [           main] s.HandlerMethodArgumentResolverComposite : Testing if argument resolver [org.springframework.web.servlet.mvc.method.annotation.MatrixVariableMethodArgumentResolver@66e889df] supports [class com.example.demo.UserBean]
2018-09-30 17:27:33.364 TRACE 2598 --- [           main] s.HandlerMethodArgumentResolverComposite : Testing if argument resolver [org.springframework.web.servlet.mvc.method.annotation.MatrixVariableMapMethodArgumentResolver@773c0293] supports [class com.example.demo.UserBean]
2018-09-30 17:27:33.364 TRACE 2598 --- [           main] s.HandlerMethodArgumentResolverComposite : Testing if argument resolver [org.springframework.web.servlet.mvc.method.annotation.ServletModelAttributeMethodProcessor@55b8dbda] supports [class com.example.demo.UserBean]
2018-09-30 17:27:33.364 TRACE 2598 --- [           main] s.HandlerMethodArgumentResolverComposite : Testing if argument resolver [org.springframework.web.servlet.mvc.method.annotation.RequestResponseBodyMethodProcessor@3b569985] supports [class com.example.demo.UserBean]
2018-09-30 17:27:33.414 DEBUG 2598 --- [           main] m.m.a.RequestResponseBodyMethodProcessor : Read [class com.example.demo.UserBean] as "application/json;charset=UTF-8" with [org.springframework.http.converter.json.MappingJackson2HttpMessageConverter@5d332969]
2018-09-30 17:27:33.428 DEBUG 2598 --- [           main] .w.s.m.m.a.ServletInvocableHandlerMethod : Failed to resolve argument 0 of type 'com.example.demo.UserBean'

org.springframework.http.converter.HttpMessageNotReadableException: JSON parse error: Cannot deserialize value of type `java.util.Date` from String "2018-01-01 10:12:11": not a valid representation (error: Failed to parse Date value '2018-01-01 10:12:11': Cannot parse date "2018-01-01 10:12:11": while it seems to fit format 'yyyy-MM-dd'T'HH:mm:ss.SSSZ', parsing fails (leniency? null)); nested exception is com.fasterxml.jackson.databind.exc.InvalidFormatException: Cannot deserialize value of type `java.util.Date` from String "2018-01-01 10:12:11": not a valid representation (error: Failed to parse Date value '2018-01-01 10:12:11': Cannot parse date "2018-01-01 10:12:11": while it seems to fit format 'yyyy-MM-dd'T'HH:mm:ss.SSSZ', parsing fails (leniency? null))
```

4. 找到关键类
HandlerMethodArgumentResolverComposite
ServletInvocableHandlerMethod
将源码包导入到项目中，准备增加日志