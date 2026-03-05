@echo off
chcp 65001 >nul 2>&1
echo ========================================
echo Building Vue project for Spring Boot...
echo ========================================

echo.
echo [1/3] Cleaning old build files...
if exist "..\src\main\resources\static" (
    rmdir /s /q "..\src\main\resources\static"
    echo Done.
)

echo.
echo [2/3] Building Vue project...
call pnpm exec vite build

if %errorlevel% neq 0 (
    echo.
    echo Build failed!
    pause
    exit /b %errorlevel%
)

echo.
echo [3/3] Verifying build output...
if exist "..\src\main\resources\static\index.html" (
    echo Build succeeded!
    echo.
    echo Output: src/main/resources/static/
    echo.
    echo Start Spring Boot and visit http://localhost:8080
) else (
    echo Build output not found!
)

echo.
echo ========================================
echo Done
echo ========================================
pause
