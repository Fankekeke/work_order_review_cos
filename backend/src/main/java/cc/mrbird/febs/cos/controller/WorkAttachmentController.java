package cc.mrbird.febs.cos.controller;


import cc.mrbird.febs.common.utils.R;
import cc.mrbird.febs.cos.entity.WorkAttachment;
import cc.mrbird.febs.cos.service.IWorkAttachmentService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author FanK
 */
@RestController
@RequestMapping("/cos/work-attachment")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class WorkAttachmentController {

    private final IWorkAttachmentService workAttachmentService;

    /**
     * 分页获取票单附件信息
     *
     * @param page           分页对象
     * @param workAttachment 票单附件信息
     * @return 结果
     */
    @GetMapping("/page")
    public R page(Page<WorkAttachment> page, WorkAttachment workAttachment) {
        return R.ok();
    }

    /**
     * 获取ID获取票单附件详情
     *
     * @param id 主键
     * @return 结果
     */
    @GetMapping("/{id}")
    public R detail(@PathVariable("id") Integer id) {
        return R.ok(workAttachmentService.getById(id));
    }

    /**
     * 获取票单附件信息列表
     *
     * @return 结果
     */
    @GetMapping("/list")
    public R list() {
        return R.ok(workAttachmentService.list());
    }

    /**
     * 新增票单附件信息
     *
     * @param workAttachment 票单附件信息
     * @return 结果
     */
    @PostMapping
    public R save(WorkAttachment workAttachment) {
        return R.ok(workAttachmentService.save(workAttachment));
    }

    /**
     * 修改票单附件信息
     *
     * @param workAttachment 票单附件信息
     * @return 结果
     */
    @PutMapping
    public R edit(WorkAttachment workAttachment) {
        return R.ok(workAttachmentService.updateById(workAttachment));
    }

    /**
     * 删除票单附件信息
     *
     * @param ids ids
     * @return 票单附件信息
     */
    @DeleteMapping("/{ids}")
    public R deleteByIds(@PathVariable("ids") List<Integer> ids) {
        return R.ok(workAttachmentService.removeByIds(ids));
    }
}
