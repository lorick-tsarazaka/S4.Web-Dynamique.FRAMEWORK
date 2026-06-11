@echo off
setlocal

pushd "%~dp0"

set "ROOT_DIR=%CD%"
set "BUILD_DIR=%ROOT_DIR%\build"
set "CLASSES_DIR=%BUILD_DIR%\WEB-INF\classes"
set "LIB_DIR=%BUILD_DIR%\WEB-INF\lib"
set "WEBAPPS_DIR=%BUILD_DIR%\WEB-INF\webapps"

if exist "%BUILD_DIR%" rmdir /s /q "%BUILD_DIR%"

mkdir "%CLASSES_DIR%"
mkdir "%LIB_DIR%"
mkdir "%WEBAPPS_DIR%"

if exist "src\WEB-INF\" xcopy "src\WEB-INF\*" "%BUILD_DIR%\WEB-INF\" /E /I /Y /Q >nul

if exist src\java\controller\*.java javac -d "%CLASSES_DIR%" src\java\controller\*.java
if exist src\java\model\*.java javac -d "%CLASSES_DIR%" src\java\model\*.java

if exist "%CLASSES_DIR%\controller" jar cf "%LIB_DIR%\Framework.jar" -C "%CLASSES_DIR%" controller

popd

endlocal
