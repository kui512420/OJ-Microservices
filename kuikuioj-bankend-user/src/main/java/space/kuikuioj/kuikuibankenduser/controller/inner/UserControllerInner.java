package space.kuikuioj.kuikuibankenduser.controller.inner;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import space.kuikuioj.kuikuibankenduser.mapper.UserMapper;
import space.kuikuioj.kuikuibankenduser.mapper.UserRankMapper;
import space.kuikuioj.kuikuibankenduser.service.UserRankService;
import space.kuikuioj.kuikuibankenduser.service.UserService;
import space.kuikuioj.kuikuiojbankendcommon.back.BaseResponse;
import space.kuikuioj.kuikuiojbankendcommon.back.ErrorCode;
import space.kuikuioj.kuikuiojbankendcommon.back.ResultUtils;
import space.kuikuioj.kuikuiojbankendcommon.exception.BusinessException;
import space.kuikuioj.kuikuiojbankendcommon.utils.CaptchaUtil;
import space.kuikuioj.kuikuiojbankendcommon.utils.JwtLoginUtils;
import space.kuikuioj.kuikuiojbankendcommon.utils.RedisSetTokenExample;
import space.kuikuioj.kuikuiojbankendmodel.dto.*;
import space.kuikuioj.kuikuiojbankendmodel.entity.User;
import space.kuikuioj.kuikuiojbankendmodel.entity.UserRank;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author kuikui
 * @date 2025/3/15 16:14
 */
@RestController
@RequestMapping("/inner")
public class UserControllerInner {

    @Autowired
    private UserRankMapper userRankMapper;
    @Autowired
    private UserMapper userMapper;

    @PostMapping("/updateSubmit")
    public void updateSubmitCountInner(@RequestBody Long userId) {
        UpdateWrapper<UserRank> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("userId", userId);
        updateWrapper.setSql("submitCount = submitCount + 1");
        userRankMapper.update(UserRank.builder().build(),updateWrapper);
    }

    @PostMapping("/updateAccept")
    public void updateAcceptCountInner(@RequestBody Long userId) {
        UpdateWrapper<UserRank> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("userId", userId);
        updateWrapper.setSql("acceptCount = acceptCount + 1");
        userRankMapper.update(UserRank.builder().build(),updateWrapper);
    }
    @GetMapping("/selectById")
    public User selectByIdInner(@RequestParam("userId") Long userId) {
        return userMapper.selectById(userId);
    }
}
