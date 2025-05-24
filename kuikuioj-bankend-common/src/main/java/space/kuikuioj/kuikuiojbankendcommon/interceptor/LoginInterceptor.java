package space.kuikuioj.kuikuiojbankendcommon.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import space.kuikuioj.kuikuiojbankendcommon.back.ErrorCode;
import space.kuikuioj.kuikuiojbankendcommon.exception.BusinessException;
import space.kuikuioj.kuikuiojbankendcommon.utils.JwtLoginUtils;
import space.kuikuioj.kuikuiojbankendcommon.utils.RedisSetTokenExample;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Component
public class LoginInterceptor implements HandlerInterceptor {

    private static final Logger logger = LoggerFactory.getLogger(LoginInterceptor.class);

    private final RedisSetTokenExample redisSetTokenExample;
    private final JwtLoginUtils jwtLoginUtils;

    @Autowired
    public LoginInterceptor(RedisSetTokenExample redisSetTokenExample, JwtLoginUtils jwtLoginUtils) {
        this.redisSetTokenExample = redisSetTokenExample;
        this.jwtLoginUtils = jwtLoginUtils;
    }

    // 定义不需要拦截的路径
    private static final List<String> EXCLUDED_PATHS = Arrays.asList(
            "/api/user/login", "/api/user/register", "/api/user/captcha", "/api/user/email","/api/user/file/userheader"
            ,"/api/user/rank","/api/submit","/api/question/inner"
    );

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String requestURI = request.getRequestURI();
        System.out.println(requestURI);
        // 检查是否是不需要拦截的路径
        for (String path : EXCLUDED_PATHS) {
            if (requestURI.startsWith(path)) {
                return true;
            }
        }

        // 从请求头中获取 Token
        String token = request.getHeader("Accesstoken");
        if (token == null || token.isEmpty()) {
            // 如果 Token 为空，返回 401 未授权
            logger.error("请求路径 {}，Token 为空，抛出异常：请携带Accesstoken请求", requestURI);
            throw new BusinessException(ErrorCode.NOT_LOGIN_ERROR, "请携带Accesstoken请求");
        }

        // 从请求中获取用户 ID，这里假设请求参数中有 userId
        Map<Object, Object> jwtResult = jwtLoginUtils.jwtPeAccess(token);
        if (jwtResult == null ||!jwtResult.containsKey("id")) {
            logger.error("请求路径 {}，Token 解析结果为空或不包含用户 ID，抛出异常：Token 无效", requestURI);
            throw new BusinessException(ErrorCode.NOT_LOGIN_ERROR, "Token 无效");
        }
        String userId = jwtResult.get("id").toString();

        // 从 Redis 中获取保存的 Token
        boolean isExist = redisSetTokenExample.isTokenExistsInSet(userId, token);
        if (!isExist) {
            logger.error("请求路径 {}，Token 在 Redis 中不存在，抛出异常：账号已过期，请重新登录", requestURI);
            throw new BusinessException(ErrorCode.FORBINDDEN_ERROR, "账号已过期，请重新登录");
        }

        // 将 userId 和 userRole 存入 request attributes
        Object userRoleObj = jwtResult.get("userRole");
        String userRole = (userRoleObj != null) ? userRoleObj.toString() : "user"; // 默认为 "user"，或根据实际情况处理
        request.setAttribute("currentUserId", userId);
        request.setAttribute("currentUserRole", userRole);

        // Token 验证通过，继续处理请求
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        HandlerInterceptor.super.afterCompletion(request, response, handler, ex);
    }
}