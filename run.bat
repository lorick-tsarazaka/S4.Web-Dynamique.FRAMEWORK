@echo off
setlocal enabledelayedexpansion

REM ============================================================
REM  BUILD FRAMEWORK
REM ============================================================

pushd "%~dp0"

REM --------------------------
REM VARIABLES
REM --------------------------
set "APP_NAME=Framework"
set "SRC_DIR=src\java"
set "WEB_DIR=src\webapps"
set "BUILD_DIR=build"
set "CLASSES_DIR=%BUILD_DIR%\WEB-INF\classes"
set "LIB_DIR=%BUILD_DIR%\WEB-INF\lib"
set "LIB=lib"

echo.
echo ===================================
echo Nettoyage...
echo ===================================

if exist "%BUILD_DIR%" rmdir /s /q "%BUILD_DIR%"

mkdir "%CLASSES_DIR%"
mkdir "%LIB_DIR%"

echo.
echo ===================================
echo Compilation Java...
echo ===================================

subst B: "%CD%" >nul 2>&1

dir /s /b "B:\%SRC_DIR%\*.java" > "%TEMP%\sources.txt"

javac -cp "B:\%LIB%\*" -d "B:\%CLASSES_DIR%" @"%TEMP%\sources.txt"

set "RESULT=%ERRORLEVEL%"

del "%TEMP%\sources.txt" 2>nul
subst B: /d >nul 2>&1

if %RESULT% neq 0 (
    echo.
    echo ERREUR : Compilation echouee.
    popd
    pause
    exit /b 1
)

echo.
echo ===================================
echo Copie des ressources Web...
echo ===================================

if exist "%WEB_DIR%" (
    xcopy "%WEB_DIR%\*" "%BUILD_DIR%\" /E /I /Y /Q >nul
)

echo.
echo ===================================
echo Extraction des JARs...
echo ===================================

REM 1) Extraire tous les JARs dans CLASSES_DIR (classes + META-INF)
for %%j in ("%LIB%\*.jar") do (
    if exist "%%~fj" (
        echo Extraction de %%~nxj
        pushd "%CLASSES_DIR%"
        jar xf "%%~fj"
        popd
    )
)

REM 2) Fusionner les fichiers SPI Spring (spring.handlers + spring.schemas)
REM    pour eviter que le dernier JAR extrait ecrase les precedents.
set "SPRING_HANDLERS=%TEMP%\spring.handlers.merged"
set "SPRING_SCHEMAS=%TEMP%\spring.schemas.merged"
if exist "%SPRING_HANDLERS%" del "%SPRING_HANDLERS%"
if exist "%SPRING_SCHEMAS%" del "%SPRING_SCHEMAS%"

for %%j in ("%LIB%\*.jar") do (
    if exist "%%~fj" (
        mkdir "%TEMP%\spring-merge" 2>nul
        pushd "%TEMP%\spring-merge"
        jar xf "%%~fj" 2>nul
        if exist "META-INF\spring.handlers" (
            type "META-INF\spring.handlers" >> "%SPRING_HANDLERS%"
            echo. >> "%SPRING_HANDLERS%"
        )
        if exist "META-INF\spring.schemas" (
            type "META-INF\spring.schemas" >> "%SPRING_SCHEMAS%"
            echo. >> "%SPRING_SCHEMAS%"
        )
        popd
        rmdir /s /q "%TEMP%\spring-merge" 2>nul
    )
)

REM 3) Copier les fichiers fusionnes dans CLASSES_DIR (ecrase les fichiers partiels)
if exist "%SPRING_HANDLERS%" (
    if not exist "%CLASSES_DIR%\META-INF" mkdir "%CLASSES_DIR%\META-INF"
    copy /Y "%SPRING_HANDLERS%" "%CLASSES_DIR%\META-INF\spring.handlers" >nul
    echo Fichier spring.handlers fusionne.
    del "%SPRING_HANDLERS%"
)
if exist "%SPRING_SCHEMAS%" (
    if not exist "%CLASSES_DIR%\META-INF" mkdir "%CLASSES_DIR%\META-INF"
    copy /Y "%SPRING_SCHEMAS%" "%CLASSES_DIR%\META-INF\spring.schemas" >nul
    echo Fichier spring.schemas fusionne.
    del "%SPRING_SCHEMAS%"
)

echo.
echo ===================================
echo Creation du Framework.jar...
echo ===================================

jar cf "%LIB_DIR%\%APP_NAME%.jar" -C "%CLASSES_DIR%" .

if errorlevel 1 (
    echo ERREUR : Creation du JAR impossible.
    popd
    pause
    exit /b 1
)

echo.
echo JAR cree :
echo %LIB_DIR%\%APP_NAME%.jar

echo.
echo ===================================
echo BUILD TERMINE
echo ===================================

popd
pause
endlocal