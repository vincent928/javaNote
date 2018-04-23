##spring核心原理
标签：spring IOC控制反转 DI依赖注入

>说起`spring`的核心是什么，基本上都知道是`IOC`、`AOP`，但是究竟什么是`IOC`、`AOP`呢？
程序员将对象的生命周期都交给`spring`管理，需要的时候，`spring`会创建并注入。那么`spring`究竟是怎么实现的呢？
当程序员没有`spring`框架之前又是怎么写代码的呢？

>`IOC`：控制反转，`DI`：依赖注入。
`DI`是对`IOC`的另一种说法，由`Martin Fowler` 在2004年初的一篇论文中首次提出的。他总结：控制的什么被`反转`了？就是：获得`依赖对象的方式`反转了。

>IOC（Inversion of Control）控制反转，这是spring的核心，它贯穿了spring的始终。那么究竟什么是IOC呢？其实对于spring框架来说，就是由spring容器来控制管理对象的生命周期和对象间的关系。

>###举个例子：	
我们是如何找女朋友的？常见的情况是，我们到处去看哪里有长得漂亮身材又好的mm，然后打听她们的兴趣爱好、qq号、名字、微信号、手机号………，想办法认识她们，投其所好送其所要，这个过程是复杂深奥的，我们必须自己设计和面对每个环节。传统的程序开发也是如此，在一个对象中，如果要使用另外的对象，就必须得到它（自己new一个，或者从JNDI中查询一个），使用完之后还要将对象销毁（比如Connection等），对象始终会和其他的接口或类藕合起来。

>###而IOC是如何完成这些的呢？
它类似于婚介中心，在我们和女朋友之间引入一个第三者：婚姻介绍所。我可以向婚介提出一个列表，告诉它我想找个什么样的女朋友，然后婚介就会按照我们的要求，提供一个女生，我们只需要去和她谈恋爱、结婚就行了。简单明了，如果婚介给我们的人选不符合要求，我们就会抛出异常。整个过程不再由我自己控制，而是有婚介这样一个类似容器的机构来控制。Spring所倡导的开发方式就是如此，所有的类都会在spring容器中登记，告诉spring你是个什么东西，你需要什么东西，然后spring会在系统运行到适当的时候，把你要的东西主动给你，同时也把你交给其他需要你的东西。所有的类的创建、销毁都由spring来控制，也就是说控制对象生存周期的不再是引用它的对象，而是spring。对于某个具体的对象而言，以前是它控制其他对象，现在是所有对象都被spring控制，所以这叫控制反转。

>###IoC的一个重点是在系统运行中，动态的向某个对象提供它所需要的其他对象。
这一点是通过DI（Dependency Injection，依赖注入）来实现的。比如对象A需要操作数据库，以前我们总是要在A中自己编写代码来获得一个`Connection`对象，有了spring我们就只需要告诉spring，A中需要一个`Connection`，至于这个`Connection`怎么构造，何时构造，A不需要知道。在系统运行时，spring会在适当的时候制造一个`Connection`，然后像打针一样，注射到A当中，这样就完成了对各个对象之间关系的控制。A需要依赖`Connection`才能正常运行，而这个`Connection`是由spring注入到A中的，`依赖注入`的名字就这么来的。那么DI是如何实现的呢？Java1.3之后一个重要特征是`反射（reflection）`，它允许程序在运行的时候动态的生成对象、执行对象的方法、改变对象的属性，spring就是通过反射来实现注入的。关于反射的相关资料请查阅java doc。
```java
public static void main(String[] args){
	ApplicationContext context = new FileSystemXmlApplicationContext(
									"applicationContext.xml");
	Animal animal = (Animal)context.getBean("animal");
	animal.say();
}
```
###分析一下以上的代码。首先是applicationContext.xml
<bean id="animal" class="test.Cat">
		<property name="name" value="kitty">
</bean>
------
###test.Cat类
```java
public class Cat implements Animal {
	private String name;
	public void say(){
		System.out.println("I am " + name + "!");
	}
	public void setName(String name){
		this.name=name;
	}
}
```
###Animal接口
```
public interface Animal{
	public void say();
}
```
>由此可知animal.say()输出的是 I am kitty!
那么，spring是如何实现的呢？

###首先定义一个Bean类。用于存放该Bean类属性方法
```java
private String id;
private String type;
private Map<String,Object> properties = new HashMap<String,Object>();
```
>一个Bean包含id,type,和properties
接下来spring就开始加载我们的配置文件，将我们的配置信息保存在一个 HashMap中，map中的key就是Bean的id，value就是这个Bean。
只有这样我们才能通过context.getBean("animal")这个方法获得Animal这个对象。String可以注入基本类型，而且可以注入像List，Map这样的类型。
以Map为例，看看spring是如何保存的。
```xml
<bean id="test" class="Test">
	<property name="testMap">
		<map>
			<entry key="a">
				<value>1</value>
			</entry>
			<entry key="b">
				<value>2</value>
			</entry>
		</map>
	</property>
</bean>
```

###而spring是如何保存上面的配置呢？
```java
if (beanProperty.element("map") != null){
	Map<String,Object> propertiesMap = new HashMap<String,Object>();
	Element propertiesListMap = (Element)beanProperty.elements().get(0);
	Iterator<?> propertiesIterator = propertiesListMap.elements.iterator();
	while(propertiesIterator.hasNext()){
		Element vet = (Element)propertiesIterator.next();
		if(vet.getName().equals("entry")){
			String key = vet.attributeValue("key");
			Iterator<?> valuesIterator = vet.elements().iterator();
			while(valuesIterator.hasNext()){
				Element value = (Element)valuesIterator.next();
				if(value.getName().equals("value")){
					propertiesMap.put(key,value.getText());
				}
				if(value.getName.equals("ref")){
					propertiesMap.put(key,new String[] {
						value.attributeValue("bean")
					});
				}
			}
		}	
	}
	bean.getProperties().put(name,propertiesMap);
}
```

###那么spring是如何依赖注入的呢？
>其实依赖注入的思想也很简单，它是通过反射机制实现的，在实例化一个类的时候，它通过反射调用类中set方法将事先保存在HashMap中的类
属性注入到类中，首先实例化一个类：
```java
public static Object newInstance(String className){
		Class<?> cls = null;
		Object obj = null;
		try{
			cls = Class.forName(className);
			obj = cls.newInstance();			
		} catch (ClassNotFoundException e){
			throw new RuntimeException(e);
		} catch (InstantiationException e){
			throw new RuntimeException(e);
		} catch (IllegalAccessException e){
			throw new RuntimeException(e);
		}
		return obj;
}
```
>接着把类的依赖注入进去：
```java
public static void setProperty(Object obj,String name,String value){
	Class<? extends Object> clazz = obj.getClass();
	try{
		String methodName = returnSetMthodName(name);
		Method[] ms = clazz.getMethods();
		for (Method m : ms ){
			if(m.getName().equals(methodName)){
				if(m.getParameterTypes().length == 1){
					Class<?> clazzParameterType = m.getParameterTypes()[0];
					setFieldValue(clazzParameterType.getName(),value,m,obj);
					break;
				}
			}
		}
	}catch (SecurityException e){
		throw new RuntimeException(e);
	}catch (IllegalAccessException e){
		throw new RuntimeException(e);
	}catch (IllegalArgumentException e ){
		throw new RuntimeException(e);
	}catch (InvocationTargetException e){
		throw new RuntimeException(e);
	}
}
```
>最后它将这个类的实例返回给我们，我们就可以用了。以Map为例，创建一个HashMap,并将该HashMap注入到需要注入的类中：
```java
if(value instanceof Map){
		Iterator<? extends Map.Entry<?, ?>> iterator = ((Map<?, ?>) value).entrySet().iterator();
		Map<String,Object> map = new HashMap<String,Object>();
		while (iterator.hasNext()){
			Map.Entry<?, ?> entryMap = (Map.Entry<?, ?>) iterator.next();
			if (entryMap.getValue() instanceof String[]){
				map.put((String) entryMap.getKey(),
						getBean(((String[]) entryMap.getValue())[0]));
			}
		}
		BeanProcesser.setProerty(obj,property,map);
	}
```
>这只是spring中最核心的依赖注入的部分，spring能做到的远不止这些。学习永无止境












