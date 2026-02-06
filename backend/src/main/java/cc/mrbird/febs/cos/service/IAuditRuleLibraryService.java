package cc.mrbird.febs.cos.service;

import cc.mrbird.febs.cos.entity.AuditRuleLibrary;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.apache.ibatis.annotations.Param;

import java.util.LinkedHashMap;

/**
 * @author FanK
 */
public interface IAuditRuleLibraryService extends IService<AuditRuleLibrary> {

    /**
     * 分页获取审核规则信息
     *
     * @param page             分页对象
     * @param auditRuleLibrary 审核规则信息
     * @return 结果
     */
    IPage<LinkedHashMap<String, Object>> queryPage(Page<AuditRuleLibrary> page, AuditRuleLibrary auditRuleLibrary);
}
