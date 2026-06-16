@echo off
setlocal enabledelayedexpansion

REM ============================================================
REM  RUN SCRIPT (Windows)
REM  Build script for a Java-based web framework.
REM ============================================================

pushd "%~dp0"

REM --------------------------
REM  VARIABLES
REM --------------------------
set "APP_NAME=Framework"
set "SRC_DIR=src\java"
set "WEB_DIR=src\webapps"
set "BUILD_DIR=build"
set "CLASSES_DIR=%BUILD_DIR%\WEB-INF\classes"
set "LIB_DIR=%BUILD_DIR%\WEB-INF\lib"
set "LIB=lib"

REM ===========================
REM  NETTOYAGE ET PREPARATION
REM ===========================
echo.
echo Nettoyage du repertoire build...
if exist "%BUILD_DIR%" rmdir /s /q "%BUILD_DIR%"
mkdir "%CLASSES_DIR%"
mkdir "%LIB_DIR%"

REM ===========================
REM  COMPILATION DES FICHIERS JAVA
REM ===========================
echo Compilation des fichiers Java...

REM Generer sources.txt en evitant le probleme des espaces avec subst
subst B: "%CD%" >nul 2>&1

dir /s /b "B:\%SRC_DIR%\*.java" > "%TEMP%\sources.txt"

javac -cp "B:\%LIB%\*" -d "B:\%CLASSES_DIR%" @"%TEMP%\sources.txt"
set "RESULT=%ERRORLEVEL%"

del "%TEMP%\sources.txt" 2>nul
subst B: /d >nul 2>&1

if %RESULT% neq 0 (
    echo ERREUR: Echec de la compilation
    popd
    pause
    endlocal
    exit /b 1
)

REM ===========================
REM  COPIER LES FICHIERS WEB
REM ===========================
echo Copie des fichiers web...
if exist "%WEB_DIR%\" (
    xcopy "%WEB_DIR%\*" "%BUILD_DIR%\" /E /I /Y /Q >nul
)

REM ===========================
REM  GENERER LE JAR
REM ===========================
echo Creation du fichier JAR...
set "JAR_PATH=%LIB_DIR%\%APP_NAME%.jar"
if exist "%CLASSES_DIR%\mg\itu\framework" (
    jar cf "%JAR_PATH%" -C "%CLASSES_DIR%" mg
    if errorlevel 1 (
        echo ERREUR: Echec de la creation du JAR
    ) else (
        echo JAR cree: %JAR_PATH%
    )
)

echo.
echo ===================================
echo Build termine
echo ===================================

popd
endlocal