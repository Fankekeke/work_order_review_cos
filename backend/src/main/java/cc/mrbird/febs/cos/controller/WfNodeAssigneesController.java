package cc.mrbird.febs.cos.controller;


import cc.mrbird.febs.common.utils.R;
import cc.mrbird.febs.cos.entity.WfNodeAssignees;
import cc.mrbird.febs.cos.service.IWfNodeAssigneesService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author FanK
 */
@RestController
@RequestMapping("/cos/wf-node-assignees")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class WfNodeAssigneesController {

    private final IWfNodeAssigneesService wfNodeAssigneesService;

    /**
     * 分页获取节点人员绑定信息
     *
     * @param page            分页对象
     * @param wfNodeAssignees 节点人员绑定信息
     * @return 结果
     */
    @GetMapping("/page")
    public R page(Page<WfNodeAssignees> page, WfNodeAssignees wfNodeAssignees) {
        return R.ok();
    }

    /**
     * 获取ID获取节点人员绑定详情
     *
     * @param id 主键
     * @return 结果
     */
    @GetMapping("/{id}")
    public R detail(@PathVariable("id") Integer id) {
        return R.ok(wfNodeAssigneesService.getById(id));
    }

    /**
     * 获取节点人员绑定信息列表
     *
     * @return 结果
     */
    @GetMapping("/list")
    public R list() {
        return R.ok(wfNodeAssigneesService.list());
    }

    /**
     * 新增节点人员绑定信息
     *
     * @param wfNodeAssignees 节点人员绑定信息
     * @return 结果
     */
    @PostMapping
    public R save(WfNodeAssignees wfNodeAssignees) {
        return R.ok(wfNodeAssigneesService.save(wfNodeAssignees));
    }

    /**
     * 修改节点人员绑定信息
     *
     * @param wfNodeAssignees 节点人员绑定信息
     * @return 结果
     */
    @PutMapping
    public R edit(WfNodeAssignees wfNodeAssignees) {
        return R.ok(wfNodeAssigneesService.updateById(wfNodeAssignees));
    }

    /**
     * 删除节点人员绑定信息
     *
     * @param ids ids
     * @return 节点人员绑定信息
     */
    @DeleteMapping("/{ids}")
    public R deleteByIds(@PathVariable("ids") List<Integer> ids) {
        return R.ok(wfNodeAssigneesService.removeByIds(ids));
    }
}
