package cc.mrbird.febs.cos.controller;


import cc.mrbird.febs.common.utils.R;
import cc.mrbird.febs.cos.entity.WorkMeasure;
import cc.mrbird.febs.cos.service.IWorkMeasureService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author FanK
 */
@RestController
@RequestMapping("/cos/work-measure")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class WorkMeasureController {

    private final IWorkMeasureService workMeasureService;

    /**
     * 分页获取安全措施确认信息
     *
     * @param page        分页对象
     * @param workMeasure 安全措施确认信息
     * @return 结果
     */
    @GetMapping("/page")
    public R page(Page<WorkMeasure> page, WorkMeasure workMeasure) {
        return R.ok();
    }

    /**
     * 获取ID获取安全措施确认详情
     *
     * @param id 主键
     * @return 结果
     */
    @GetMapping("/{id}")
    public R detail(@PathVariable("id") Integer id) {
        return R.ok(workMeasureService.getById(id));
    }

    /**
     * 获取安全措施确认信息列表
     *
     * @return 结果
     */
    @GetMapping("/list")
    public R list() {
        return R.ok(workMeasureService.list());
    }

    /**
     * 新增安全措施确认信息
     *
     * @param workMeasure 安全措施确认信息
     * @return 结果
     */
    @PostMapping
    public R save(WorkMeasure workMeasure) {
        return R.ok(workMeasureService.save(workMeasure));
    }

    /**
     * 修改安全措施确认信息
     *
     * @param workMeasure 安全措施确认信息
     * @return 结果
     */
    @PutMapping
    public R edit(WorkMeasure workMeasure) {
        return R.ok(workMeasureService.updateById(workMeasure));
    }

    /**
     * 删除安全措施确认信息
     *
     * @param ids ids
     * @return 安全措施确认信息
     */
    @DeleteMapping("/{ids}")
    public R deleteByIds(@PathVariable("ids") List<Integer> ids) {
        return R.ok(workMeasureService.removeByIds(ids));
    }
}
