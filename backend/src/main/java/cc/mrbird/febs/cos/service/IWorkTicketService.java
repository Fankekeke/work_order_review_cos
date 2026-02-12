package cc.mrbird.febs.cos.service;

import cc.mrbird.febs.common.exception.FebsException;
import cc.mrbird.febs.cos.entity.WorkApprovalLog;
import cc.mrbird.febs.cos.entity.WorkTicket;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.apache.ibatis.annotations.Param;

import java.util.LinkedHashMap;
import java.util.List;

/**
 * @author FanK
 */
public interface IWorkTicketService extends IService<WorkTicket> {

    /**
     * 分页获取作业票信息
     *
     * @param page        分页对象
     * @param workTicket 作业票信息
     * @return 结果
     */
    IPage<LinkedHashMap<String, Object>> queryPage(Page<WorkTicket> page, WorkTicket workTicket);

    /**
     * 作业票审核
     *
     * @param workApprovalLog 作业票信息
     * @return 结果
     */
    boolean audit(WorkApprovalLog workApprovalLog);

    /**
     * 获取作业票信息列表
     *
     * @return 列表
     */
    List<LinkedHashMap<String, Object>> queryTicketDetail(Integer id);

    /**
     * 高频风险作业票
     *
     * @return 结果
     */
    LinkedHashMap<String, Object> queryHighFrequencyRisk();

    /**
     * 新增作业票信息
     *
     * @param workTicket 作业票信息
     * @return 结果
     */
    boolean addWorkTicket(WorkTicket workTicket) throws FebsException;
}
