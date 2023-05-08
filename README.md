## Java Application Development

### Purpose of application
The purpose of this application is to provide a GUI based scheduling desktop application.

### Author Information
Harrison Van Nort
application version: 1.0  
date: 04/30/2023

### IDE and java module Information
IntelliJ IDEA 2022.3.1 (Community Edition)

#### javafx: openjfx-11.0.2
#### mysql connector: mysql-connector-j-8.0.32

### Additional report

For the custom report, I chose to display the number of appointments per Country. I chose to have sql do all the work and created a query that contains inner joins which combines three tables and spits out two columns (Country and count). 
The query is complicated but made the java side was very simple and I was able to pull this information into a tableview to present the data. 

### How to run the program

As the program starts, a login screen is presented. The user will
be required to have a valid username and password that matches 
information in a mysql database. This program requires java 11 and
has not been tested with any other jvm. 
