A school project - a site that allows for creation of projects each of which has  
a number of jobs. Default users are an admin, a manager and a member of staff.  
Login as admin to create a project. Passwords are ted2012 for all.  

###Set up

I suppose you have a working eclipse JavaEE set up - including a tomcat server
(I used 7.0.32) and a jdk (for tomcat and eclipse alike) - I used 1.7.9.

1. Dump mysql-connector-java-5.1.24-bin.jar into $CATALINA_HOME\lib.
2. Install MySql (if you don't have it installed - you may need Net framework 4)
3. Create the DB schema
4. Edit context xml with your account/pass for MySQL server as well as the url
for the DB. Default :

```xml
<?xml version="1.0" encoding="UTF-8"?>
<Context antiJARLocking="true" path="/ted2012">
	<Resource name="jdbc/TestDB" auth="Container" type="javax.sql.DataSource"
		maxActive="100" maxIdle="30" maxWait="10000" username="root"
		password="root" factory="org.apache.tomcat.jdbc.pool.DataSourceFactory"
		driverClassName="com.mysql.jdbc.Driver"
		url="jdbc:mysql://localhost:3306/ted2012?useUnicode=yes&amp;characterEncoding=UTF-8"
		removeAbandoned="true" removeAbandonedTimeout="60" />
</Context>
````

Instructions to reproduce [jstl - el - read element from map](http://stackoverflow.com/q/12872965/281545)

5. Launch in eclipse > login as admin (username: ted2012 - pass: ted2012) >
 Project List > Create project > fill the fields in (like djufgjdg) > hit
 Add Staff > hit Create Project
6. As you can see the scriplet correctly picks up the succes message 
```Το project δημιουργήθηκε επιτυχώς ``` 
while JSTL no

The Bug solved in <http://stackoverflow.com/a/15745520/281545> :

5. Launch in eclipse > register an account with greek username IN ECLIPSE > login
as admin (username: ted2012 - pass: ted2012) > visit the profile of the user.
6. Shutdown Tomcat from eclipse, export war to  $CATALINA_HOME\webapps, launch
$CATALINA_HOME\bin\startup.bat, open the site in the browser login as admin and
try to visit the user profile.
7. ?
