@echo off
cls

REM 
cd /d "%~dp0"

REM 
if not exist "bin" mkdir "bin"

REM 
javac -d "bin" src\main\*.java src\student\*.java src\lecturer\*.java src\admin\*.java
if errorlevel 1 (
    echo Compilation failed.
    pause
    exit /b
)

REM 
java -cp "bin" main.Main
pause