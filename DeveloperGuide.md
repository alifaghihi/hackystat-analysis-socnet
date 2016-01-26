# 1.0 Introduction #

I have tried to make downloading, compiling, and running the SocNet system as painless and easy as possible. You'll need a few tools to start off with, but once you've got those in place it should come together quickly.


# 2.0 Downloading the SocNet Sources #

## 2.1 Install Necessary Tools ##
For this, you will need:
  * Subversion
  * Java
  * Ant
  * JAXB
  * JavaMail
  * Java Transaction Library

Install [Subversion](http://subversion.tigris.org/) 1.6.

Install [Java](http://java.sun.com/) 1.6.

Install [ANT](http://ant.apache.org/) 2.0.

Purportedly, in Java 1.6 JAXB and JavaMail are included, however in Ubuntu at least that was not the case, so for installation instructions for JavaMail, check out Hackystat's great pages on the subject [Installing JAXB](http://code.google.com/p/hackystat/wiki/InstallingJAXBinJava5/)  and [Installing JavaMail](http://code.google.com/p/hackystat/wiki/InstallingJavaMail/).

The last one, the Java Transaction Library, requires some special treatment because of click-through licenses disagreeing with Ivy. You can download the library [here](http://java.sun.com/javaee/technologies/jta/index.jsp/).

![http://hackystat-analysis-socnet.googlecode.com/files/JavaTransationAPIScreenshotAnnotated.png](http://hackystat-analysis-socnet.googlecode.com/files/JavaTransationAPIScreenshotAnnotated.png)

Download Java Transaction API Specification 1.1 Maintenance Release, both the class files and the Javadocs to whatever the environment variable ${java.io.tmpdir} resolves to. In Linux, this is "/tmp". Ivy will look for this library in the temporary folder, so once it's downloaded that's all you need to do.

Now we're ready to get down to business.

## 2.1 Checking out the SocNet Sources ##

Checkout the SocNet trunk into the directory of your choice using the following command:

|svn checkout http://hackystat-analysis-socnet.googlecode.com/svn/trunk/|
|:----------------------------------------------------------------------|

Now, on to building!

# 3.0 Building and Running SocNet #
## 3.1 Get Dependencies using Ivy ##

Fortunately, Ivy makes this part a breeze. If you have Ivy installed, run ant in the top level directory and Ivy will fetch everything you need.

If you don't have Ivy installed, you can still run ant, and it whimper about Ivy not being installed and prompt you to install ivy using the command:

|ant -f ivy.build.xml|
|:-------------------|

After executing that command, run ant and Ivy will merrily go about retrieving things so that you don't have to.

## 3.2 Set Up Configuration Files ##

### 3.2.1 Socnet Configuration File ###
The socnet.properties file should be placed in the computer's home directory in the hackystat home folder. This file is not strictly necessary, but if you want to use it, it allows additional specification and configuration. A sample file can be viewed/downloaded [here](http://hackystat-analysis-socnet.googlecode.com/svn/trunk/config/socnet.properties.sample/).

### 3.2.2 Twitter Client Configuration File ###
The socnet.twitter.properties file should be placed in the same place as the socnet.properties file. Note that the socnet.twitter.properties files must contain a valid Twitter username and valid Twitter password. The default API call limit for a Twitter account is 150 calls per hour.  Raise this at your peril--Twitter will blacklist your account if you repeatedly go over your call limit. It is included in the configuration file so that after Twitter has raised the API call limit, it is easy to change. A sample file can be viewed/downloaded [/ here](http://hackystat-analysis-socnet.googlecode.com/svn/trunk/config/socnet.twitter.properties.sample).

**All values in the socnet.twitter.properties configuration file are mandatory**

### 3.2.3 Hackystat Client Configuration File ###
Create a blank file named socnet.hackystatclient.properties in the hackystatclient folder within socnet folder, which is within the hackystat hidden folder in the user's home directory. In Linux, the abosolute path is

|$USER\_HOME/.hackystat/socnet/hackystatclient/socnet.hackystatclient.properties|
|:------------------------------------------------------------------------------|


Now it's time to run this bad boy

## 3.3 Running SocNet ##

### 3.3.1 Running the SocNet Server ###
First, we have to start up the server. To do this, execute

|ant run|
|:------|

in the top level directory.

### 3.3.2 Running the Twitter Client ###
Then, execute

|ant twitterclient|
|:----------------|

in the top level directory to start up the Twitter Client.

### 3.3.3 Running the Hackystat Client ###

#### 3.3.3.1 Setup ####
Execute

|ant projectselector|
|:------------------|

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

|ant hackystatclient |
|:-------------------|

in the top level directory.



Known Issues: The very first time you initialize the twitter account, the server throws an exception. This causes the Twitter client to throw an exception as well. If this occurs, restart both programs and it should run fine. I thought that this problem had been resolved, as it only appears on the very first initialization, but apparently the issue has reappeared.