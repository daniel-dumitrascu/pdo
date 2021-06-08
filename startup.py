#!/usr/bin/env python3
import os
import shutil
import time
import subprocess

APP_NAME = "centralpoint"
TOMCAT_HOME = "C:\\dev\\tomcat"
CODE_HOME = "C:\\Home\\central-point2"
RESOURCE_ROOT = "C:\\Home\\dataset_main"
FRONTEND_ROOT = "C:\\Home\\central-point2\\src\\main\\frontend\\angular7-httpclient"

def tomcat_cleanup():
	print("Cleaning tomcat...")
	if os.path.exists(TOMCAT_HOME + "/webapps/" + APP_NAME + ".war"):
		os.remove(TOMCAT_HOME + "/webapps/" + APP_NAME + ".war")
	
	for i in range(10):
		if os.path.isdir(TOMCAT_HOME + "/work/Catalina/localhost/" + APP_NAME):
			try:
				if os.path.isdir(TOMCAT_HOME + "/work/Catalina/localhost/" + APP_NAME):
					os.remove(TOMCAT_HOME + "/work/Catalina/localhost/" + APP_NAME)
			except:
				print("Could not clean the 'work/Catalina/localhost' directory, we'll wait 3 seconds for tomcat to clean it.")
				time.sleep(3)
		else:
			break;

		
	while os.path.isdir(TOMCAT_HOME + "/webapps/centralpoint"):
		try:
			if os.path.isdir(TOMCAT_HOME + "/webapps/centralpoint"):
				os.remove(TOMCAT_HOME + "/webapps/centralpoint")
		except:
			print("Could not clean the 'webapps' directory, we'll wait 3 seconds for tomcat to clean it.")
			time.sleep(3)
			

def war_moving():
	print("Moving the war file in tomcat...")
	shutil.move(CODE_HOME + "/target/" + APP_NAME + ".war", TOMCAT_HOME + "/webapps/" + APP_NAME + ".war")
	
	# we wait for tomcat to take the war file and create the folder for it
	while os.path.isdir(TOMCAT_HOME + "/webapps/centralpoint") == False:
		print("Folder is not yet created in 'tomcat/webapps' so, we'll wait 2 seconds for tomcat to create it.")
		time.sleep(2) # wait 2 seconds for the tomcat to create the folder


def start_tomcat_debug():
	print("Starting tomcat...")
	subprocess.call([TOMCAT_HOME + '\\bin\\catalina.bat', "jpda", "start"])
	print("done")
	
	
def start_http_resource_server():
	print("Starting the http resource server...")
	command = "http-server " + RESOURCE_ROOT
	os.system("start cmd.exe /k " + command)

def start_frontend_server():
	print("Starting the frontend server...")
	serverStartCommand = "ng serve --host 0.0.0.0 --port 3100"
	fullCommand = "cd " + FRONTEND_ROOT + " & " + serverStartCommand	
	subprocess.Popen("cmd /k " + fullCommand, creationflags=subprocess.CREATE_NEW_CONSOLE)


	
start_tomcat_debug()
compilationResult = os.system('mvn clean install')
if compilationResult == 0:
	tomcat_cleanup()
	war_moving()
	start_http_resource_server()
	start_frontend_server()
else: 
	print("The code compilation failed.")