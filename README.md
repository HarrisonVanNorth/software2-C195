## Java Application Development

### Scenario / Purpose

You are working for a software company that has been contracted to develop a GUI-based scheduling desktop application.
The contract is with a global consulting organization that conducts business in multiple languages and has main offices
in Phoenix, Arizona; White Plains, New York; Montreal, Canada; and London, England. The consulting organization has
provided a MySQL database that the application must pull data from. The database is used for other systems, so its
structure cannot be modified.

The organization outlined specific business requirements that must be met as part of the application. From these
requirements, a system analyst at your company created solution statements for you to implement in developing the
application. These statements are listed in the requirements section.

Your company acquires Country and First-Level-Division data from a third party that is updated once per year. These
tables are pre-populated with read-only data. Please use the attachment “Locale Codes for Region and Language” to review
division data. Your company also supplies a list of contacts, which are pre-populated in the Contacts table; however,
administrative functions such as adding users are beyond the scope of the application and done by your company’s IT
support staff. Your application should be organized logically using one or more design patterns and generously commented
using Javadoc so your code can be read and maintained by other programmers.

### Author Information

- Harrison Van Nort
- Email: hvanno3@wgu.edu
- date: 05/12/2023

### Application Version

- 0.0.0 - No release

### IDE and java module Information

- IntelliJ IDEA Community Edition 2022.3.1
- JDK: Java SE 17.0.5
- JavaFX-SDK-17.0.1
- mysql connector: mysql-connector-j-8.0.32
- Library: mssql-jdbc:12.2.0

### Additional report

Custom report is displayed in the GUI on the Reports view when you first log in to the project.The report is displayed
in three tables. Click on each tab to see each report.

### How to

As the program starts, a login screen is presented. The user will
be required to have a valid username and password that matches
information in a mysql database. This program requires java 11 and
has not been tested with any other jvm. 
