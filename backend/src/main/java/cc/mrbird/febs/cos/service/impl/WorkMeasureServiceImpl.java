package cc.mrbird.febs.cos.service.impl;

import cc.mrbird.febs.cos.entity.WorkMeasure;
import cc.mrbird.febs.cos.dao.WorkMeasureMapper;
import cc.mrbird.febs.cos.service.IWorkMeasureService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;

/**
 * @author FanK
 */
@Service
public class WorkMeasureServiceImpl extends ServiceImpl<WorkMeasureMapper, WorkMeasure> implements IWorkMeasureService {

    /**
     * 分页获取安全措施确认信息
     *
     * @param page        分页对象
     * @param workMeasure 安全措施确认信息
     * @return 结果
     */
    @Override
    public IPage<LinkedHashMap<String, Object>> queryPage(Page<WorkMeasure> page, WorkMeasure workMeasure) {
        return baseMapper.queryPage(page, workMeasure);
    }
}
