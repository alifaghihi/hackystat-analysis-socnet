SocNet is a tool chain for collecting and analyzing data from Hackystat and various social networking venues in order to study the relationships between one's social network and one's programming practices. SocNet aims to measure the effects of certain intangibles on coding practices. Environment, philosophies, hobbies, these are things that can't be directly measured, but that have an impact on work; it is those hidden variables that SocNet seeks to quantify.

Data collected from Hackystat, "an open source framework for collection, analysis, visualization, interpretation, annotation, and dissemination of software development process and product data", plays an important role in the analysis of coding practices. To learn more about Hackystat, visit their [home page](http://code.google.com/p/hackystat/).

SocNet users register with a number of sensors that interface with social networking platforms that send data to the SocNet server where it can be accessed for analysis.

![http://hackystat-analysis-socnet.googlecode.com/files/System%20Design.jpeg](http://hackystat-analysis-socnet.googlecode.com/files/System%20Design.jpeg)

The persistent store that this system uses is [Neo4j](http://neo4j.org/), and an example of a graph like the one developed by SocNet is as follows:

![http://hackystat-analysis-socnet.googlecode.com/files/Database%20Structure.jpeg](http://hackystat-analysis-socnet.googlecode.com/files/Database%20Structure.jpeg)