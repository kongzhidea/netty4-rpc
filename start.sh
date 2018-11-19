#!/bin/bash
if [ $# -gt 0 ]
then
	mvn clean install
    echo "编译 server"
fi

# demo
#java -cp .:target/lib/*:target/netty4-rpc.jar:target/test-classes/  -Dio.netty.leakDetectionLevel=advanced -Dio.netty.leakDetection.maxRecords=32 -Xdebug -Xnoagent -Djava.compiler=NONE -Xrunjdwp:transport=dt_socket,server=y,suspend=n,address=51639 com.netty4.rpc.demo.server.bootstrap.DeamonRunner


pfile=server.pid
log=logs/stdout

mainClass="com.netty4.rpc.demo.server.bootstrap.DeamonRunner"
libs=".:target/lib/*:target/netty4-rpc.jar:target/test-classes/"


# jdk1.8 需要换成元空间
#JAVA_OPTS="-Xms400m -Xmx400m -Xss256k -XX:PermSize=112m -XX:MaxPermSize=112m";

JAVA_OPTS="$JAVA_OPTS -verbose:gc"
JAVA_OPTS="$JAVA_OPTS -XX:+PrintGCTimeStamps"
JAVA_OPTS="$JAVA_OPTS -XX:+PrintGCDetails"
JAVA_OPTS="$JAVA_OPTS -Xloggc:/data/log/gclogs/gc.log"


vm_args="-server -verbose:gc -XX:+UseConcMarkSweepGC -XX:+UseParNewGC -XX:-CMSParallelRemarkEnabled -XX:+UseConcMarkSweepGC -XX:+UseCMSCompactAtFullCollection -XX:SurvivorRatio=8 -XX:MaxTenuringThreshold=7 -XX:GCTimeRatio=19 -XX:CMSInitiatingOccupancyFraction=70 -XX:CMSFullGCsBeforeCompaction=0 -XX:+CMSClassUnloadingEnabled -Xdebug -Xnoagent -Djava.compiler=NONE -Xrunjdwp:transport=dt_socket,server=y,suspend=n,address=51639  $JAVA_OPTS ";


nohup java $vm_args -cp $libs $mainClass >>$log 2>&1 &

pid=$!

echo $pid >$pfile

echo "start server!"
