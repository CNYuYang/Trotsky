#！/bin/sh

echo "➡️ 检查是否安装gradle"

if ! [ -x "$(command -v gradle)" ]; then
  echo 'Error: gradle is not installed.' >&2
  exit 1
fi

gradle -v

echo "➡️ 删除之前构建内容"

rm -rf build

echo "➡️ 获取最新源码"

git pull

echo "➡️ 开始构建"

gradle build -Dquarkus.package.type=native -Dquarkus.native.additional-build-args=-H:ResourceConfigurationFiles=resources-config.json

cp ./build/trotsky-beta-runner trotsky