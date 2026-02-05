package cc.mrbird.febs.cos.controller;


import cc.mrbird.febs.common.utils.R;
import cc.mrbird.febs.cos.entity.WorkApprovalLog;
import cc.mrbird.febs.cos.service.IWorkApprovalLogService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author FanK
 */
@RestController
@RequestMapping("/cos/work-approval-log")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class WorkApprovalLogController {

    private final IWorkApprovalLogService workApprovalLogService;

    /**
     * 分页获取审批流转记录信息
     *
     * @param page            分页对象
     * @param workApprovalLog 审批流转记录信息
     * @return 结果
     */
    @GetMapping("/page")
    public R page(Page<WorkApprovalLog> page, WorkApprovalLog workApprovalLog) {
        return R.ok();
    }

    /**
     * 获取ID获取审批流转记录详情
     *
     * @param id 主键
     * @return 结果
     */
    @GetMapping("/{id}")
    public R detail(@PathVariable("id") Integer id) {
        return R.ok(workApprovalLogService.getById(id));
    }

    /**
     * 获取审批流转记录信息列表
     *
     * @return 结果
     */
    @GetMapping("/list")
    public R list() {
        return R.ok(workApprovalLogService.list());
    }

    /**
     * 新增审批流转记录信息
     *
     * @param workApprovalLog 审批流转记录信息
     * @return 结果
     */
    @PostMapping
    public R save(WorkApprovalLog workApprovalLog) {
        return R.ok(workApprovalLogService.save(workApprovalLog));
    }

    /**
     * 修改审批流转记录信息
     *
     * @param workApprovalLog 审批流转记录信息
     * @return 结果
     */
    @PutMapping
    public R edit(WorkApprovalLog workApprovalLog) {
        return R.ok(workApprovalLogService.updateById(workApprovalLog));
    }

    /**
     * 删除审批流转记录信息
     *
     * @param ids ids
     * @return 审批流转记录信息
     */
    @DeleteMapping("/{ids}")
    public R deleteByIds(@PathVariable("ids") List<Integer> ids) {
        return R.ok(workApprovalLogService.removeByIds(ids));
    }
}
