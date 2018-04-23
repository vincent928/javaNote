# java对象的创建

------
```java
//java代码
public class ConstructorDemo {  
    private int value;  

    public ConstructorDemo(int i, Object o) {  
      this.value = i;  
    }  

    public static void main(String[] args) {  
     ConstructorDemo demo = new ConstructorDemo(2, args);  
    }  
}  
```  
    
------

```class
//javap class代码
public ConstructorDemo(int, java.lang.Object);  
Code:  
Stack=2, Locals=3, Args_size=3  
0:   aload_0  
1:   invokespecial   #1; //Method java/lang/Object."<init>":()V  
4:   aload_0  
5:   iload_1  
6:   putfield        #2; //Field value:I  
9:   return  

public static void main(java.lang.String[]);  
Code:  
Stack=4, Locals=2, Args_size=1  
0:   new     #3; //class ConstructorDemo  
3:   dup  
4:   iconst_2  
5:   aload_0  
6:   invokespecial   #4; //Method "<init>":(ILjava/lang/Object;)V  
9:   astore_1  
10:  return  
```       
------

先从main()方法开始看。 
第一条指令是new，用于创建出ConstructorDemo类型的一个空对象，执行过后指向该对象的引用被压到操作数栈上。 
第二条指令是dup，将操作数栈顶的值复制一份压回到栈顶；其中dup出来的一份用于作为隐式参数传到实例构造器里去（对应后面的invokespecial），原本的一份用于保存到局部变量去（对应后面的astore_1）。 
第三条指令是iconst_2，将常量2压到操作数栈上，作为ConstructorDemo实例构造器的第一个显式参数。 
第四条指令是aload_0，将main()方法的参数args作为ConstructorDemo实例构造器的第二个显式参数。 
第五条指令是invokespecial，调用ConstructorDemo实例构造器。再次留意，前面已经传了三个参数，分别是new出来的实例的引用、常量2与main()的参数args。该指令执行过后，操作数栈顶就只剩下dup前通过new得到的引用。 
第六条指令是astore_1，将操作数栈顶的引用保存到局部变量 demo 中。执行过后操作数栈空了。 
最后一条指令是return，结束main()方法的执行并返回。 

然后从ConstructorDemo的实例构造器来看。 
第一条指令是aload_0，将第一个参数（不管是隐式还是显式参数）压到操作数栈上。从main()的调用序列可以看到第一个参数是刚new出来的对象实例的引用，对这个构造器来说也就是“this”。 
第二条指令是invokespecial，调用Object的实例构造器。前一条指令的“this”就是这个调用的参数。执行过后操作数栈就空了。 
第三条指令又是aload_0，再次将“this”压到操作数栈上。 
第四条指令是iload_1，将第二个参数压到操作数栈上，也就是i。 
第五条指令是putfield，将i赋值给this.value。执行过后操作数栈又空了。 
最后一条指令是return，结束该实例构造器的执行并返回。 

这个例子的注意点在于： 
1、Java的实例构造器只负责初始化，不负责创建对象；Java虚拟机的字节码指令的设计也反映了这一点，有一个new指令专门用于创建对象实例，而调用实例构造器则使用invokespecial指令。 
2、“this”是作为实例构造器的第一个实际参数传入的。 

笔记:
 ConstructorDemo demo = new ConstructorDemo(2, args); 
 执行此语句时，jvm先创建空的对象，并将对象引用this隐式传入构造器，当参数传递完毕后，调用构造器。完成对象的初始化后，将地址返回，赋给对象引用 demo。
 所以实例构造器是不创建对象，只负责初始化对象。真正创建对象实例的还是new关键字