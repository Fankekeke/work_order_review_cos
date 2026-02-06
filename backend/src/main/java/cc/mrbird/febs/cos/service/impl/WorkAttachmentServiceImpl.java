package cc.mrbird.febs.cos.service.impl;

import cc.mrbird.febs.cos.entity.WorkAttachment;
import cc.mrbird.febs.cos.dao.WorkAttachmentMapper;
import cc.mrbird.febs.cos.service.IWorkAttachmentService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;

/**
 * @author FanK
 */
@Service
public class WorkAttachmentServiceImpl extends ServiceImpl<WorkAttachmentMapper, WorkAttachment> implements IWorkAttachmentService {

    /**
     * 分页获取票单附件信息
     *
     * @param page           分页对象
     * @param workAttachment 票单附件信息
     * @return 结果
     */
    @Override
    public IPage<LinkedHashMap<String, Object>> queryPage(Page<WorkAttachment> page, WorkAttachment workAttachment) {
        return baseMapper.queryPage(page, workAttachment);
    }
}
