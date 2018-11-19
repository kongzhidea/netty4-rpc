#!/bin/bash
pfile=server.pid

if [ ! -f $pfile ]
then
    echo "$pfile not exists!"
    exit 1
fi

pid=`cat $pfile`

kill  $pid

rm -f $pfile

# 再次检查进程是否存在，若存在则强制kill
ret=`ps -ef |grep " $pid " |grep -v "grep" |awk '{print $2}'`

if [ "$ret" = "" ]
then
    echo "focus stop server "
    kill -9 $pid
else
    echo "stop server!"
fi

