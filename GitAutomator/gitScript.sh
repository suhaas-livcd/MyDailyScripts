#!/bin/bash

# getUserLog : to launch zanity GUI and store data
getUserLog(){
	zenity --forms --title="My Day" --text="Every day is a learning" \
	   --add-entry="Day In a word" \
	   --add-entry="Beauty I saw" \
	   --add-entry="I Learned" \
	   --add-entry="You wish if any" \
	   --add-entry="Remind me to" \
	   --add-entry="Others"
}

# initApplication : to initialise the application
initApplication(){
	STRING="Hi Suhaas, launching GitAutomator..."
	echo $STRING
}

# createFile : to create the file with the date
createFile(){
#PWD : /home/suhaas/Desktop/MyPrepLogger/diary
CURRENT_DATE="`date "+%d_%m_%Y_%H:%M:%S"`"
echo $CURRENT_DATE
}

###
# Main body of script starts here
###
initApplication
getUserLog
createFile