#!/bin/sh
echo -------------------------------------------
echo start server
echo -------------------------------------------
# ������Ŀ����·��
export CODE_HOME="/app/deploy/Spring_RocketMQ-package"
#��־·��
export LOG_PATH="/app/logs/project/spring_rocketmq"
mkdir -p $LOG_PATH
# ��������·��
export CLASSPATH="$CODE_HOME/classes:$CODE_HOME/lib/*"
# java��ִ���ļ�λ��
export _EXECJAVA="$JAVA_HOME/bin/java"
# JVM��������
export JAVA_OPTS="-server -Xms128m -Xmx256m -Xss256k -XX:MaxDirectMemorySize=128m"
# ������
export MAIN_CLASS=com.vstaryw.mq.Application
$_EXECJAVA $JAVA_OPTS -classpath $CLASSPATH $MAIN_CLASS &
tail -f $LOG_PATH/stdout.log