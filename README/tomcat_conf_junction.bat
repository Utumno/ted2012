REM @echo off
REM see http://stackoverflow.com/questions/1645843/batch-file-resolve-absolute-path-from-relative-path-and-or-file-name

set PATH_TO_ECLIPSE_SERVER_CONFIG=..\..\Servers\"Tomcat v7.0 Server at localhost-config"
set PATH_TO_PROJECT_SERVER_CONFIG=..\conf\

rem // save current directory
pushd .

rem // change to relative directory and save value of CD (current directory) variable
cd %PATH_TO_ECLIPSE_SERVER_CONFIG%
set PATH_TO_ECLIPSE_SERVER_CONFIG=%CD%

rem // restore current directory
popd

cd %PATH_TO_PROJECT_SERVER_CONFIG%
set PATH_TO_PROJECT_SERVER_CONFIG=%CD%

echo PATH_TO_ECLIPSE_SERVER_CONFIG : %PATH_TO_ECLIPSE_SERVER_CONFIG%
echo PATH_TO_PROJECT_SERVER_CONFIG  : %PATH_TO_PROJECT_SERVER_CONFIG%

junction "%PATH_TO_PROJECT_SERVER_CONFIG%\Servers_eclipse_project_tomcat7_conf" "%PATH_TO_ECLIPSE_SERVER_CONFIG%"

pause
