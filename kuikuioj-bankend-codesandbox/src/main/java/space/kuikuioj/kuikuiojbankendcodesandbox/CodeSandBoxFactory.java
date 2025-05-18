package space.kuikuioj.kuikuiojbankendcodesandbox;


import space.kuikuioj.kuikuiojbankendcodesandbox.impl.ExampleCodeSandBox;
import space.kuikuioj.kuikuiojbankendcodesandbox.impl.JavaDockerCodeSandBox;
import space.kuikuioj.kuikuiojbankendcodesandbox.impl.RemoteCodeSandBox;

/**
 * 代码沙箱工厂 （根据传入的字符串，创建不同的沙箱实例）
 * @author kuikui
 * @date 2025/4/5 18:08
 */
public class CodeSandBoxFactory {

    public static CodeSandBox newInstance(String type){
        switch (type) {
            case "example":
                return new ExampleCodeSandBox();
            case "docker":
                return new JavaDockerCodeSandBox();
            case "remote":
                return new RemoteCodeSandBox();
            default:
                return new ExampleCodeSandBox(); // 默认使用示例沙箱
        }
    }
}
