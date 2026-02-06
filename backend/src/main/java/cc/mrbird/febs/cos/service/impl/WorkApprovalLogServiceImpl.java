package cc.mrbird.febs.cos.service.impl;

import cc.mrbird.febs.cos.entity.WorkApprovalLog;
import cc.mrbird.febs.cos.dao.WorkApprovalLogMapper;
import cc.mrbird.febs.cos.service.IWorkApprovalLogService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;

/**
 * @author FanK
 */
@Service
public class WorkApprovalLogServiceImpl extends ServiceImpl<WorkApprovalLogMapper, WorkApprovalLog> implements IWorkApprovalLogService {

    /**
     * 分页获取审批流转记录信息
     *
     * @param page            分页对象
     * @param workApprovalLog 审批流转记录信息
     * @return 结果
     */
    @Override
    public IPage<LinkedHashMap<String, Object>> queryPage(Page<WorkApprovalLog> page, WorkApprovalLog workApprovalLog) {
        return baseMapper.queryPage(page, workApprovalLog);
    }
}
