package space.kuikuioj.kuikuiojbankendmodel.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TagCountVO {
    private String tagName;
    private Long count;
} 