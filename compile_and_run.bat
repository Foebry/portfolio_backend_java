@echo off

REM Set project directory
set PROJECT_DIRECTORY=e:/projects/portfolio/backend

REM Compile Java code
cd %PROJECT_DIRECTORY%/src && javac -source 17 -target 17 -d %PROJECT_DIRECTORY%/bin -cp ".;lib/mysql-connector-j-8.2.0.jar" *.java

REM Run the main class
cd %PROJECT_DIRECTORY% && java -cp "bin;lib/mysql-connector-j-8.2.0.jar" Server