package cc.mrbird.febs.cos.service.impl;

import cc.mrbird.febs.cos.entity.AuditRuleLibrary;
import cc.mrbird.febs.cos.dao.AuditRuleLibraryMapper;
import cc.mrbird.febs.cos.service.IAuditRuleLibraryService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;

/**
 * @author FanK
 */
@Service
public class AuditRuleLibraryServiceImpl extends ServiceImpl<AuditRuleLibraryMapper, AuditRuleLibrary> implements IAuditRuleLibraryService {

    /**
     * 分页获取审核规则信息
     *
     * @param page             分页对象
     * @param auditRuleLibrary 审核规则信息
     * @return 结果
     */
    @Override
    public IPage<LinkedHashMap<String, Object>> queryPage(Page<AuditRuleLibrary> page, AuditRuleLibrary auditRuleLibrary) {
        return baseMapper.queryPage(page, auditRuleLibrary);
    }
}
