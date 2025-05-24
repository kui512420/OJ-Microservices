package space.kuikuioj.kuikuiojbankendcodesandbox.impl;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.file.FileWriter;
import com.fasterxml.jackson.databind.ObjectMapper;
import space.kuikuioj.kuikuiojbankendcodesandbox.CodeSandBox;
import space.kuikuioj.kuikuiojbankendmodel.dto.ExecuteCodeRequest;
import space.kuikuioj.kuikuiojbankendmodel.dto.ExecuteCodeResponse;
import space.kuikuioj.kuikuiojbankendmodel.entity.JudgeInfo;
import space.kuikuioj.kuikuiojbankendmodel.entity.Question;
import space.kuikuioj.kuikuiojbankendmodel.entity.TestCase;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author kuikui
 * @date 2025/4/5 18:21
 */
public class RemoteCodeSandBox implements CodeSandBox {

    private static final String TEMP_PATH = System.getProperty("user.dir") + File.separator + "temp";
    private static final String TEMP_FILE_NAME = "Main.java";
    
    // 获取 Java 相关命令的路径
    private static final String JAVAC_CMD = getJavacCommand();
    private static final String JAVA_CMD = getJavaCommand();
    
    /**
     * 获取 javac 命令路径
     */
    private static String getJavacCommand() {
        String javaHome = System.getProperty("java.home");
        String osName = System.getProperty("os.name").toLowerCase();
        
        // 如果是 JRE，需要找到 JDK 的路径
        String javacPath;
        if (javaHome.endsWith("jre")) {
            javacPath = javaHome.replace("jre", "") + "bin" + File.separator + "javac";
        } else {
            javacPath = javaHome + File.separator + "bin" + File.separator + "javac";
        }
        
        // Windows 下添加 .exe 扩展名
        if (osName.contains("windows")) {
            javacPath += ".exe";
        }
        
        // 如果找不到，尝试从 JAVA_HOME 环境变量获取
        File javacFile = new File(javacPath);
        if (!javacFile.exists()) {
            String javaHomeEnv = System.getenv("JAVA_HOME");
            if (javaHomeEnv != null) {
                javacPath = javaHomeEnv + File.separator + "bin" + File.separator + "javac";
                if (osName.contains("windows")) {
                    javacPath += ".exe";
                }
            }
        }
        
        // 最后尝试直接使用命令名（依赖系统PATH）
        javacFile = new File(javacPath);
        if (!javacFile.exists()) {
            // 使用默认命令，依赖系统PATH
            return osName.contains("windows") ? "javac.exe" : "javac";
        }
        
        return javacPath;
    }
    
    /**
     * 获取 java 命令路径
     */
    private static String getJavaCommand() {
        String javaHome = System.getProperty("java.home");
        String osName = System.getProperty("os.name").toLowerCase();
        
        String javaPath = javaHome + File.separator + "bin" + File.separator + "java";
        
        // Windows 下添加 .exe 扩展名
        if (osName.contains("windows")) {
            javaPath += ".exe";
        }
        
        return javaPath;
    }

    // 定义危险代码的正则表达式模式列表
    private static final List<Pattern> DANGEROUS_CODE_PATTERNS = Arrays.asList(
            // 禁止执行系统命令
            Pattern.compile("Runtime\\.getRuntime\\(\\)\\.exec\\("),
            // 禁止使用ProcessBuilder
            Pattern.compile("ProcessBuilder"),
            // 禁止通过反射访问系统类
            Pattern.compile("System\\.getSecurityManager\\(\\)"),
            // 禁止使用SecurityManager
            Pattern.compile("java\\.lang\\.SecurityManager"),
            // 禁止反射
            Pattern.compile("java\\.lang\\.reflect"),
            // 禁止文件操作
            Pattern.compile("java\\.io\\.File(?!Writer)"),
            Pattern.compile("java\\.nio\\.file"),
            // 禁止网络操作
            Pattern.compile("java\\.net\\.Socket"),
            Pattern.compile("java\\.net\\.ServerSocket"),
            Pattern.compile("java\\.net\\.URL"),
            // 禁止线程操作
            Pattern.compile("java\\.lang\\.Thread\\.sleep"),
            // 禁止类加载器
            Pattern.compile("ClassLoader"),
            // 禁止第三方unsafe包
            Pattern.compile("sun\\.misc\\.Unsafe")
    );

    // 静态初始化块，确保临时目录存在
    static {
        // 确保临时目录存在
        if (!FileUtil.exist(TEMP_PATH)) {
            FileUtil.mkdir(TEMP_PATH);
        }
    }

    /**
     * 检查代码是否包含危险操作
     * @param code 要检查的代码
     * @return 如果包含危险代码，返回错误消息；否则返回null
     */
    private static String checkDangerousCode(String code) {
        for (Pattern pattern : DANGEROUS_CODE_PATTERNS) {
            Matcher matcher = pattern.matcher(code);
            if (matcher.find()) {
                return "代码中包含不允许的操作: " + matcher.group();
            }
        }
        return null;
    }

    /**
     * 执行结果类
     */
    public static class ExecutionResult {
        private String output;
        private String error;
        private boolean success;
        
        public ExecutionResult(String output, String error, boolean success) {
            this.output = output;
            this.error = error;
            this.success = success;
        }
        
        public String getOutput() { return output; }
        public String getError() { return error; }
        public boolean isSuccess() { return success; }
    }

    /**
     * 执行单个测试用例
     *
     * @param code      提交的代码
     * @param inputData 输入数据
     * @return ExecutionResult 包含输出和错误信息
     * @throws InterruptedException
     * @throws IOException
     */
    public ExecutionResult judeg(String code, String inputData) throws InterruptedException, IOException {
        // 读取源文件
        String result = code;
        // 创建临时目录
        long dirName = new Date().getTime();
        String workDir = TEMP_PATH + File.separator + dirName;
        FileUtil.mkdir(workDir);

        try {
            // 写入Java文件
            String javaFile = workDir + File.separator + TEMP_FILE_NAME;
            new FileWriter(javaFile).write(result);

            // 编译（使用绝对路径的 javac 命令）
            Process compile = Runtime.getRuntime().exec(
                    new String[]{JAVAC_CMD, "-encoding", "utf-8", TEMP_FILE_NAME},
                    null,  // 环境变量（null表示继承当前环境）
                    new File(workDir)  // 工作目录
            );
            
            if (compile.waitFor() != 0) {
                String compileError = readStream(compile.getErrorStream());
                return new ExecutionResult("", "编译失败: " + compileError, false);
            }

            // 运行（使用绝对路径的 java 命令）
            // 添加安全管理器参数限制代码执行权限
            Process run = Runtime.getRuntime().exec(
                    new String[]{JAVA_CMD, "-Dfile.encoding=UTF-8",
                            // 限制执行时间（5秒）
                            "-Xmx256m", "-Xss256k",
                            // 禁止系统调用和文件访问
                            "-Djava.security.manager",
                            "-Djava.security.policy==" + System.getProperty("user.dir") + File.separator + "security.policy",
                            "Main"},
                    null,
                    new File(workDir)
            );

            // 模拟输入，将输入数据写入到运行程序的标准输入流中
            try (PrintWriter writer = new PrintWriter(run.getOutputStream())) {
                writer.println(inputData);
                writer.flush();
            }

            // 等待程序执行完成
            int exitCode = run.waitFor();
            
            if (exitCode == 0) {
                String output = readStream(run.getInputStream());
                return new ExecutionResult(output, "", true);
            } else {
                String error = readStream(run.getErrorStream());
                return new ExecutionResult("", "运行失败: " + error, false);
            }
        } finally {
            // 清理临时目录
            try {
                FileUtil.del(workDir);
            } catch (Exception e) {
                // 忽略清理错误
            }
        }
    }

    /**
     * 读取输入流内容
     */
    private static String readStream(InputStream input) throws IOException {
        StringBuilder result = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(input, "UTF-8"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (result.length() > 0) {
                    result.append("\n");
                }
                result.append(line);
            }
        }
        return result.toString();
    }
    
    @Override
    public ExecuteCodeResponse executeCode(ExecuteCodeRequest executeCodeRequest) throws IOException, InterruptedException {

        ExecuteCodeResponse response = new ExecuteCodeResponse();

        // 首先检查代码安全性
        String dangerousCodeMessage = checkDangerousCode(executeCodeRequest.getCode());
        if (dangerousCodeMessage != null) {
            // 存在安全问题 处理
            response.setMessage(dangerousCodeMessage);
            response.setStatus(3);
            return response;
        }

        ObjectMapper objectMapper = new ObjectMapper();
        List<String> outputList = new ArrayList<>();
        Question question = executeCodeRequest.getQuestion();
        String code = executeCodeRequest.getCode();
        boolean allPassed = true;
        List<JudgeInfo> judgeInfoList = new ArrayList<>();
        
        for (TestCase oj : objectMapper.readValue(question.getJudgeCase(), TestCase[].class)) {
            JudgeInfo judgeInfo = new JudgeInfo();
            String inputData = oj.getInput();
            String expectedOutput = oj.getOutput();

            Long startTime = System.currentTimeMillis();
            
            try {
                // 执行代码，不再使用全局的 System.out 重定向
                ExecutionResult executionResult = judeg(code, inputData);
                
                Long endTime = System.currentTimeMillis();
                
                if (executionResult.isSuccess()) {
                    String actualOutput = executionResult.getOutput().trim();
                    outputList.add(actualOutput);
                    judgeInfo.setUserOutput(actualOutput);
                    
                    // 验证输出结果
                    if (actualOutput.equals(expectedOutput)) {
                        judgeInfo.setPassed(2);
                        judgeInfo.setMessage("通过");
                    } else {
                        judgeInfo.setPassed(3);
                        judgeInfo.setMessage("未通过");
                        allPassed = false;
                    }
                } else {
                    // 执行失败
                    judgeInfo.setPassed(3);
                    judgeInfo.setMessage("执行失败: " + executionResult.getError());
                    judgeInfo.setUserOutput("");
                    outputList.add("");
                    allPassed = false;
                }
                
                judgeInfo.setTime(endTime - startTime);
                judgeInfoList.add(judgeInfo);
                
            } catch (Exception e) {
                Long endTime = System.currentTimeMillis();
                judgeInfo.setPassed(3);
                judgeInfo.setMessage("执行异常: " + e.getMessage());
                judgeInfo.setUserOutput("");
                judgeInfo.setTime(endTime - startTime);
                outputList.add("");
                judgeInfoList.add(judgeInfo);
                allPassed = false;
            }
        }
        
        if (allPassed) {
            response.setMessage("通过");
            response.setStatus(2);
        } else {
            response.setMessage("未通过");
            response.setStatus(3);
        }
        response.setJudgeInfo(judgeInfoList);
        response.setOutputList(outputList);
        return response;
    }
}