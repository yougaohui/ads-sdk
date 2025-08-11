@echo off
setlocal enabledelayedexpansion

REM 广告SDK发布脚本 (Windows版本)
REM 使用方法: publish.bat <version>
REM 例如: publish.bat 1.0.0

if "%~1"=="" (
    echo 使用方法: %0 ^<version^>
    echo 例如: %0 1.0.0
    exit /b 1
)

set VERSION=%~1
set TAG=v%VERSION%

echo 开始发布版本: %VERSION%

REM 检查是否有未提交的更改
git status --porcelain >nul 2>&1
if %errorlevel% neq 0 (
    echo 错误: 有未提交的更改，请先提交或暂存
    git status
    exit /b 1
)

REM 更新版本号
echo 更新版本号为: %VERSION%
powershell -Command "(Get-Content gradle.properties) -replace 'VERSION_NAME=.*', 'VERSION_NAME=%VERSION%' | Set-Content gradle.properties"

REM 提交版本更新
git add gradle.properties
git commit -m "Bump version to %VERSION%"

REM 创建标签
echo 创建标签: %TAG%
git tag %TAG%

REM 推送更改和标签
echo 推送更改和标签到远程仓库
git push origin main
git push origin %TAG%

echo 发布完成！
echo 版本 %VERSION% 已推送到GitHub
echo GitHub Actions将自动构建并发布到GitHub Packages
echo.
echo 其他项目可以通过以下方式使用:
echo implementation 'com.github.yourusername:ads-core:%VERSION%'
echo implementation 'com.github.yourusername:ads-cn:%VERSION%'
echo implementation 'com.github.yourusername:ads-global:%VERSION%'
echo implementation 'com.github.yourusername:ads-noop:%VERSION%'

pause
