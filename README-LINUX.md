# KuiKui OJ 项目 Linux 部署说明

本文档提供了项目在 Linux 系统上部署的相关说明和注意事项。

## 文件路径说明

项目已经做了以下适配，使其能够在 Linux 系统上正常运行：

1. 所有文件路径现在都使用了平台无关的方式表示，使用`File.separator`替代了硬编码的`\`分隔符。
2. 文件存储路径已经从 Windows 绝对路径修改为项目根目录的相对路径：
   - 用户头像存储在 `[项目根目录]/data/userHeader/` 目录下
   - 题目内容图片存储在 `[项目根目录]/data/questionContent/` 目录下
   - 代码沙箱临时文件存储在 `[项目根目录]/temp/` 目录下

## 目录结构

系统会自动创建以下目录（如果它们不存在）：
```
[项目根目录]
├── data
│   ├── userHeader      # 用户头像存储目录
│   └── questionContent # 题目内容图片存储目录
├── temp                # 代码执行临时目录
└── security.policy     # Java安全策略文件
```

## 部署步骤

1. 确保目标服务器已安装 JDK 8+ 和 Maven
2. 克隆代码到服务器：`git clone [项目仓库URL]`
3. 编译项目：`mvn clean package -DskipTests`
4. 确保 `security.policy` 文件存在于项目根目录下
5. 启动各个服务：
   ```bash
   # 网关服务
   nohup java -jar kuikuioj-bankend-gateway/target/kuikuioj-bankend-gateway-0.0.1-SNAPSHOT.jar &
   
   # 用户服务
   nohup java -jar kuikuioj-bankend-user/target/kuikuioj-bankend-user-0.0.1-SNAPSHOT.jar &
   
   # 题目服务
   nohup java -jar kuikuioj-bankend-question/target/kuikuioj-bankend-question-0.0.1-SNAPSHOT.jar &
   
   # 提交服务
   nohup java -jar kuikuioj-bankend-questionsubmit/target/kuikuioj-bankend-questionsubmit-0.0.1-SNAPSHOT.jar &
   
   # 竞赛服务
   nohup java -jar kuikuioj-bankend-competition/target/kuikuioj-bankend-competition-0.0.1-SNAPSHOT.jar &
   
   # AI服务
   nohup java -jar kuikuioj-bankend-ai/target/kuikuioj-bankend-ai-0.0.1-SNAPSHOT.jar &
   ```

## 数据库配置

确保在 Linux 服务器上已经安装了 MySQL 并创建了`kuikuioj`数据库，同时根据实际情况修改各个服务的`application.yml`配置文件中的数据库连接信息。

## Redis 配置

确保在 Linux 服务器上已经安装了 Redis，并根据实际情况修改各个服务的`application.yml`配置文件中的 Redis 连接信息。

## 注意事项

1. 确保 Linux 系统上已安装 Java 环境，推荐 JDK 8 及以上版本
2. 确保各个端口未被占用（8080, 8081, 8082, 8083, 8084, 8085）
3. 配置防火墙允许这些端口的访问
4. 如果使用 Nacos 作为服务发现，确保 Nacos 服务正常运行
5. 对于代码沙箱功能，确保 JDK 的 javac 和 java 命令可用
6. 请定期清理 temp 目录，避免磁盘空间占用过大 