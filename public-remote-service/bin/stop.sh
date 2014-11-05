#!/bin/bash

base="/home/kongzhidea/workspace/public-remote-service"

pfile=$base/server.pid
log=$base/logs/stdout

if [ ! -f $pfile ]
then
    echo "$pfile not exists!"
    exit 1
fi

kill -9 `cat $pfile`
rm -f $pfile

echo "stop server!"
