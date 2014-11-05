#!/bin/bash

base="/home/kongzhidea/workspace/public-remote-service"
pfile=$base/server.pid
log=$base/logs/stdout

workspace=$base/public-remote-server

mainClass="com.rr.publik.bootstrap.DeamonRunner"
libs="public-remote-server/target/lib/*:public-remote-server/target/public-remote-server.jar"
configFile="public-remote-server/target/classes/com/rr/publik/server/server.xml"
beanName="serverStart"

if [ ! -d $workspace ]
then
    echo "$workspace not exists!"
    exit 1
fi

if [ -f $pfile ]
then
    echo "`cat $pfile` is running!"
    exit 1
fi

#memory_options="-Xms6g -Xmx8g -XX:NewSize=1600m -XX:MaxNewSize=1600m";
#gc_options=" -XX:+PrintGCDetails -XX:+PrintGCTimeStamps -XX:+PrintGCApplicationConcurrentTime -XX:+UseParNewGC -Xloggc:logs/gc.log -XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=/data/logs/game/headdump/ -XX:ErrorFile=logs/javacore.log";
#jmx_options="-Dcom.sun.management.jmxremote.port=12306"
#jmx_options="$jmx_options -Dcom.sun.management.jmxremote.authenticate=false"
#jmx_options="$jmx_options -Dcom.sun.management.jmxremote.ssl=false"

memory_options="-Xms200m -Xmx200m -XX:NewSize=100m -XX:MaxNewSize=100m";

service_vm_args="$memory_options"

vm_args="-server -verbose:gc -XX:PermSize=64M -XX:MaxPermSize=64M -XX:+UseConcMarkSweepGC -XX:+UseParNewGC -XX:-CMSParallelRemarkEnabled -XX:+UseConcMarkSweepGC -XX:+UseCMSCompactAtFullCollection -XX:SurvivorRatio=8 -XX:MaxTenuringThreshold=7 -XX:GCTimeRatio=19 -XX:CMSInitiatingOccupancyFraction=70 -XX:CMSFullGCsBeforeCompaction=0 -XX:+CMSClassUnloadingEnabled -Xdebug -Xnoagent -Djava.compiler=NONE -Xrunjdwp:transport=dt_socket,server=y,suspend=n,address=51639  $service_vm_args ";

cd $base

#nohup mvn exec:java -Dexec.mainClass="$mainClass" -Dexec.args="$configFile $beanName" >>$log 2>&1 &
nohup java $vm_args -cp $libs $mainClass $configFile $beanName>>$log 2>&1 &

pid=$!

echo $pid >$pfile

echo "start server!"

