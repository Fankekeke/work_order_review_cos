package cc.mrbird.febs.cos.controller;


import cc.mrbird.febs.common.utils.R;
import cc.mrbird.febs.cos.entity.AuditRuleLibrary;
import cc.mrbird.febs.cos.service.IAuditRuleLibraryService;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

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
        return R.ok();
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
