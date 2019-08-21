#!/bin/bash

http=`netstat -tln | grep -E 8081`
if [ -n "$http" ]
then
    echo "http"
else
	echo "-http"
fi
