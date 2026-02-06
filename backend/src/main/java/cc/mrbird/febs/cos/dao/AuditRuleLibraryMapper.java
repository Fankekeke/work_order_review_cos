package cc.mrbird.febs.cos.dao;

import cc.mrbird.febs.cos.entity.AuditRuleLibrary;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.LinkedHashMap;

/**
 * @author FanK
 */
public interface AuditRuleLibraryMapper extends BaseMapper<AuditRuleLibrary> {

    /**
     * 分页获取审核规则信息
     *
     * @param page             分页对象
     * @param auditRuleLibrary 审核规则信息
     * @return 结果
     */
    IPage<LinkedHashMap<String, Object>> queryPage(Page<AuditRuleLibrary> page, @Param("auditRuleLibrary") AuditRuleLibrary auditRuleLibrary);
}
