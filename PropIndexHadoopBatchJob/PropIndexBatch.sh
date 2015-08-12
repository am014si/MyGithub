#!/bin/sh
#cd /opt/process/LiveAlertApplications/CommentsMailer

JAVA_HOME=/var/jdk1.6.0_25
PATH=/usr/bin/:$JAVA_HOME/bin:/opt/apache-ant-1.9.4/bin:$PATH
echo "PATH:$PATH" >> logs/PropIndexBatch_`date +%m%d%y`.txt
export PATH JAVA_HOME

CLASSPATH=.:`echo *.jar|tr ' ' ':'`:`echo lib/*.jar|tr ' ' ':'`:`echo bin|tr ' ' ':'`:`echo config|tr ' ' ':'`:`echo src|tr ' ' ':'`:$CLASSPATH
export CLASSPATH
echo "  CLASSPATH: $CLASSPATH" >> logs/PropIndexBatch_`date +%m%d%y`.txt
echo "  Compiling Plz. wait.. " >> logs/PropIndexBatch_`date +%m%d%y`.txt
ant >> logs/PropIndexBatch_`date +%m%d%y`.txt
echo "	Compilation Process Completed . Now generating month wise report data files" >> logs/PropIndexBatch_`date +%m%d%y`.txt
date >> logs/PropIndexBatch_`date +%m%d%y`.txt
$JAVA_HOME/bin/java -server -d64 -Xms2048m -Xmx3048m -XX:ParallelGCThreads=4 org.springframework.batch.core.launch.support.CommandLineJobRunner configure/EtlDBToHadoopProcess.xml etlDBToHadoopJob  >> logs/PropIndexBatch_`date +%m%d%y`.txt
date >> logs/PropIndexBatch_`date +%m%d%y`.txt
echo "	Process Executed........." >> logs/PropIndexBatch_`date +%m%d%y`.txt

#Hadoop Commands
#su hdusr >> logs/PropIndexBatch_`date +%m%d%y`.txt
hadoop dfs -rm -r /propIndexData/liveDB
hadoop dfs -mkdir /propIndexData/liveDB >> logs/PropIndexBatch_`date +%m%d%y`.txt
hadoop dfs -copyFromLocal /opt/propIndexData/liveDB/* /propIndexData/liveDB/ >> logs/PropIndexBatch_`date +%m%d%y`.txt
#exit

#hadoop jar /opt/PropIndexHadoop.jar com.mb.hadoop.data.generator.LocalityReportGenerator /propIndexData/LocalityWisePropertyListingReport/2014/input 2014 9