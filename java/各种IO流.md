# 各种I/O流

------
>Java中所有的流都是基于字节流，所以最基本的流是
```
//输入输出字节流
InputStream
OutputStream
```
```
//在字节流的基础上，封装了字符流
Reader
Writer
```
```
//进一步，又封装了缓存流
BufferedReader
PrintWriter
```
```
//以及数据流
DataInputStream
DataOutputStream
```
```
//对象流
ObjectInputStream
ObjectOutputStream
```
>以及一些其他的奇奇怪怪的流 ~~~