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

getPID() {
    javaps=`$JAVA_HOME/bin/jps -l | grep $APP_MAIN`
    if [ -n "$javaps" ]; then
        PID=`echo $javaps | awk '{print $1}'`
    else
        PID=0
    fi
}

startup() {
    getPID
    echo "================================================================================================================"
    if [ $PID -ne 0 ]; then
        echo "yzCloud already started(PID=$PID)"
        echo "================================================================================================================"
    else
        echo -n "Starting yzCloud"
        nohup $JAVA_HOME/bin/java $JAVA_OPTS -classpath $CLASSPATH $APP_MAIN >/dev/null 2>&1 &
#        nohup $JAVA_HOME/bin/java $JAVA_OPTS $DEBUG -classpath $CLASSPATH $APP_MAIN  >> $APP_LOG/nohup.log 2>>$APP_LOG/e.log &
        getPID
        if [ $PID -ne 0 ]; then
            echo "(PID=$PID)...[Success]"
            echo "================================================================================================================"
        else
            echo "[Failed]"
            echo "================================================================================================================"
        fi
    fi
}

shutdown(){
    getPID
    echo "================================================================================================================"
    if [ $PID -ne 0 ]; then
        echo -n "Stopping yzcloud(PID=$PID)..."
        kill -9 $PID
        if [ $? -eq 0 ]; then
            echo "[Success]"
            echo "================================================================================================================"
        else
            echo "[Failed]"
            echo "================================================================================================================"
        fi
        getPID
        if [ $PID -ne 0 ]; then
            shutdown
        fi
    else
        echo "yzcloud is not running"
        echo "================================================================================================================"
    fi
}

shutdown
startup
