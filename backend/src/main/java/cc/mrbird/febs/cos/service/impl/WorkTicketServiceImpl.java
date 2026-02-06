package cc.mrbird.febs.cos.service.impl;

import cc.mrbird.febs.cos.entity.WorkTicket;
import cc.mrbird.febs.cos.dao.WorkTicketMapper;
import cc.mrbird.febs.cos.service.IWorkTicketService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;

/**
 * @author FanK
 */
@Service
public class WorkTicketServiceImpl extends ServiceImpl<WorkTicketMapper, WorkTicket> implements IWorkTicketService {

    /**
     * 分页获取作业票信息
     *
     * @param page        分页对象
     * @param workTicket 作业票信息
     * @return 结果
     */
    @Override
    public IPage<LinkedHashMap<String, Object>> queryPage(Page<WorkTicket> page, WorkTicket workTicket) {
        return baseMapper.queryPage(page, workTicket);
    }
}
