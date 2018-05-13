#!/bin/bash

# getUserLog : to launch zanity GUI and store data
getUserLog(){
	mUserFormData=""
	mUserFormData=$(zenity --forms --title="My Day" --text="Every day is a learning" \
	   --add-entry="Day In a word" \
	   --add-entry="Beauty I saw" \
	   --add-entry="I Learned" \
	   --add-entry="I wish" \
	   --add-entry="Remind me to" \
	   --add-entry="Others")
	echo "$mUserFormData"
}

# initApplication : to initialise the application
initApplication(){
	STRING="Hi Suhaas, launching GitAutomator..."
	echo $STRING
}

#directoryManager : to create a dir /2018/May/file_1
directoryManager(){
echo "Initialising directoryManager()"
cd /home/suhaas/Desktop/MyPrepLogger/diary
CURRENT_MONTH="`date "+%b"`"
echo $CURRENT_MONTH
CURRENT_YEAR="`date "+%Y"`"
echo $CURRENT_YEAR
mkdir -p $CURRENT_YEAR/$CURRENT_MONTH
cd $CURRENT_YEAR/$CURRENT_MONTH
}

#gitManager : to commit file
gitManager(){
echo "Initialising gitManager()"
echo "WKDIR in path : "
pwd
git status
git add --all
git commit -m "[My Diary][$1]"
git push
}

# createFile : to create the file with the date
createFile(){
#PWD : /home/suhaas/Desktop/MyPrepLogger/diary
CURRENT_DATE="`date "+%d_%b_%Y_%H:%M:%S"`"
echo $CURRENT_DATE
echo "Param #1 is $1"
mRawFormData=$1
IFS='|'        						# | is set as delimiter
read -ra ADDR <<< "$mRawFormData"	# str is read into an array as tokens separated by IFS
arraylength=${#ADDR[@]} 			# get length of an array
mFileWrite="#Script Writing Data to file\n"
mFileName="$CURRENT_DATE.txt"
	for (( i=1; i<${arraylength}+1; i++ )); # access each element of array
		do
	    echo $i " / " ${arraylength} " : " ${ADDR[$i-1]}
		case "$i" in
		"1")
		mFileWrite="$mFileWrite Day In a word       : ${ADDR[$i-1]}\n"
		;;
		"2")
		mFileWrite="$mFileWrite Beauty I saw        : ${ADDR[$i-1]}\n"
		;;
		"3")
		mFileWrite="$mFileWrite I Learned           : ${ADDR[$i-1]}\n"
		;;
		"4")
		mFileWrite="$mFileWrite I Wish              : ${ADDR[$i-1]}\n"
		;;
		"5")
		mFileWrite="$mFileWrite Remind me to        : ${ADDR[$i-1]}\n"
		;;
		"6")
		mFileWrite="$mFileWrite Others              : ${ADDR[$i-1]}\n"
		;;
		*)
		    mFileWrite="$mFileWrite Invalid Data in Switch Case\n"
		    ;;
		esac
	done
mFileWrite="$mFileWrite\n Dear Diary,\n Best,\n Suhaas"

echo "Mounting to path"
directoryManager

echo "--Writing Data to file--"
echo -e $mFileWrite
echo -e $mFileWrite >> $mFileName
echo "--Closing--"

#COMMIT_MSG : Saved with the daily word
COMMIT_MSG="${ADDR[0]}"
gitManager "$COMMIT_MSG"

echo "--Script End--"

}

###
# Main body of script starts here
###
initApplication
mUserFormData=$( getUserLog )	
createFile "$mUserFormData"