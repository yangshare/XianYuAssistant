@echo off
setlocal enabledelayedexpansion

echo ====================================
echo XianYu Assistant - Release Script
echo ====================================
echo.

:: 检查工作目录是否干净
git diff-index --quiet HEAD -- 2>nul
if errorlevel 1 (
    echo Error: Working directory has uncommitted changes
    echo Please commit or stash your changes before releasing
    pause
    exit /b 1
)

set /p version="Please enter version number (e.g., 1.0.0): "

if "%version%"=="" (
    echo Error: Version number cannot be empty
    pause
    exit /b 1
)

:: 验证版本号格式 (semver: X.Y.Z)
:: 使用 <nul set /p= 避免echo添加空格，并使用简化的正则表达式
<nul set /p=%version% | findstr /r "[0-9][0-9]*\.[0-9][0-9]*\.[0-9][0-9]*" >nul
if errorlevel 1 (
    echo Error: Invalid version format. Expected: X.Y.Z ^(e.g., 1.0.0^)
    pause
    exit /b 1
)

:: 检查 tag 是否已存在
git tag -l "v%version%" 2>nul | findstr /C:"v%version%" >nul
if %errorlevel% equ 0 (
    echo Error: Tag v%version% already exists
    pause
    exit /b 1
)

echo.
echo Preparing to release version: v%version%
echo.

git tag v%version%
if errorlevel 1 (
    echo Error: Failed to create Git Tag
    pause
    exit /b 1
)

echo Git Tag v%version% created successfully
echo.

git push origin v%version%
if errorlevel 1 (
    echo Error: Failed to push Tag to remote repository
    echo Cleaning up local tag...
    git tag -d v%version% >nul 2>&1
    pause
    exit /b 1
)

echo.
echo ====================================
echo Release successful! Version v%version% has been pushed to remote repository
echo GitHub Actions will automatically build Docker image and push to Docker Hub
echo ====================================
pause
