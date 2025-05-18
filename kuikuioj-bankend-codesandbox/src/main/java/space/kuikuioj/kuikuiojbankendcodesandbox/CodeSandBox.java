package space.kuikuioj.kuikuiojbankendcodesandbox;

import space.kuikuioj.kuikuiojbankendmodel.dto.ExecuteCodeRequest;
import space.kuikuioj.kuikuiojbankendmodel.dto.ExecuteCodeResponse;

import java.io.IOException;

/**
 * @author kuikui
 * @date 2025/4/5 17:46
 */
public interface CodeSandBox {

    /**
     * 执行代码
     */
    ExecuteCodeResponse executeCode(ExecuteCodeRequest executeCodeRequest) throws IOException, InterruptedException;

}
