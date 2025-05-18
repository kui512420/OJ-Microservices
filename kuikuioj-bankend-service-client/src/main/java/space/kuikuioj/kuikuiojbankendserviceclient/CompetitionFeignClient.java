package space.kuikuioj.kuikuiojbankendserviceclient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import space.kuikuioj.kuikuiojbankendmodel.entity.CompetitionParticipant;

/**
 * @author kuikui
 * @date 2025/5/17 16:49
 * @description
 */
@FeignClient(name = "service-competition", path = "/api/competition/inner")
public interface CompetitionFeignClient {

    @PostMapping("/selectOne")
    CompetitionParticipant selectOne(@RequestParam("competitionId") Long competition_id, @RequestParam("userId") Long user_id);
}
