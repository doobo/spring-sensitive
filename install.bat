@echo off
:: 设置 JAVA_HOME（Windows 路径使用反斜杠 \）
set JAVA_HOME="C:\users\Administrator\.jdks\ms-21.0.7"
:: 添加到 PATH
set PATH=%JAVA_HOME%\bin;%PATH%

:: 验证变量
echo JAVA_HOME: %JAVA_HOME%
java -version
:: powershell设置的环境变量
:: $env:JAVA_HOME = "C:/users/Administrator/.jdks/ms-21.0.7"
:: $env:Path += ";$env:JAVA_HOME\bin"
CALL mvn clean
CALL mvn release:prepare
CALL mvn release:perform
