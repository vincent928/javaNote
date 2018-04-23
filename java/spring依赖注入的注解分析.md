#spring注入的注解

------
>@Autowired
*org.springframework.bean.factory
*spring
@Autowired是spring提供的注解，通过`AutowiredAnnotationBeanPostProcessor`类实现的依赖注入，与@Inject两者可互换使用
@Autowired有个`required`属性，可以配置为false。若配置为false后，spring上下文没有找到对应的bean时，不会抛错误，会让这个
bean处于未装配状态。如果代码没有进行null检查的话，这个未装配状态的属性可能会出现`NullPointerException`

```java
@Autowired(required=false)
private Demo demo;
```


>@Inject
*javax.inject
*JSR330(Dependency Injection for Java)
这是JSR330中的规范，通过`AutowiredAnnotationBeanPostProcessor`类实现的依赖注入

```java
@Inject
private Demo demo;
```

>@Resource
*javax.annotation
*JSR250(Common Annotations for Java)
这是JSR250中的规范，`@Resource`通过`CommonAnnotationBeanPostProcessor`类实现的依赖注入

`@Resource`一般会指定一个`name`属性
```java
@Resource(name = "demo")
private Demo demo;
```

#这些注解之间的不同之处

>`@Inject`和`@Autowired`基本上是一样的，因为两者都是通过`AutowiredAnnotationBeanPostProcessor`实现依赖注入，但是@Resource不同，
它通过`CommonAnnotationBeanPostProcessor`来处理依赖注入。当然，都是`BeanPostProcessor`

------

>*`@Autowired`和`@Inject`
*默认 `autowired by type` 通过类型进行装配
*可以通过`@Qualifier`显示指定 `autowired by qualifier name`

------

>*@Resource
*默认 `autowired by field name` 通过域名进行装配
*如果 `autowired by field name` 注入失败，则会退化为 `autowired by type`进行注入
*可以通过@Qualifier显示指定 `autowired by qualifier name`
*如果`autowired by qualifier name` 失败，会退化为`autowired by field name`。但是不会继续退化为`autowired by type`


#总结
在使用上可以用@Inject,这是JSR330规范的实现，而@Autowired是spring的实现，如果不用spring，则用不到这个注解。
而@Resource则是JSR250规范的实现，这是多年以前的规范。










