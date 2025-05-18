package space.kuikuioj.kuikuiojbankendserviceclient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import space.kuikuioj.kuikuiojbankendcommon.back.BaseResponse;
import space.kuikuioj.kuikuiojbankendmodel.entity.Question;

/**
 * @author kuikui
 * @date 2025/5/17 13:04
 * @description
 */
@FeignClient(name = "service-question", path = "/api/question/inner")
public interface QuestionFeignClient {

    @PostMapping("/updateSubmitNum")
    void updateSubmitCountInner(@RequestBody Long questionId);

    @GetMapping("/selectById")
    Question selectByIdInner(@RequestParam("questionId") Long questionId);
    @GetMapping("/getCount")
    Long getCount();
}
