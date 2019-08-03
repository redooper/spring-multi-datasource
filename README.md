# spring-multi-datasource
Spring动态数据源轻量级实现



## 一、项目特性

- 自动配置，开箱即用
- 支持LookupKey注解嵌套使用
- 支持LookupKey注解于方法上和类上
- 支持Hikari和Druid数据源



## 二、示例程序

https://github.com/redooper/demo-parent/tree/master/spring-multi-datasource-demo



## 三、使用步骤

1、克隆项目

```shell
git clone https://github.com/redooper/spring-multi-datasource.git
```



2、自定义`spring-boot`版本

```xml
<spring-boot.version>2.1.2.RELEASE</spring-boot.version>
```



3、打包到本地

```shell
mvn clean install -Dmaven.test.skip=true
```



4、项目中添加依赖

```xml
<dependency>
    <groupId>com.redooper</groupId>
    <artifactId>spring-multi-datasource</artifactId>
    <version>1.0.0-SNAPSHOT</version>
</dependency>
```



5、多数据源配置

**注意**：`Hikari`数据源使用`jdbc-url`配置项，`Druid`数据源使用`url`配置项

```yaml
spring:
  multi-datasource:
    defaultTargetDataSource: db_foo
    targetDataSources:
      db_foo:
        driver-class-name: com.mysql.cj.jdbc.Driver
#        jdbc-url: jdbc:mysql://localhost:3306/db_foo
        url: jdbc:mysql://localhost:3306/db_foo
        username: root
        password: songshu
      db_bar:
        driver-class-name: com.mysql.cj.jdbc.Driver
#        jdbc-url: jdbc:mysql://localhost:3306/db_bar
        url: jdbc:mysql://localhost:3306/db_bar
        username: root
        password: songshu
```



6、建议定义数据源枚举类

```java
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class LookupKeyEnums {

    public static final String DB_FOO = "db_foo";

    public static final String DB_BAR = "db_bar";

}
```



7、在目标方法或类上添加`@LookupKey`注解

**取值优先级**：方法上的注解属性值 > 类上的注解属性值 > 默认数据源`defaultTargetDataSource`

```java
@Slf4j
@Service
@LookupKey(LookupKeyEnums.DB_FOO)
public class FooServiceImpl implements FooService {
...
}
```

```java
@Override
@LookupKey(LookupKeyEnums.DB_BAR)
public Iterable<Bar> findAll() {
    return barRepository.findAll();
}
```



## 四、测试用例

```java
@Slf4j
@SpringBootApplication
public class MultiDataSourceApplication implements ApplicationRunner {

    @Autowired
    private FooService fooService;

    @Autowired
    private BarService barService;

    public static void main(String[] args) {
        SpringApplication.run(MultiDataSourceApplication.class, args);
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        // 1.测试LookupKey注解于类上
        Iterable<Foo> foos = fooService.findAll();
        foos.forEach(foo -> log.info("Foo: {}", JSON.toJSONString(foo)));

        // 2.测试LookupKey注解于方法上
        Iterable<Bar> bars = barService.findAll();
        bars.forEach(bar -> log.info("Bar: {}", JSON.toJSONString(bar)));

        // 3.测试LookupKey嵌套使用
        fooService.findAll(0);

        // 4.测试事务
        Snowflake snowflake = IdUtil.getSnowflake(0, 0);
        Foo foo = Foo.builder()
                .id(snowflake.nextId())
                .name("Jackie")
                .age(28)
                .tableName("t_foo")
                .build();
        fooService.save(foo);
    }
}
```

