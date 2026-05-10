@echo off
setlocal

set APP_HOME=%~dp0
set GRADLE_VERSION=8.10.2
set GRADLE_HOME=%APP_HOME%.gradle\wrapper\dists\gradle-%GRADLE_VERSION%
set GRADLE_BIN=%GRADLE_HOME%\gradle-%GRADLE_VERSION%\bin\gradle.bat
set GRADLE_ZIP=%APP_HOME%.gradle\wrapper\dists\gradle-%GRADLE_VERSION%-bin.zip

if exist "%GRADLE_BIN%" goto run

if not exist "%GRADLE_HOME%" mkdir "%GRADLE_HOME%"
if not exist "%GRADLE_ZIP%" (
  powershell -NoProfile -ExecutionPolicy Bypass -Command "$ErrorActionPreference = 'Stop'; Invoke-WebRequest -Uri 'https://services.gradle.org/distributions/gradle-%GRADLE_VERSION%-bin.zip' -OutFile '%GRADLE_ZIP%'"
  if errorlevel 1 exit /b %errorlevel%
)

powershell -NoProfile -ExecutionPolicy Bypass -Command "$ErrorActionPreference = 'Stop'; Expand-Archive -Force -Path '%GRADLE_ZIP%' -DestinationPath '%GRADLE_HOME%'"
if errorlevel 1 exit /b %errorlevel%

:run
call "%GRADLE_BIN%" %*
exit /b %errorlevel%
