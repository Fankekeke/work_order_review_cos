package cc.mrbird.febs.cos.controller;


import cc.mrbird.febs.common.utils.R;
import cc.mrbird.febs.cos.entity.AuditRuleLibrary;
import cc.mrbird.febs.cos.service.IAuditRuleLibraryService;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author FanK
 */
@RestController
@RequestMapping("/cos/audit-rule-library")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AuditRuleLibraryController {

    private final IAuditRuleLibraryService auditRuleLibraryService;

    /**
     * 分页获取审核规则信息
     *
     * @param page             分页对象
     * @param auditRuleLibrary 审核规则信息
     * @return 结果
     */
    @GetMapping("/page")
    public R page(Page<AuditRuleLibrary> page, AuditRuleLibrary auditRuleLibrary) {
        return R.ok(auditRuleLibraryService.queryPage(page, auditRuleLibrary));
    }

    /**
     * 获取审核规则信息列表
     *
     * @param keyword 关键词
     * @return 结果
     */
    @GetMapping("/queryLibraryKeyword")
    public R queryLibraryKeyword(String keyword) {
        List<AuditRuleLibrary> list = auditRuleLibraryService.list(Wrappers.<AuditRuleLibrary>lambdaQuery().eq(AuditRuleLibrary::getIsActive, 1));
        if (CollectionUtil.isEmpty(list)) {
            return R.ok(Collections.emptyList());
        }
        // 根据关键词筛选匹配的规则（假设关键词匹配规则名称或描述）
        List<AuditRuleLibrary> result = list.stream()
                .filter(rule -> rule.getRuleName().contains(keyword))
                .collect(Collectors.toList());
        // 返回结果
        return R.ok(result);
    }

    /**
     * 获取ID获取审核规则详情
     *
     * @param id 主键
     * @return 结果
     */
    @GetMapping("/{id}")
    public R detail(@PathVariable("id") Integer id) {
        return R.ok(auditRuleLibraryService.getById(id));
    }

    /**
     * 获取审核规则信息列表
     *
     * @return 结果
     */
    @GetMapping("/list")
    public R list() {
        return R.ok(auditRuleLibraryService.list());
    }

    /**
     * 新增审核规则信息
     *
     * @param auditRuleLibrary 审核规则信息
     * @return 结果
     */
    @PostMapping
    public R save(AuditRuleLibrary auditRuleLibrary) {
        return R.ok(auditRuleLibraryService.save(auditRuleLibrary));
    }

    /**
     * 修改审核规则信息
     *
     * @param auditRuleLibrary 审核规则信息
     * @return 结果
     */
    @PutMapping
    public R edit(AuditRuleLibrary auditRuleLibrary) {
        return R.ok(auditRuleLibraryService.updateById(auditRuleLibrary));
    }

    /**
     * 删除审核规则信息
     *
     * @param ids ids
     * @return 审核规则信息
     */
    @DeleteMapping("/{ids}")
    public R deleteByIds(@PathVariable("ids") List<Integer> ids) {
        return R.ok(auditRuleLibraryService.removeByIds(ids));
    }
}
