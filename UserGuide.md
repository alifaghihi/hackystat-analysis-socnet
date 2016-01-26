# 1.0 Introduction #

Currently, only a few of the planned SocNet services are available to users. Sensors that have been implemented include:
  * Twitter Sensor
  * Hackystat Sensor

Coming sensors are:
  * Ohloh Sensor
  * ~~Facebook Sensor~~ **{Update}** Because of Facebook's data use policies, the Facebook application is currently on hold indefinitely. In the future, we hope to offer similar functionality from a more user-friendly social networking utility, such as OpenSocial or Orkut.


Future sensors that could also be fun include media player sensors, to log the music you listen to while you code.

Additionally, visualization and analysis tools are planned for future releases.

See the individual sections below for instructions on registering with each of the SocNet sensors.


# 2.0 Twitter Sensor #

## 2.1 About the Twitter Sensor ##
The Twitter Sensor sends information about your Twitter friends and followers to the SocNet server.

## 2.2 Using the Twitter Sensor ##
To register with the SocNet Twitter sensor, follow the Twitter account "HackystatSocNet". The sensor will then be able to ascertain who you are following and who follows you, and will persist that relationship information in the SocNet social media graph.

# 3.0 Hackystat Sensor #

## 3.1 About the Hackystat Sensor ##
The Hackystat Sensor asks you which projects you wish to release information about to the SocNet server. For each project you select, you may select certain dates for which the Hackystat sensor is allowed access. Then, for the projects and dates allowed, the Hackystat Sensor sends Build, Churn, CodeIssue, Commit, Coverage, CyclomaticComplexity, DevTime, Issue, and UnitTest Telemetry streams to the SocNet server.

## 3.2 Before You Start Using the Hackystat Sensor ##
To use the Hackystat Sensor you must first be a Hackystat user. If you are not a Hackystat user, but would like to become one (and we certainly recommend that!), head over to [the Hackystat home page](http://www.hackystat.org) to learn more about Hackystat and get registered.

Once you've gotten registered and played around with the Hackystat system a bit, you're ready go to.

## 3.3 Download the Hackystat Client Binary Distribution ##

Download the latest release of the SocNet Hackystat from the featured downloads section of the project page. Today, the latest download is [SocnetHackystatClient\_0.2a-bin.zip](http://hackystat-analysis-socnet.googlecode.com/files/SocnetHackystatClient_0.2a-bin.zip).

Once you have that downloaded, unzip it to the location you would like to run it from.

## 3.4 Create the Hackystat Client Properties File ##

Create a blank file named socnet.hackystatclient.properties in the hackystatclient folder within socnet folder, which is within the hackystat hidden folder in the user's home directory. In Linux, the abosolute path is

|$USER\_HOME/.hackystat/socnet/hackystatclient/socnet.hackystatclient.properties|
|:------------------------------------------------------------------------------|

## 3.5 Using the Hackystat Client ##

### 3.5.1 Setup ###

Execute

|java -jar project\_selector.jar|
|:------------------------------|

in the top level directory of the archive that you unxipped to start up the project selection dialog. That should bring up a dialog box that looks something like this:

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

### 3.5.2 Run ###
To run the Hackystat Client daemon process, execute

|java -jar hackystat\_client.jar |
|:-------------------------------|

in the top level directory. It will start sending information right away!