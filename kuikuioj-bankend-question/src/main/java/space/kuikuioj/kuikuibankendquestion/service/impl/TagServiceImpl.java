package space.kuikuioj.kuikuibankendquestion.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import space.kuikuioj.kuikuibankendquestion.mapper.TagMapper;
import space.kuikuioj.kuikuibankendquestion.service.TagService;
import space.kuikuioj.kuikuiojbankendcommon.back.ErrorCode;
import space.kuikuioj.kuikuiojbankendcommon.exception.BusinessException;
import space.kuikuioj.kuikuiojbankendmodel.dto.TagRequest;
import space.kuikuioj.kuikuiojbankendmodel.entity.Tag;

/**
 * @author kuikui
 * @date 2025/4/25 16:50
 */
@Service
public class TagServiceImpl implements TagService {

    @Resource
    private TagMapper tagMapper;

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
}
