package cc.mrbird.febs.cos.service.impl;

import cc.mrbird.febs.cos.entity.SafetyMeasures;
import cc.mrbird.febs.cos.dao.SafetyMeasuresMapper;
import cc.mrbird.febs.cos.service.ISafetyMeasuresService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;

/**
 * @author FanK
 */
@Service
public class SafetyMeasuresServiceImpl extends ServiceImpl<SafetyMeasuresMapper, SafetyMeasures> implements ISafetyMeasuresService {

    /**
     * 分页获取安全措施复核信息
     *
     * @param page           分页对象
     * @param safetyMeasures 安全措施复核信息
     * @return 结果
     */
    @Override
    public IPage<LinkedHashMap<String, Object>> queryPage(Page<SafetyMeasures> page, SafetyMeasures safetyMeasures) {
        return baseMapper.queryPage(page, safetyMeasures);
    }
}
