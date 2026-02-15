package cc.mrbird.febs.cos.controller;


import cc.mrbird.febs.common.exception.FebsException;
import cc.mrbird.febs.common.utils.R;
import cc.mrbird.febs.cos.entity.WfNodeAssignees;
import cc.mrbird.febs.cos.entity.WfProcessConfig;
import cc.mrbird.febs.cos.service.IWfNodeAssigneesService;
import cc.mrbird.febs.cos.service.IWfProcessConfigService;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author FanK
 */
@RestController
@RequestMapping("/cos/wf-process-config")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class WfProcessConfigController {

    private final IWfProcessConfigService wfProcessConfigService;

    private final IWfNodeAssigneesService wfNodeAssigneesService;

    /**
     * 分页获取审批流配置信息
     *
     * @param page            分页对象
     * @param wfProcessConfig 审批流配置信息
     * @return 结果
     */
    @GetMapping("/page")
    public R page(Page<WfProcessConfig> page, WfProcessConfig wfProcessConfig) {
        return R.ok(wfProcessConfigService.queryPage(page, wfProcessConfig));
    }

    /**
     * 获取ID获取审批流配置详情
     *
     * @param id 主键
     * @return 结果
     */
    @GetMapping("/{id}")
    public R detail(@PathVariable("id") Integer id) {
        return R.ok(wfProcessConfigService.getById(id));
    }

    /**
     * 获取审批流配置信息列表
     *
     * @return 结果
     */
    @GetMapping("/list")
    public R list() {
        return R.ok(wfProcessConfigService.list());
    }

    /**
     * 新增审批流配置信息
     *
     * @param wfProcessConfig 审批流配置信息
     * @return 结果
     */
    @PostMapping
    public R save(WfProcessConfig wfProcessConfig) throws FebsException {
        // 判断是否已经存在此作业类型
        long count = wfProcessConfigService.count(Wrappers.<WfProcessConfig>lambdaQuery().eq(WfProcessConfig::getWorkType, wfProcessConfig.getWorkType()));
        if (count > 0) {
            throw new FebsException("此作业类型已存在");
        }
        List<WfNodeAssignees> nodeAssignees = new ArrayList<>();
        wfProcessConfig.setCreateDate(DateUtil.formatDateTime(new Date()));
        wfProcessConfigService.save(wfProcessConfig);

        WfNodeAssignees nodeAssignees1 = new WfNodeAssignees();
        nodeAssignees1.setProcessId(Long.valueOf(wfProcessConfig.getId()));
        nodeAssignees1.setRoleKey("初审");
        nodeAssignees1.setNodeOrder(1);
        nodeAssignees1.setUserId(Long.valueOf(wfProcessConfig.getReviewer1()));

        WfNodeAssignees nodeAssignees2 = new WfNodeAssignees();
        nodeAssignees2.setProcessId(Long.valueOf(wfProcessConfig.getId()));
        nodeAssignees2.setRoleKey("复审");
        nodeAssignees2.setNodeOrder(2);
        nodeAssignees2.setUserId(Long.valueOf(wfProcessConfig.getReviewer2()));

        WfNodeAssignees nodeAssignees3 = new WfNodeAssignees();
        nodeAssignees3.setProcessId(Long.valueOf(wfProcessConfig.getId()));
        nodeAssignees3.setRoleKey("终审");
        nodeAssignees3.setNodeOrder(3);
        nodeAssignees3.setUserId(Long.valueOf(wfProcessConfig.getReviewer3()));

        nodeAssignees.add(nodeAssignees1);
        nodeAssignees.add(nodeAssignees2);
        nodeAssignees.add(nodeAssignees3);
        return R.ok(wfNodeAssigneesService.saveBatch(nodeAssignees));
    }

    /**
     * 修改审批流配置信息
     *
     * @param wfProcessConfig 审批流配置信息
     * @return 结果
     */
    @PutMapping
    public R edit(WfProcessConfig wfProcessConfig) throws FebsException {
        // 判断是否已经存在此作业类型
        long count = wfProcessConfigService.count(Wrappers.<WfProcessConfig>lambdaQuery().eq(WfProcessConfig::getWorkType, wfProcessConfig.getWorkType()).ne(WfProcessConfig::getId, wfProcessConfig.getId()));
        if (count > 0) {
            throw new FebsException("此作业类型已存在");
        }
        wfNodeAssigneesService.remove(Wrappers.<WfNodeAssignees>lambdaQuery().eq(WfNodeAssignees::getProcessId, wfProcessConfig.getId()));
        List<WfNodeAssignees> nodeAssignees = new ArrayList<>();

        WfNodeAssignees nodeAssignees1 = new WfNodeAssignees();
        nodeAssignees1.setProcessId(Long.valueOf(wfProcessConfig.getId()));
        nodeAssignees1.setRoleKey("初审");
        nodeAssignees1.setNodeOrder(1);
        nodeAssignees1.setUserId(Long.valueOf(wfProcessConfig.getReviewer1()));

        WfNodeAssignees nodeAssignees2 = new WfNodeAssignees();
        nodeAssignees2.setProcessId(Long.valueOf(wfProcessConfig.getId()));
        nodeAssignees2.setRoleKey("复审");
        nodeAssignees2.setNodeOrder(2);
        nodeAssignees2.setUserId(Long.valueOf(wfProcessConfig.getReviewer2()));

        WfNodeAssignees nodeAssignees3 = new WfNodeAssignees();
        nodeAssignees3.setProcessId(Long.valueOf(wfProcessConfig.getId()));
        nodeAssignees3.setRoleKey("终审");
        nodeAssignees3.setNodeOrder(3);
        nodeAssignees3.setUserId(Long.valueOf(wfProcessConfig.getReviewer3()));

        nodeAssignees.add(nodeAssignees1);
        nodeAssignees.add(nodeAssignees2);
        nodeAssignees.add(nodeAssignees3);
        wfProcessConfigService.updateById(wfProcessConfig);
        return R.ok(wfNodeAssigneesService.saveBatch(nodeAssignees));
    }

    /**
     * 删除审批流配置信息
     *
     * @param ids ids
     * @return 审批流配置信息
     */
    @DeleteMapping("/{ids}")
    public R deleteByIds(@PathVariable("ids") List<Integer> ids) {
        return R.ok(wfProcessConfigService.removeByIds(ids));
    }
}
