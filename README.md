#### Handler 实现自定义协议的解析:

具体作业见

`org.geektimes.configuration.microprofile.config.annotation.DefaultConfigSourceFactory`

`sun.net.www.protocol.ttl.Handler`,使用 `getContent` 方法来获取内容

这两个类,实现了 ttl 协议的解析(取名无力).

===============================================================

#### @Repeatable 注解的作用

```java
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface ConfigSources {

    ConfigSource[] value();
}
```

```java
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Repeatable(ConfigSources.class)
public @interface ConfigSource {

    String name() default "";
    int ordinal() default DEFAULT_ORDINAL;
    String resource();
    String encoding() default "UTF-8";
    Class<? extends ConfigSourceFactory> factory() default ConfigSourceFactory.class;
}
```

关于@Repeatable 的注解,Oracle 官方的文档https://docs.oracle.com/javase/tutorial/java/annotations/repeating.html 里面有相关内容

下面将对@ConfigSources @ConfigSource这两个注解做相关解释

JDK7 Class和 Fileld 无法添加多次同样的注解.JDK8 针对这个问题进行了改进,使用了@Repeatable元注解,元注解里面指定的类,相当于一个容器,里面用来盛放对应的注解的数组,参见下面的代码

```java
@ConfigSources(value = {@ConfigSource(ordinal = 200, resource = "classpath:/META-INF/default.properties")
        , @ConfigSource(ordinal = 300, resource = "classpath:/META-INF/default2.properties")
})
public class ConfigSourcesTest {

}
```

在上面的代码中,通过`ConfigSourcesTest` 的`@ConfigSources`注解,把`@ConfigSource`数组放进`value()`方法

然后可以通过解析`@ConfigSources`注解,进而获得`@ConfigSource`注解数组,参见下面代码

```java
ConfigSources configSources = ConfigSourcesTest.class.getAnnotation(ConfigSources.class);
ConfigSource[] sources = configSources.value();
for (ConfigSource item : sources) {
    int ordinal = item.ordinal();
    String resource = item.resource();
    URL resourceURL = new URL(resource);

    String encoding = item.encoding();
    String name = item.name();
    Class<? extends ConfigSourceFactory> factory = item.factory();
    if (ConfigSourceFactory.class.equals(factory)) {
        factory = DefaultConfigSourceFactory.class;

    }
    ConfigSourceFactory configSourceFactory = factory.newInstance();
    org.eclipse.microprofile.config.spi.ConfigSource target = configSourceFactory.createConfigSource(name, ordinal, resourceURL, encoding);

    System.out.println(target.getProperties());
}
```

这本质上是 JDK8 的语法糖,反编译下面代码,能发现反编译的 Class 信息,得到的 RuntimeVisiableAnnotation 里面有`Lorg/geektimes/configuration/microprofile/config/annotation/ConfigSources;`

据此,得出结论,实际上,编译器是将多个`@ConfigSource`注解放入了`@ConfigSources`注解里面,从而实现了 JDK7 和 JDK8 兼容的问题

```java
@ConfigSource(ordinal = 200, resource = "classpath:/META-INF/default.properties")
@ConfigSource(ordinal = 200, resource = "classpath:/META-INF/default.properties")
@ConfigSource(ordinal = 300, resource = "classpath:/META-INF/default2.properties")
public class ConfigSourceTest {
```

