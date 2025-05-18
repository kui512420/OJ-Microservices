package space.kuikuioj.kuikuibankenduser.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import space.kuikuioj.kuikuibankenduser.service.LoginLogService;
import space.kuikuioj.kuikuiojbankendcommon.back.BaseResponse;
import space.kuikuioj.kuikuiojbankendcommon.back.ErrorCode;
import space.kuikuioj.kuikuiojbankendcommon.back.ResultUtils;
import space.kuikuioj.kuikuiojbankendcommon.exception.BusinessException;
import space.kuikuioj.kuikuiojbankendmodel.dto.LoginLogRequest;
import space.kuikuioj.kuikuiojbankendmodel.entity.LoginLog;

/**
 * @author kuikui
 * @date 2025/4/27 16:46
 */
@RestController
@RequestMapping("/loginLog")
public class LoginLogController {

    @Resource
    private LoginLogService loginLogService;

    /**
     * 查询登录日志
     * @param request 查询参数
     * @return 分页结果
     */
    @PostMapping("/list")
    public BaseResponse<Page<LoginLog>> getLoginLogList(@RequestBody LoginLogRequest request) {
        if (request == null) {
            throw new BusinessException(ErrorCode.PARMS_ERROR,"参数异常");
        }

        Page<LoginLog> loginLogPage = loginLogService.queryLoginLogs(
                request.getUserAccount(),
                request.getIp(),
                request.getStartTime(),
                request.getEndTime(),
                request.getCurrent(),
                request.getPageSize()
        );

        return ResultUtils.success("获取日志成功",loginLogPage);
    }
}
