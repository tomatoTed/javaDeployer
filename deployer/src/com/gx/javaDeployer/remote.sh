#!/bin/bash

tomcatDir=$1 #tomcat主目录
appName=$2
tempDir="/home/web_tomcat/remote"  #war包临时存放目录
dateStr=`date --date='0 days ago' "+%m%d%H%M%S"`

cd $tomcatDir
echo "shutdown server"
sh bin/shutdown.sh
pid=$(ps -ef|grep tomcat-juli.jar|grep -v 'grep'|awk '{print $2}');
if [[ $pid =~ ^[0-9]+$ ]]; then
	echo 'is number'
	echo 'pid='${pid}
	$(kill -9 $pid) 
	echo "kill server"
else 
	echo 'pid='${pid}
	echo 'not number'
fi;
echo "finished shutdown server"
echo "backup war file"
if [ -d 'webapps/${appName}' ]; then 
	tar -zcvf webapps/${appName}${dateStr}.tar.gz webapps/${appName};
	rm -rf webapps/${appName}/*
	echo 'finished remove war'
else 
	mkdir webapps/${appName}
fi;
echo "finished backup war file"
echo "exract war file"
cp -v ${tempDir}/${appName}.war webapps/${appName}
cd webapps/${appName}
jar -xvf ${appName}.war
cd ../..
echo "finished exract war file"
echo "copy jar file"
if [ -d 'webapps/${appName}/WEB-INF/lib' ]; then 
	rm -rf webapps/${appName}/WEB-INF/lib/*
else  
	mkdir webapps/${appName}/WEB-INF/lib
fi;
echo "finished copy jar file"
cp -v mylib/* webapps/${appName}/WEB-INF/lib
echo "start server"
sh bin/startup.sh
echo "finished start server"
echo "clean log"
> logs/std.log
echo "show log"
tail -100f logs/std.log|sed '/configured successfully/Q'
echo "publish success"
