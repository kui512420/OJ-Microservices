package space.kuikuioj.kuikuiojbankendserviceclient;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import space.kuikuioj.kuikuiojbankendmodel.entity.Question;
import space.kuikuioj.kuikuiojbankendmodel.entity.User;
import space.kuikuioj.kuikuiojbankendmodel.entity.UserRank;

/**
 * @author kuikui
 * @date 2025/5/17 13:04
 * @description
 */
@FeignClient(name = "service-user", path = "/api/user/inner")
public interface UserFeignClient {
    @PostMapping("/updateSubmit")
    void updateSubmitCountInner(Long id);
    @PostMapping("/updateAccept")
    void updateAcceptCountInner(Long id);
    @GetMapping("/selectById")
    User selectByIdInner(@RequestParam("userId") Long userId);
}
