package space.kuikuioj.kuikuibankendquestion.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import space.kuikuioj.kuikuiojbankendmodel.dto.TagRequest;
import space.kuikuioj.kuikuiojbankendmodel.entity.Tag;

import java.util.List;
import java.util.Map;

/**
 * @author kuikui
 * @date 2025/4/25 16:50
 */

public interface TagService {
    void addTag(Tag tag);
    Page<Tag> list(TagRequest tagRequest);

    int del(Long id);
    
    /**
     * 获取热门标签及其使用次数
     * @param limit 返回的标签数量限制，默认为10
     * @return 标签名称和使用次数的映射列表
     */
    List<Map<String, Object>> getPopularTags(Integer limit);
}
