package space.kuikuioj.kuikuiojbankendcommon.config;

import cn.hutool.core.io.FileUtil;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;
import java.io.File;

/**
 * 路径初始化器，确保系统所需的目录在应用启动时存在
 */
@Component
@Configuration
public class PathInitializer {

    /**
     * 系统所需的路径列表
     */
    private static final String[] REQUIRED_PATHS = {
            // 代码执行临时目录
            System.getProperty("user.dir") + File.separator + "temp",
            // 用户头像目录
            System.getProperty("user.dir") + File.separator + "data" + File.separator + "userHeader",
            // 题目内容图片目录
            System.getProperty("user.dir") + File.separator + "data" + File.separator + "questionContent"
    };

    /**
     * 在应用启动时创建必要的目录
     */
    @PostConstruct
    public void initPaths() {
        for (String path : REQUIRED_PATHS) {
            if (!FileUtil.exist(path)) {
                FileUtil.mkdir(path);
                System.out.println("已创建目录: " + path);
            }
        }
        
        // 检查安全策略文件是否存在
        String policyPath = System.getProperty("user.dir") + File.separator + "security.policy";
        if (!FileUtil.exist(policyPath)) {
            System.out.println("警告: 未找到安全策略文件: " + policyPath);
            System.out.println("请确保security.policy文件位于项目根目录");
        }
    }
} 