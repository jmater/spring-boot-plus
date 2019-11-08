本测试项目基于Spring Boot Plus 脚手架项目,已经修改为使用内存数据库进行测试,
主要测试代码位于src/main/java/io.geekidea.springbootplus.test下面。
测试代码位于 src/test/io.geekidea.springbootplus下面
(1)项目导入到ide当中，注意是maven项目
(2)mvn clean compile 进行编译
(3)启用redis 2.8以上版本，默认端口6479，密码：0ac94e4e647e4b3d95e9de1ca116b5a2
(4)运行 SpringBootPlusApplication.java 启动项目
(5)默认运行端口8888.
(6)项目context path 是 /  ，访问url： http://127.0.0.1:8888/,已经集成了spring-boot-admin。
(7)swagger文档路径  http://127.0.0.1:8888/docs
(8)账户测试程序： TestAcct。  并发测试程序：TestAcctLocker

### Technology stack 
Component| Version |  Remark
-|-|-
Spring Boot | 2.2.0.RELEASE | Latest release stable version |
Spring Framework | 5.2.0.RELEASE | Latest release stable version |
Mybatis | 3.5.2 | DAO Framework |
Mybatis Plus | 3.2.0 | mybatis Enhanced framework |
Alibaba Druid | 1.1.20 | Data source |
Fastjson | 1.2.62 | JSON processing toolset |
swagger2 | 2.6.1 | Api document generation tool |
commons-lang3 | 3.9 | Apache language toolkit |
commons-io | 2.6 | Apache IO Toolkit |
commons-codec | 1.13 | Apache Toolkit such as encryption and decryption |
commons-collections4 | 4.4 | Apache collections toolkit |
reflections | 0.9.11 | Reflection Toolkit  |
hibernate-validator | 6.0.17.Final | Validator toolkit |
Shiro | 1.4.1 | Permission control |
JWT | 3.8.3 | JSON WEB TOKEN |
hutool-all | 5.0.3 | Common toolset |
lombok | 1.18.10 | Automatically plugs |
mapstruct | 1.3.1.Final | Object property replication tool |

## License
spring-boot-plus is under the Apache 2.0 license. See the [LICENSE](https://github.com/geekidea/spring-boot-plus/blob/master/LICENSE) file for details.

