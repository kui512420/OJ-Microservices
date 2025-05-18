package space.kuikuioj.kuikuiojbankendcodesandbox;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import space.kuikuioj.kuikuiojbankendmodel.dto.ExecuteCodeRequest;
import space.kuikuioj.kuikuiojbankendmodel.dto.ExecuteCodeResponse;


import java.io.IOException;

/**
 * @author kuikui
 * @date 2025/4/5 18:27
 */
@AllArgsConstructor
public class CodeSandBoxProxy implements CodeSandBox{

    private  CodeSandBox codeSandBox;
    @Override
    public ExecuteCodeResponse executeCode(ExecuteCodeRequest executeCodeRequest) throws IOException, InterruptedException {
        ExecuteCodeResponse executeCodeResponse = codeSandBox.executeCode(executeCodeRequest);
        return executeCodeResponse;
    }
}
