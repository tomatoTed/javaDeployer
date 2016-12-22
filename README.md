# 1. 关于javaDeployer

* javaDeployer是一个用java编写的持续集成工具,可以将您的webapp应用一键打包到远程环境并自动部署.它智能的支持整包部署和增量部署,并自动选择是否起停服务器.
* 它可以帮您省却数分钟的编译,打包,传输,备份,服务器起停时间.停止这些重复的劳动吧,用来看看新闻刷刷知乎也是不错呢.
* 目前javaDeployer已经应用于我司的测试环境.

# 2. javaDeployer的几个版本
* GUI-swing版
* GUI-javaFX版 (**开发中**)
* webapp版 (**我也很期待呢o(∩_∩)o**)

# 3. 环境介绍
| 环境\版本 | GUI-swing | GUI-javaFX | webapp |
| :----- | :---- |:---- | :---- |
| jdk1.7+| yes | yes | yes |
| svn   | yes | yes | yes|
| maven | yes | yes | yes |
| javaFX 2.0+ | no | yes | no|

#  4. quick start(GUI-swing版)
1. 下载这个软件,并且在`config.properties`中配置您服务器相关信息
1. 编写start.cmd或者直接在命令行(快捷键`win+R`)中输入`java -jar deployer.jar config.properties`运行程序
1. 选择`整包部署`或者`增量部署`
1. 如果是`增量部署`,选择要部署的`diff`文件(这些文件根据`svn diff`命令获取),然后点击`开始`.
值得一说的是javaDeployer会自动记忆你本次忽略的文件,下次将不会自动勾选这些文件

# 5. 环境安装
+ [jdk1.7+](http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html)
您需要安装jdk来运行此程序,您可以在命令行(快捷键`win+R`,下同)中输入`java -version`来查看您的java版本号
+ [svn](https://tortoisesvn.net/)
您需要安装subversion来运行此程序,您可以输入`svb help`来查看您的java版本号
+ [maven](http://maven.apache.org/)
您需要安装maven并且配置环境变量,您可以输入`mvn -v`来查看您的maven版本号
+ [javaFX 2.0+](http://www.javafxchina.net/blog/)(GUI-javaFX版本需要)
在oracle jdk `Java SE 7 Update 6 `以后,会包含java FX,查看jdk中是否有`jfxrt.jar`即可

# 6. 截图

* 本地编译打包阶段

![](http://i1.piimg.com/567571/6958989758e9be0a.jpg)

* 上传阶段

![](http://i1.piimg.com/567571/33e2efa0cdcbd431.jpg)

* 备份,删除,拷贝,解压,服务器起停阶段

![](http://i1.piimg.com/567571/a57052123acf2f8e.jpg)

* 增量部署选择diff文件界面

![](http://i1.piimg.com/567571/eb20fa97496de05b.jpg)

# 7. License
MIT