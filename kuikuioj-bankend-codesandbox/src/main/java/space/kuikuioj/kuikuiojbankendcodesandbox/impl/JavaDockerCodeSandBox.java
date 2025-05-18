package space.kuikuioj.kuikuiojbankendcodesandbox.impl;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.file.FileWriter;
import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import space.kuikuioj.kuikuiojbankendcodesandbox.CodeSandBox;
import space.kuikuioj.kuikuiojbankendmodel.dto.ExecuteCodeRequest;
import space.kuikuioj.kuikuiojbankendmodel.dto.ExecuteCodeResponse;
import space.kuikuioj.kuikuiojbankendmodel.entity.JudgeInfo;


import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 基于Docker的代码沙箱实现
 * @author kuikui
 * @date 2025/5/1 9:44
 */
@Component
@Slf4j
public class JavaDockerCodeSandBox implements CodeSandBox {

    @Override
    public ExecuteCodeResponse executeCode(ExecuteCodeRequest executeCodeRequest) throws IOException, InterruptedException {
        return null;
    }
}
