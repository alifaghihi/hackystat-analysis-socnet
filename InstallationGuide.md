# 1.0 Introduction #

The SocNet binary distribution contains almost everything you need to run the system. If you've got Java 1.6, you are all set!


# 2.0 Downloading the SocNet Sources #

## 2.1 Install Necessary Tools ##
For this, you will need:
  * Java 1.6

Install [Java](http://java.sun.com/) 1.6.

Now we're ready to get down to business.

## 2.1 Download the Distribution ##

Download the latest release of SocNet from the featured downloads on the Project Home Page. Today, that is [SocNet\_0.2a-bin.zip](http://hackystat-analysis-socnet.googlecode.com/files/Socnet_0.2a-bin.zip).

Unzip it wherever you would like to run it from.

# 3.0 Running SocNet #

## 3.1 Set Up Configuration Files ##

### 3.1.1 Socnet Configuration File ###
The socnet.properties file should be placed in the computer's home directory in the hackystat home folder. This file is not strictly necessary, but if you want to use it, it allows additional specification and configuration. A sample file can be viewed/downloaded [here](http://hackystat-analysis-socnet.googlecode.com/svn/trunk/config/socnet.properties.sample/).

### 3.1.2 Twitter Client Configuration File ###
The socnet.twitter.properties file should be placed in the same place as the socnet.properties file. Note that the socnet.twitter.properties files must contain a valid Twitter username and valid Twitter password. The default API call limit for a Twitter account is 150 calls per hour.  Raise this at your peril--Twitter will blacklist your account if you repeatedly go over your call limit. It is included in the configuration file so that after Twitter has raised the API call limit, it is easy to change. A sample file can be viewed/downloaded [here](http://hackystat-analysis-socnet.googlecode.com/svn/trunk/config/socnet.twitter.properties.sample/).

**All values in the socnet.twitter.properties configuration file are mandatory**

### 3.1.3 Hackystat Client Configuration File ###

Create a blank file named socnet.hackystatclient.properties in the hackystatclient folder within socnet folder, which is within the hackystat hidden folder in the user's home directory. In Linux, the abosolute path is

|$USER\_HOME/.hackystat/socnet/hackystatclient/socnet.hackystatclient.properties|
|:------------------------------------------------------------------------------|


Now it's time to run this bad boy

## 3.2 Running SocNet ##

### 3.2.1 Running the SocNet Server ###
First, we have to start up the server. To do this, execute

|java -jar socnetserver.jar|
|:-------------------------|

in the top level directory.

### 3.2.2 Running the Twitter Client ###

Execute

|java -jar socnet\_twitter.jar|
|:----------------------------|

to start up the Twitter Client.

### 3.3.3 Running the Hackystat Client ###

#### 3.3.3.1 Setup ####

Execute

|java -jar project\_selector.jar|
|:------------------------------|

in the top level directory to start up the project selection dialog. That should bring up a dialog box that looks something like this:

![http://hackystat-analysis-socnet.googlecode.com/files/ProjectSelectionDialog.png](http://hackystat-analysis-socnet.googlecode.com/files/ProjectSelectionDialog.png)

Click on a project in the list for which you want to send information to the SocNet server.

![http://hackystat-analysis-socnet.googlecode.com/files/SelectProject.png](http://hackystat-analysis-socnet.googlecode.com/files/SelectProject.png)

Set the start date and end date for which you want the SocNet server to be able to access data for this project.

![http://hackystat-analysis-socnet.googlecode.com/files/SelectStart.png](http://hackystat-analysis-socnet.googlecode.com/files/SelectStart.png)

Finally, check the checkbox indicating that you want to send data for this project to the SocNet server.

![http://hackystat-analysis-socnet.googlecode.com/files/SelectSendToSocNet.png](http://hackystat-analysis-socnet.googlecode.com/files/SelectSendToSocNet.png)

Repeat this process for each project for which you are interested in sending information to the SocNet server. When you have done this for as many projects as you are interested in sending information to the SocNet server for, click "Save and Close"

![http://hackystat-analysis-socnet.googlecode.com/files/SaveAndClose.png](http://hackystat-analysis-socnet.googlecode.com/files/SaveAndClose.png)

Information gleaned from the dialog will be saved in the socnet.hackystatclient.properties file.

#### 3.3.3.2 Run ####
To run the Hackystat Client daemon process, execute

|java -jar hackystat\_client.jar |
|:-------------------------------|

in the top level directory.


Known issues with the binary distribution: Currently, JavaMail is not playing nicely with the binary distribution. This will prevent the server from sending registration emails to newly registered clients. However, that is the only difficulty it causes.