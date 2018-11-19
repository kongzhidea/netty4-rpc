#!/bin/bash
if [ $# -gt 0 ]
then
	mvn clean install
    echo "编译 server"
fi


java -cp .:target/lib/*:target/netty4-rpc.jar:target/test-classes/  -Dio.netty.leakDetectionLevel=advanced -Dio.netty.leakDetection.maxRecords=32 -Xdebug -Xnoagent -Djava.compiler=NONE -Xrunjdwp:transport=dt_socket,server=y,suspend=n,address=51639 com.netty4.rpc.demo.server.bootstrap.DeamonRunner

