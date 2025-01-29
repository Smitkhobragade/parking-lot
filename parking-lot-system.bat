@echo off
setlocal

:: Get the directory where the script is located
set SCRIPT_DIR=%~dp0

:: Check if Java is installed
where java >nul 2>nul
if %errorlevel% neq 0 (
    echo [ERROR] Java is not installed. Please install Java 17+.
    exit /b 1
)

:: Run the application
java -jar "%SCRIPT_DIR%parking-lot-system.jar" %*

