

# 说明

此项目用于分析研究、调试Spring Web解析转换Web参数的流程。

从Spring Framework v5.0.9.RELEASE 复制过来，并且丢弃了git的历史来减少仓库的大小

其他版本的Spring请见[Spring的官方git地址](https://github.com/spring-projects/spring-framework)

此项目只增加自定义日志输出，打印出流程，用于理解分析代码流程



# 参考资料

[Spring-framework 5.0.2源码导入idea步骤及demo开发](https://zhouxiaowu.coding.me/2018/07/21/Spring-framework-5-0-2%E6%BA%90%E7%A0%81%E5%AF%BC%E5%85%A5idea%E6%AD%A5%E9%AA%A4%E5%8F%8Ademo%E5%BC%80%E5%8F%91/)

[spring-5-embedded-tomcat-8-gradle-tutorial](https://auth0.com/blog/spring-5-embedded-tomcat-8-gradle-tutorial/)

# 步骤

1. 在Intellij IDEA Spring源码根目录中，右键新建Module，选择Gradle java和Web， ArtifactId随意。**此项目用demo表示ArtifactId**

2. 修改demo项目的build.gradle，在dependencies中增加

   ```java
   compile(project(":spring-webmvc"))
   ```

3. 在demo项目中的java新建package及controller，编写一个post API，代码见ApiController
4. 项目增加tomcat，具体请见 https://github.com/bmuschko/gradle-tomcat-plugin
5. 