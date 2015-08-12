#!/bin/sh
#cd /opt/process/LiveAlertApplications/CommentsMailer

JAVA_HOME=/usr/lib/jvm/java-6-oracle
PATH=/usr/bin/:$JAVA_HOME/bin:/opt/ant/bin:$PATH
echo "PATH:$PATH" >> logs/GOHFMailerSMS_Contact_Process_`date +%m%d%y`.txt
export PATH JAVA_HOME

CLASSPATH=.:`echo *.jar|tr ' ' ':'`:`echo lib/*.jar|tr ' ' ':'`:`echo bin|tr ' ' ':'`:`echo config|tr ' ' ':'`:`echo src|tr ' ' ':'`:$CLASSPATH
export CLASSPATH
echo "  CLASSPATH: $CLASSPATH" >> logs/GOHFMailerSMS_Contact_Process_`date +%m%d%y`.txt
#echo "  Compiling Plz. wait.. " >> logs/GOHFMailerSMS_Contact_Process_`date +%m%d%y`.txt
#ant >> logs/GOHFMailerSMS_Contact_Process_`date +%m%d%y`.txt
#echo "	Compilation Process Completed . Now Sending Communication" >> logs/GOHFMailerSMS_Contact_Process_`date +%m%d%y`.txt
date >> logs/GOHFMailerSMS_Contact_Process_`date +%m%d%y`.txt
$JAVA_HOME/bin/java -server -d64 -Xms2048m -Xmx2048m -XX:ParallelGCThreads=4 org.springframework.batch.core.launch.support.CommandLineJobRunner GOHFMailerSMSProcess.xml gohfMailerSmsProcessJob executionType=CO >> logs/GOHFMailerSMS_Contact_Process_`date +%m%d%y`.txt
date >> logs/GOHFMailerSMS_Contact_Process_`date +%m%d%y`.txt
echo "	Process Executed........." >> logs/GOHFMailerSMS_Contact_Process_`date +%m%d%y`.txt
