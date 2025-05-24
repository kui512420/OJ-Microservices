package space.kuikuioj.kuikuiojbankendquestionsubmit.rabbitmq;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.Channel;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;
import space.kuikuioj.kuikuiojbankendcodesandbox.CodeSandBox;
import space.kuikuioj.kuikuiojbankendcodesandbox.CodeSandBoxFactory;
import space.kuikuioj.kuikuiojbankendmodel.dto.ExecuteCodeRequest;
import space.kuikuioj.kuikuiojbankendmodel.dto.ExecuteCodeResponse;
import space.kuikuioj.kuikuiojbankendmodel.entity.JudgeInfo;
import space.kuikuioj.kuikuiojbankendmodel.entity.Question;
import space.kuikuioj.kuikuiojbankendmodel.entity.QuestionSubmit;
import space.kuikuioj.kuikuiojbankendmodel.entity.UserRank;
import space.kuikuioj.kuikuiojbankendquestionsubmit.mapper.QuestionSubmitMapper;
import space.kuikuioj.kuikuiojbankendserviceclient.QuestionFeignClient;
import space.kuikuioj.kuikuiojbankendserviceclient.UserFeignClient;

import java.io.IOException;
import java.util.List;

/**
 * 队列的消费者
 * @author kuikui
 * @date 2025/4/17 14:36
 */
@Service
@Slf4j
public class RabbitMQConsumer {
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Resource
    private QuestionSubmitMapper questionSubmitMapper;
    @Resource
    private UserFeignClient userFeignClient;
    @Resource
    private QuestionFeignClient questionFeignClient;
    /**
     * 监听队列消息
     * @param message
     */
    @RabbitListener(queues = {"code_queue"},ackMode = "MANUAL")
    public void consume(Message  message, Channel channel) {
        String msg = new String(message.getBody());
        long deliveryTag = message.getMessageProperties().getDeliveryTag();
        try {
            ExecuteCodeRequest executeCodeRequest = objectMapper.readValue(msg, ExecuteCodeRequest.class);
            // 使用远程代码沙箱
            CodeSandBox remoteSandBox = CodeSandBoxFactory.newInstance("remote");
            ExecuteCodeResponse response = remoteSandBox.executeCode( executeCodeRequest);

            List<JudgeInfo> judgeInfoList = response.getJudgeInfo();

            judgeInfoList.forEach(System.out::println);
            // 查询用户是否是首次通过

            Long questionId =  executeCodeRequest.getQuestion().getId();
            Long userId = executeCodeRequest.getUserId();
            Long submitId = executeCodeRequest.getQuestionSubmitId();
            QueryWrapper<QuestionSubmit> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("questionId",questionId)
                    .eq("userId",userId)
                                .eq("status", 2);
            boolean isFirst = questionSubmitMapper.selectCount(queryWrapper) == 0;
            if (response.getStatus() == 2) {
                // 更新执行的代码的状态
                if(isFirst){
                    // 更新执行的代码的通过数量
                    updateAcceptedCount(questionId);

                    userFeignClient.updateAcceptCountInner(userId);
                }
                updateQuestionSubmitStatus(submitId,2);

            }else{
                updateQuestionSubmitStatus(submitId,3);
            }
            updateJudgeInfo(submitId,judgeInfoList);
            channel.basicAck(deliveryTag, false);
        } catch (IOException e) {
            log.error("RabbitMQ Consumer: IOException while processing message: '{}', error: {}", msg, e.getMessage(), e);
            try {
                channel.basicNack(deliveryTag, false, false); // deliveryTag, multiple, requeue=false
            } catch (IOException nackException) {
                log.error("RabbitMQ Consumer: IOException while nacking message after previous IOException: {}", nackException.getMessage(), nackException);
            }
        } catch (InterruptedException e) {
            log.error("RabbitMQ Consumer: InterruptedException while processing message: '{}', error: {}", msg, e.getMessage(), e);
            try {
                channel.basicNack(deliveryTag, false, false); // deliveryTag, multiple, requeue=false
            } catch (IOException nackException) {
                log.error("RabbitMQ Consumer: IOException while nacking message after InterruptedException: {}", nackException.getMessage(), nackException);
            }
            // Consider whether to rethrow or not. Rethrowing might trigger Spring AMQP's default error handling.
            // For now, just logging and nacking. If Spring AMQP is configured with retries, this might still lead to retries.
            // To ensure no retries from Spring AMQP level for this specific exception if it's wrapped,
            // more complex error handling strategy might be needed.
            // Thread.currentThread().interrupt(); // Restore interrupt status if appropriate
            // throw new RuntimeException(e); // Original behavior
        } catch (Exception e) {
            // Catch any other unexpected exceptions
            log.error("RabbitMQ Consumer: Unexpected exception while processing message: '{}', error: {}", msg, e.getMessage(), e);
            try {
                channel.basicNack(deliveryTag, false, false); // deliveryTag, multiple, requeue=false
            } catch (IOException nackException) {
                log.error("RabbitMQ Consumer: IOException while nacking message after unexpected exception: {}", nackException.getMessage(), nackException);
            }
        }
    }



    /**
     * 更新提交状态
     * @param submitId
     */
    private void updateQuestionSubmitStatus(Long submitId,Integer status) {
        try {
            UpdateWrapper<QuestionSubmit> updateWrapper = new UpdateWrapper<>();
            updateWrapper.eq("id", submitId);
            QuestionSubmit updateQuestionSubmit = new QuestionSubmit();
            updateQuestionSubmit.setStatus(status);
            questionSubmitMapper.update(updateQuestionSubmit, updateWrapper);
        } catch (Exception e) {
        }
    }

    /**
     * 更新通过数量
     * @param questionId
     */
    private void updateAcceptedCount(Long questionId) {
        try {
            questionFeignClient.updateSubmitCountInner(questionId);
        } catch (Exception e) {
        }
    }


    /**
     * 更新判题信息
     * @param questionSummitId
     */
    private void updateJudgeInfo(Long questionSummitId, List<JudgeInfo> judgeInfoList) {
        try {
            UpdateWrapper<QuestionSubmit> updateWrapper = new UpdateWrapper<>();
            QuestionSubmit questionSubmit = new QuestionSubmit();
            questionSubmit.setJudgeInfo(judgeInfoList.toString());
            updateWrapper.eq("id", questionSummitId);
            questionSubmitMapper.update(questionSubmit, updateWrapper);
        } catch (Exception e) {
        }
    }
}