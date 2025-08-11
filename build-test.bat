@echo off
echo 正在测试 Android 广告SDK 构建...
echo.

echo 1. 清理项目...
call gradlew clean
if %ERRORLEVEL% neq 0 (
    echo 清理失败！
    pause
    exit /b 1
)

echo.
echo 2. 构建项目...
call gradlew build
if %ERRORLEVEL% neq 0 (
    echo 构建失败！
    pause
    exit /b 1
)

echo.
echo 3. 发布到本地Maven仓库...
call gradlew publishToMavenLocal
if %ERRORLEVEL% neq 0 (
    echo 发布失败！
    pause
    exit /b 1
)

echo.
echo 构建成功！项目已发布到本地Maven仓库。
pause
