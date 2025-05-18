package space.kuikuioj.kuikuibankendquestion.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import space.kuikuioj.kuikuiojbankendmodel.dto.TagRequest;
import space.kuikuioj.kuikuiojbankendmodel.entity.Tag;


/**
 * @author kuikui
 * @date 2025/4/25 16:50
 */

public interface TagService {
    void addTag(Tag tag);
    Page<Tag> list(TagRequest tagRequest);

    int del(Long id);
}
