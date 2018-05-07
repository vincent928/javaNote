# cookie 和 session
---

## 1. Cookie
> 当我们第一次访问某一个网站的时候，我们本地是不会有这个网站的cookie的，那cookie是如何产生的呢。当我们第一次访问的时候，服务器会生成一个`cookie`对象，这个cookie可能会存储一些对需求有用的信息，服务器还会在响应头里加上这样的一句话：`set-Cookie`,并将cookie信息存储在`响应头`里。客户端在接受到服务器的请求后，会将响应头里cookie存储起来。当客户端再次向服务器发送请求的时候，客户端会在`请求头`里面将上cookie:这样的一句话,把cookie放入请求头中。这样服务器就可以在请求头里面拿到cookie了。下图就是cookie传递的几个过程。
  [1]: https://img-blog.csdn.net/20160305151848535?watermark/2/text/aHR0cDovL2Jsb2cuY3Nkbi5uZXQv/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70/gravity/Center
> *Cookie的属性*
A.cookie的`name`属性,getName()和setName()两个方法，name属性用来确定cookie的唯一性。
B.cookie的`value`属性,getValue()setValue()两个方法，value只能存储英文，数字以及其他符号，如果要存储中文信息的话首先得将中文编码。一个cookie最多可以存储4K的内容,不同的浏览器存储cookie的个数不一样，一般的浏览器达到了能存储50个cookie；
C.cookie的`path`属性，getPath()和setPath()两个方法，path属性是用来确定那个路径下的页面才能取得cookie，例如cookie.setPath("/test/abc/");那就说明只有是在"/test/abc/"路径下的页面才能获得该cookie；
D.cookie的`domian`属性，getDomain()和setDomain()两个方法，domain属性是用来确定在什么域名下才能取得该cookie的，例如cookie.setDomain("www.baidu.com")，那么在"m.baidu.com"域名下面就不能取得该cookie。如果设成cookie.setDomain(".baidu.com");那么在"www.baidu.com"和“m.baidu.com”下面都能取得该cookie；
E.cookie的`maxAge`属性，getMaxAge()和setMaxAge()两个方法，maxAge属性是用来确定cookie的有效时间的，单位为秒。默认的maxAge为-1，当maxAge为负数的时候，表面客户端不会将该cookie存储在本地硬盘中，而是存储在内存中，当关闭浏览器的时候，该cookie就会失效。如果maxAge为正数，那么浏览器会对cookie做持久化存储在本地，即使关掉浏览器或电脑，cookie在maxAge的时间内还是有效的，如果想删除cookie，就cookie.setMaxAge(0);将maxAge设置为0就表示删除该cookie。如果要想让cookie永久有效就cookie.setMaxAge(Integer.MAX_VALUE)；
F.cookie的`secure`属性，getSecure()和setSecure()方法，secure属性是用来指定cookie只能在安全协议下传输的，例如在HTTPS，SSL协议下传输，但设置了secure为true后也不会自动对cookie进行加密处理，加密的自己手动处理。

---

## 2. Session 
> `session`是将用户信息保存在服务器端来确定用户身份的一种机制，但是`session`还是会依赖`cookie`，因为当请求来到服务器的时候，服务器虽然已经创建了`session`，但是还是不知道这个请求对应的是哪一个`session`，所以在这里还需要创建一个`JSSESSIONID`的`cookie`用来记录`session`的id，如果在浏览器禁掉`cookie`后，服务器可以对url重写，运用`response.encodeURL(url);`改方法会自动在url后面带上`sessionId`的参数，这样每次用户请求的时候都会带上`sessionId`这个参数。记住一点就可以了，在服务器生成`session`的时候，这个`session`会有一个id与之对应，服务器会将这个id存储在一个cookie中传给客户端，`JSSESSIONID`
`cookie`的`maxAge`是负数，`cookie`没有在本地被持久化，所以当我们关掉浏览器的时候，这个`cookie`也会被清除，我们就找不到这个`cookie`与之对应的`session`了，所以我们经常听说关闭浏览器后，这个`session`的生命周期就结束了。我觉得`session`如果还在存活期内，它还是存在的，只是我们再也找不到它了。
> *Session属性*
attribute属性，void
`setAttribute(String,String)`；`String getAttribute(String)`；
`void removeAttribute(String)`设置和获取session里面的属性。
id属性，String
`getId()`;`session`的`id`是自动生成的
createTime,long
`getCreateTime()`;返回`session`被创建的时间。可以将long型转换为Date型的。
maxInactiveInterval属性,也就是session的有效时间，单位为秒，int
`getMaxInactiveInterval()`和`void setMaxInactiveInterval()`;

---

## 3.Cookie和Session的区别
> 1.`cookie`中内容是存储在`客户端`的，`session`的内容是存储在`服务器`上的。
2.由于cookie是存储在本地的，所以比较容易拿到cookie并对cookie进行解析和cookie欺骗。session相较于cookie要`安全`一些。
3.session是放在服务器上的，当访问增多的时候，容易给服务器增加`压力`，这时可以考虑将session存储的内容放在cookie中。
最后总结一下，cookie和session都是一种跟踪会话，帮助服务器来确定客户端用户身份的一种机制。cookie只是将信息存储在了客户端，session是将信息存储在服务器，session是对cookie有`依赖`关系的。