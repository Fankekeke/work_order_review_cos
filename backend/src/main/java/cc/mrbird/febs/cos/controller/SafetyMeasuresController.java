package cc.mrbird.febs.cos.controller;


import cc.mrbird.febs.common.utils.R;
import cc.mrbird.febs.cos.entity.SafetyMeasures;
import cc.mrbird.febs.cos.service.ISafetyMeasuresService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author FanK
 */
@RestController
@RequestMapping("/cos/safety-measures")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class SafetyMeasuresController {

    private final ISafetyMeasuresService safetyMeasuresService;

    /**
     * 分页获取安全措施复核信息
     *
     * @param page           分页对象
     * @param safetyMeasures 安全措施复核信息
     * @return 结果
     */
    @GetMapping("/page")
    public R page(Page<SafetyMeasures> page, SafetyMeasures safetyMeasures) {
        return R.ok(safetyMeasuresService.queryPage(page, safetyMeasures));
    }

    /**
     * 获取ID获取安全措施复核详情
     *
     * @param id 主键
     * @return 结果
     */
    @GetMapping("/{id}")
    public R detail(@PathVariable("id") Integer id) {
        return R.ok(safetyMeasuresService.getById(id));
    }

    /**
     * 获取安全措施复核信息列表
     *
     * @return 结果
     */
    @GetMapping("/list")
    public R list() {
        return R.ok(safetyMeasuresService.list());
    }

    /**
     * 新增安全措施复核信息
     *
     * @param safetyMeasures 安全措施复核信息
     * @return 结果
     */
    @PostMapping
    public R save(SafetyMeasures safetyMeasures) {
        return R.ok(safetyMeasuresService.save(safetyMeasures));
    }

    /**
     * 修改安全措施复核信息
     *
     * @param safetyMeasures 安全措施复核信息
     * @return 结果
     */
    @PutMapping
    public R edit(SafetyMeasures safetyMeasures) {
        return R.ok(safetyMeasuresService.updateById(safetyMeasures));
    }

    /**
     * 删除安全措施复核信息
     *
     * @param ids ids
     * @return 安全措施复核信息
     */
    @DeleteMapping("/{ids}")
    public R deleteByIds(@PathVariable("ids") List<Integer> ids) {
        return R.ok(safetyMeasuresService.removeByIds(ids));
    }
}
