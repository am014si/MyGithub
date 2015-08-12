BACK_PATH=/home/gohfuser/GOHFBatchJob/Scheduled_Batch_log


echo "check ====="$0
process=`ps ax | grep $0 | grep -v grep | wc -l`
echo $process
# . $HOME/.bash_profile
if [ $process -gt 2 ]
then
echo "$0 is already running"
exit 1
fi
while [ true ] 
do
        echo "start ...................."
        aStart=`date +%s`
        cd /home/gohfuser/GOHFBatchJob
#        . $HOME/.bash_profile
        ./GOHFMailerSMS_Booking_Process.sh
#       times=$( date +%H%M )
        aEnd=`date +%s`
        echo "`date` :Execution Time of this cycle is :  `expr $aEnd - $aStart`"   >> ${BACK_PATH}/GOHFBatchBookingMailerScheduler.log
        sleep 10
done
