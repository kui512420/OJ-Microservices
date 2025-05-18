package space.kuikuioj.kuikuiojbankendserviceclient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
import space.kuikuioj.kuikuiojbankendmodel.dto.SubmitRequest;
import space.kuikuioj.kuikuiojbankendmodel.entity.QuestionSubmit;

import java.util.List;

@FeignClient(name = "service-questionsubmit", path = "/api/submit/inner")
public interface QuestionSubmitFeignClient {

    @PostMapping("/sub")
    Long submitInner(@RequestBody SubmitRequest submitRequest);

    @GetMapping("/selectCount")
    Long selectCountInner(
            @RequestParam("userId") Long userId,
            @RequestParam("competitionId") Long competitionId
    );

    @GetMapping("/selectAcceptCount")
    Long selectAcceptCount(
            @RequestParam("userId") Long userId,
            @RequestParam("competitionId") Long competitionId
    );

    @GetMapping("/selectList")
    List<QuestionSubmit> selectList(
            @RequestParam("userId") Long userId,
            @RequestParam("competitionId") Long competitionId
    );

    @GetMapping("/selectList2")
    List<QuestionSubmit> selectList2(
            @RequestParam("userId") Long userId,
            @RequestParam("competitionId") Long competitionId,
            @RequestParam("questionId") Long questionId
    );

    @PostMapping("/updateById") // 使用POST更符合RESTful规范
    void updateById(
            @RequestParam("id") Long id,
            @RequestParam("competitionId") Long competitionId
    );
    @GetMapping("/getCount")
    Long getCount();
}