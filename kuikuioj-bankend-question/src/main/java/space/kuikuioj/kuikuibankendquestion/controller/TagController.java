package space.kuikuioj.kuikuibankendquestion.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;
import space.kuikuioj.kuikuibankendquestion.service.TagService;
import space.kuikuioj.kuikuiojbankendcommon.back.BaseResponse;
import space.kuikuioj.kuikuiojbankendcommon.back.ResultUtils;
import space.kuikuioj.kuikuiojbankendmodel.dto.TagRequest;
import space.kuikuioj.kuikuiojbankendmodel.entity.Tag;


import java.util.Date;

/**
 * @author kuikui
 * @date 2025/4/25 16:48
 */
@RestController
@RequestMapping("/tag")
public class TagController {
    @Resource
    private TagService tagService;

    @PostMapping("/tag")
    public BaseResponse<String> tag(@RequestBody Tag tag) {
        tag.setCreateTime(new Date());
        tag.setUpdateTime(new Date());
        tagService.addTag(tag);
        return ResultUtils.success("添加标签成功","");
    }
    @PostMapping("/list")
    public BaseResponse<Page<Tag>> list(@RequestBody TagRequest tagRequest) {
        Page<Tag> pageTag= tagService.list(tagRequest);
        return ResultUtils.success("查询标签列表成功",pageTag);
    }
    @DeleteMapping("/del/{id}")
    public BaseResponse<Integer> del(@PathVariable Long id) {
        int count = tagService.del(id);
        return ResultUtils.success("删除成功",count);
    }
}
