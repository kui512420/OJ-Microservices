package space.kuikuioj.kuikuiojbankendmodel.dto;

import lombok.Data;

@Data
public class SubmitRankRequest {
    private Integer type;
    private Integer page;
    private Integer count;
}
