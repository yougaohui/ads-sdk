#!/bin/bash

# 广告SDK发布脚本
# 使用方法: ./scripts/publish.sh <version>

set -e

if [ $# -eq 0 ]; then
    echo "使用方法: $0 <version>"
    echo "例如: $0 1.0.0"
    exit 1
fi

VERSION=$1
TAG="v$VERSION"

echo "开始发布版本: $VERSION"

# 检查是否有未提交的更改
if [ -n "$(git status --porcelain)" ]; then
    echo "错误: 有未提交的更改，请先提交或暂存"
    git status
    exit 1
fi

# 更新版本号
echo "更新版本号为: $VERSION"
sed -i "s/VERSION_NAME=.*/VERSION_NAME=$VERSION/" gradle.properties

# 提交版本更新
git add gradle.properties
git commit -m "Bump version to $VERSION"

# 创建标签
echo "创建标签: $TAG"
git tag $TAG

# 推送更改和标签
echo "推送更改和标签到远程仓库"
git push origin main
git push origin $TAG

echo "发布完成！"
echo "版本 $VERSION 已推送到GitHub"
echo "GitHub Actions将自动构建并发布到GitHub Packages"
echo ""
echo "其他项目可以通过以下方式使用:"
echo "implementation 'com.github.yourusername:ads-core:$VERSION'"
echo "implementation 'com.github.yourusername:ads-cn:$VERSION'"
echo "implementation 'com.github.yourusername:ads-global:$VERSION'"
echo "implementation 'com.github.yourusername:ads-noop:$VERSION'"
