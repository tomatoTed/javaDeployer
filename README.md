#关于javaDeployer
> javaDeployer是一个用java编写的持续集成工具,可以将您的webapp应用一键打包到远程环境并自动部署.它智能的支持整包部署和增量部署,并自动选择是否起停服务器.
它可以帮您省却数分钟的编译,打包,传输,起停时间.停止这些重复的劳动吧,用来看看新闻刷刷知乎也是不错呢.
目前javaDeployer已经应用于我司的测试环境.

#javaDeployer的几个版本
分别为GUI-swing版,GUI-javaFX版和webapp版,其中GUI-javaFX版和webapp版我在持续开发中.

#GUI-swing版
###依赖
+ jdk1.7+
您需要安装jdk来运行此程序,您可以在命令行(快捷键`win+R`)中输入`java -version`来查看您的java版本号
+ svn
您需要安装jdk来运行此程序,您可以在命令行(快捷键`win+R`)中输入`java -version`来查看您的java版本号
+ maven
您需要安装maven并且配置环境变量,您可以在命令行(快捷键`win+R`)中输入`mvn -v`来查看您的maven版本号

#quick start
1. 下载这个软件,并且在config.properties中配置您服务器相关信息
1. 编写start.cmd或者直接在命令行(快捷键`win+R`)中输入`java -jar deployer.jar config.properties`运行程序
1. 选择`整包部署`或者`增量部署`
1. 如果是`增量部署`,选择要部署的`diff文件`(这些文件根据`svn diff`命令获取),然后点击`开始`
