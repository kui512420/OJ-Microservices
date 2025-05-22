package space.kuikuioj.kuikuibankendquestion.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import space.kuikuioj.kuikuibankendquestion.mapper.QuestionMapper;
import space.kuikuioj.kuikuibankendquestion.mapper.TagMapper;
import space.kuikuioj.kuikuibankendquestion.service.TagService;
import space.kuikuioj.kuikuiojbankendcommon.back.ErrorCode;
import space.kuikuioj.kuikuiojbankendcommon.exception.BusinessException;
import space.kuikuioj.kuikuiojbankendmodel.dto.TagRequest;
import space.kuikuioj.kuikuiojbankendmodel.entity.Question;
import space.kuikuioj.kuikuiojbankendmodel.entity.Tag;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author kuikui
 * @date 2025/4/25 16:50
 */
@Service
public class TagServiceImpl implements TagService {

    @Resource
    private TagMapper tagMapper;
    
    @Resource
    private QuestionMapper questionMapper;
    
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void addTag(Tag tag) {
        try {
            tagMapper.insert(tag);
        }catch (Exception e) {
            throw  new BusinessException(ErrorCode.PARMS_ERROR,"标签已经存在！");
        }
    }

    @Override
    public Page<Tag> list(TagRequest tagRequest) {
        Integer page = tagRequest.getPage();
        Integer count = tagRequest.getCount();
        Integer type = tagRequest.getType();

        // 为分页参数设置默认值
        if (page == null || page < 1) {
            page = 1; // 默认第一页
        }
        if (count == null || count < 1) {
            count = 10; // 默认每页10条
        }

        // 为 type 设置默认值或进行错误处理
        if (type == null) {
            type = 0; // 假设 type 为 null 时，默认为 0 (查询所有)
        }

        QueryWrapper<Tag> queryWrapper = new QueryWrapper<>();
        Page<Tag> pageOj = new Page<>(page, count);

        switch (type) {
            case 0:
                // 查询所有，不添加额外条件
                break;
            case 1:
                if (tagRequest.getTag() == null || tagRequest.getTag().isEmpty()) {
                    throw new BusinessException(ErrorCode.PARMS_ERROR, "当type为1时，参数'tag'不能为空");
                }
                queryWrapper.eq("name", tagRequest.getTag());
                break;
            default:
                throw new BusinessException(ErrorCode.PARMS_ERROR, "无效的参数'type': " + type);
        }
        return tagMapper.selectPage(pageOj, queryWrapper);
    }

    @Override
    public int del(Long id) {
        return tagMapper.deleteById(id);
    }
    
    @Override
    public List<Map<String, Object>> getPopularTags(Integer limit) {
        if (limit == null || limit <= 0) {
            limit = 10; // 默认返回前10个热门标签
        }
        
        // 获取所有问题
        List<Question> questions = questionMapper.selectAllQuestion();
        
        // 用于统计标签出现次数的Map
        Map<String, Integer> tagCountMap = new HashMap<>();
        
        // 遍历所有问题，提取标签并统计
        for (Question question : questions) {
            String tagsJson = question.getTags();
            if (tagsJson != null && !tagsJson.isEmpty()) {
                try {
                    // 解析JSON标签数组
                    List<String> tagList = objectMapper.readValue(tagsJson, new TypeReference<List<String>>() {});
                    
                    // 统计每个标签的出现次数
                    for (String tag : tagList) {
                        tagCountMap.put(tag, tagCountMap.getOrDefault(tag, 0) + 1);
                    }
                } catch (JsonProcessingException e) {
                    // 日志记录解析错误，但继续处理其他问题
                    System.err.println("Error parsing tags JSON for question ID " + question.getId() + ": " + e.getMessage());
                }
            }
        }
        
        // 将标签和计数转换为结果格式，并按计数降序排序
        return tagCountMap.entrySet().stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                .limit(limit)
                .map(entry -> {
                    Map<String, Object> tagInfo = new HashMap<>();
                    tagInfo.put("name", entry.getKey());
                    tagInfo.put("value", entry.getValue());
                    return tagInfo;
                })
                .collect(Collectors.toList());
    }
}
