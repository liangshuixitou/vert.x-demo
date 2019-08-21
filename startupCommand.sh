#!/bin/bash
if [ -z $YZ_HOME ]; then
  echo "$YZ_HOME not set"
  YZ_HOME=.
fi

JAVA_HOME=$YZ_HOME/jdk8
JAVA_OPTS="-Duser.timezone=GMT+8  -Dfile.encoding=UTF-8 -server -Xms124m -Xmx1024m  -XX:+PrintGCTimeStamps -XX:+PrintGCDetails -Xloggc:./gc.log -XX:+HeapDumpOnOutOfMemoryError"
#DEBUG="-agentlib:jdwp=transport=dt_socket,address=8888,server=y,suspend=y"
#DEBUG=""
APP_LOG=$YZ_HOME/var/logs
APP_HOME=$YZ_HOME/
APP_MAIN=org.nit.dqmserver.Startup

for i in $YZ_HOME/lib/*.*; do
CLASSPATH="$CLASSPATH":"$i"
done
PD=0

echo "$JAVA_HOME/bin/java $JAVA_OPTS -classpath $CLASSPATH $APP_MAIN"