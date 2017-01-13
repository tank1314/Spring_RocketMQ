#!/bin/sh
export LOG_PATH="/app/logs/project/spring_rocketmq"
mkdir -p $LOG_PATH
# ������
export MAIN_CLASS=com.vstaryw.mq.Application
echo -------------------------------------------
echo stop server
#������ؽ���
PIDs=`jps -l | grep $MAIN_CLASS | awk '{print $1}'`
#ֹͣ����
if [ -n "$PIDs" ]; then
  for PID in $PIDs; do
      kill $PID
      echo "kill $PID"
  done
fi
#�ȴ�50��
for i in 1 10; do
  PIDs=`jps -l | grep $MAIN_CLASS | awk '{print $1}'`
  if [ ! -n "$PIDs" ]; then
    echo "stop server success"
    echo -------------------------------------------
    break
  fi
  echo "sleep 5s"
  sleep 5
done
#����ȴ�50�뻹û��ֹͣ�ֱ꣬��ɱ��
PIDs=`jps -l | grep $MAIN_CLASS | awk '{print $1}'`
if [ -n "$PIDs" ]; then
  for PID in $PIDs; do
      kill -9 $PID
      echo "kill -9 $PID"
  done
fi
tail -fn200 $LOG_PATH/stdout.log