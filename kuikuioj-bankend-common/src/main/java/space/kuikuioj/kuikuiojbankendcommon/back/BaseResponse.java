package space.kuikuioj.kuikuiojbankendcommon.back;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author kuikui
 * @date 2025/3/15 16:20
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BaseResponse<T> implements Serializable {
    private int code;
    private String message;
    private T data;

}
