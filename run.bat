@echo off
setlocal enabledelayedexpansion

REM ============================================================
REM  RUN SCRIPT (Windows)
REM  Generalised build script for a Java-based web framework.
REM ============================================================

pushd "%~dp0"

REM --------------------------
REM  CONFIGURATION (customise)
REM --------------------------
set "ROOT_DIR=%CD%"

set "SRC_DIR=%ROOT_DIR%\src"
set "RES_DIR=%ROOT_DIR%\src\WEB-INF"
set "BUILD_DIR=%ROOT_DIR%\build"
set "CLASSES_DIR=%BUILD_DIR%\WEB-INF\classes"
set "LIB_DIR=%BUILD_DIR%\WEB-INF\lib"
set "WEBAPPS_DIR=%BUILD_DIR%\WEB-INF\webapps"

set "PACKAGES=controller model"

set "CP=%ROOT_DIR%\lib\*"

set "JAR_NAME=Framework"

REM --------------------------
REM  CLEAN & INIT BUILD DIR
REM --------------------------
echo === Cleaning and creating build directory ===

if exist "%BUILD_DIR%" rmdir /s /q "%BUILD_DIR%"

mkdir "%CLASSES_DIR%"
mkdir "%LIB_DIR%"
mkdir "%WEBAPPS_DIR%"

REM --------------------------
REM  COPY RESOURCES
REM --------------------------
echo === Copying WEB-INF resources ===
if exist "%RES_DIR%\" (
    xcopy "%RES_DIR%\*" "%BUILD_DIR%\WEB-INF\" /E /I /Y /Q >nul
)

REM --------------------------
REM  COMPILE JAVA SOURCES
REM --------------------------
echo === Compiling Java sources ===
for %%p in (%PACKAGES%) do (
    if exist "%SRC_DIR%\java\%%p\*.java" (
        echo   Compiling package: %%p
        pushd "%SRC_DIR%\java\%%p"
        javac -cp "%CP%" -d "%CLASSES_DIR%" *.java
        if errorlevel 1 (
            echo ERROR: Compilation failed for package %%p
        ) else (
            echo   OK
        )
        popd
    ) else (
        echo   No .java files found in package: %%p
    )
)

REM --------------------------
REM  PACKAGE INTO JAR
REM --------------------------
echo === Creating JAR archive ===
set "JAR_PATH=%LIB_DIR%\%JAR_NAME%.jar"
if exist "%CLASSES_DIR%\controller" (
    jar cf "%JAR_PATH%" -C "%CLASSES_DIR%" controller
    if errorlevel 1 (
        echo ERROR: JAR creation failed
    ) else (
        echo   JAR created at: %JAR_PATH%
    )
)

echo === Done ===

popd
endlocal