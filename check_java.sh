#!/bin/bash
echo "检查 Java 环境:"
echo "JAVA_HOME: $JAVA_HOME"
echo "which java: $(which java)"
echo "which javac: $(which javac)"
echo "java -version:"
java -version
echo "javac -version:"
javac -version 2>&1 || echo "javac 命令不存在"
echo "当前用户: $(whoami)"
echo "PATH: $PATH" 