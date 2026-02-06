package cc.mrbird.febs.cos.service.impl;

import cc.mrbird.febs.cos.entity.WfProcessConfig;
import cc.mrbird.febs.cos.dao.WfProcessConfigMapper;
import cc.mrbird.febs.cos.service.IWfProcessConfigService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;

/**
 * @author FanK
 */
@Service
public class WfProcessConfigServiceImpl extends ServiceImpl<WfProcessConfigMapper, WfProcessConfig> implements IWfProcessConfigService {

    /**
     * 分页获取审批流配置信息
     *
     * @param page            分页对象
     * @param wfProcessConfig 审批流配置信息
     * @return 结果
     */
    @Override
    public IPage<LinkedHashMap<String, Object>> queryPage(Page<WfProcessConfig> page, WfProcessConfig wfProcessConfig) {
        return baseMapper.queryPage(page, wfProcessConfig);
    }
}
