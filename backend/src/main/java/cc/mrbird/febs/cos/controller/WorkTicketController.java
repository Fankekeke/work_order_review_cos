package cc.mrbird.febs.cos.controller;


import cc.mrbird.febs.common.utils.R;
import cc.mrbird.febs.cos.entity.WorkTicket;
import cc.mrbird.febs.cos.service.IWorkTicketService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author FanK
 */
@RestController
@RequestMapping("/cos/work-ticket")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class WorkTicketController {

    private final IWorkTicketService workTicketService;

    /**
     * 分页获取作业票信息
     *
     * @param page        分页对象
     * @param workTicket 作业票信息
     * @return 结果
     */
    @GetMapping("/page")
    public R page(Page<WorkTicket> page, WorkTicket workTicket) {
        return R.ok();
    }

    /**
     * 获取ID获取作业票详情
     *
     * @param id 主键
     * @return 结果
     */
    @GetMapping("/{id}")
    public R detail(@PathVariable("id") Integer id) {
        return R.ok(workTicketService.getById(id));
    }

    /**
     * 获取作业票信息列表
     *
     * @return 结果
     */
    @GetMapping("/list")
    public R list() {
        return R.ok(workTicketService.list());
    }

    /**
     * 新增作业票信息
     *
     * @param workTicket 作业票信息
     * @return 结果
     */
    @PostMapping
    public R save(WorkTicket workTicket) {
        return R.ok(workTicketService.save(workTicket));
    }

    /**
     * 修改作业票信息
     *
     * @param workTicket 作业票信息
     * @return 结果
     */
    @PutMapping
    public R edit(WorkTicket workTicket) {
        return R.ok(workTicketService.updateById(workTicket));
    }

    /**
     * 删除作业票信息
     *
     * @param ids ids
     * @return 作业票信息
     */
    @DeleteMapping("/{ids}")
    public R deleteByIds(@PathVariable("ids") List<Integer> ids) {
        return R.ok(workTicketService.removeByIds(ids));
    }
}
