package cc.mrbird.febs.cos.service.impl;

import cc.mrbird.febs.cos.entity.AuditUser;
import cc.mrbird.febs.cos.entity.WfProcessConfig;
import cc.mrbird.febs.cos.dao.WfProcessConfigMapper;
import cc.mrbird.febs.cos.service.IWfProcessConfigService;
import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author FanK
 */
@Service
public class WfProcessConfigServiceImpl extends ServiceImpl<WfProcessConfigMapper, WfProcessConfig> implements IWfProcessConfigService {

    /**
     * 分页获取审批流配置信息
     *
     * @param page            分页对象
     * @param wfProcessConfig 审批流配置信息
     * @return 结果
     */
    @Override
    public IPage<LinkedHashMap<String, Object>> queryPage(Page<WfProcessConfig> page, WfProcessConfig wfProcessConfig) {
        IPage<LinkedHashMap<String, Object>> result = baseMapper.queryPage(page, wfProcessConfig);
        List<LinkedHashMap<String, Object>> list = result.getRecords();
        if (CollectionUtil.isEmpty(list)) {
            return result;
        }

        List<Integer> processIds = list.stream().map(item -> Integer.parseInt(item.get("id").toString())).collect(Collectors.toList());
        List<AuditUser> processDetail = baseMapper.queryProcessDetail(processIds);
        Map<Integer, List<AuditUser>> processDetailMap = processDetail.stream().collect(Collectors.groupingBy(AuditUser::getProcessId));

        for (LinkedHashMap<String, Object> stringObjectLinkedHashMap : list) {
            Integer processId = Integer.parseInt(stringObjectLinkedHashMap.get("id").toString());
            List<AuditUser> auditUsers = processDetailMap.get(processId);
            stringObjectLinkedHashMap.put("auditUsers", auditUsers);

            if (CollectionUtil.isNotEmpty(auditUsers)) {
                for (AuditUser auditUser : auditUsers) {
                    if (auditUser.getNodeOrder() == 1) {
                        stringObjectLinkedHashMap.put("reviewer1", auditUser.getUserId());
                    } else if (auditUser.getNodeOrder() == 2) {
                        stringObjectLinkedHashMap.put("reviewer2", auditUser.getUserId());
                    } else if (auditUser.getNodeOrder() == 3) {
                        stringObjectLinkedHashMap.put("reviewer3", auditUser.getUserId());
                    }
                }
            }
        }
        return result;
    }
}
