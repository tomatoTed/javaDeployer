#!/bin/bash

tomcatDir=$1 #tomcat主目录

cd $tomcatDir
echo "shutdown server"
sh bin/shutdown.sh
echo 'finished shutdown server'
echo 'exit_qwertyuiop'
