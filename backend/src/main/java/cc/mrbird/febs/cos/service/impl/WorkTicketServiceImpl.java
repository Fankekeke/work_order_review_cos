package cc.mrbird.febs.cos.service.impl;

import cc.mrbird.febs.common.exception.FebsException;
import cc.mrbird.febs.cos.entity.*;
import cc.mrbird.febs.cos.dao.WorkTicketMapper;
import cc.mrbird.febs.cos.service.*;
import cn.hutool.core.date.DateUtil;
import com.alibaba.dashscope.aigc.generation.Generation;
import com.alibaba.dashscope.aigc.generation.GenerationParam;
import com.alibaba.dashscope.aigc.generation.GenerationResult;
import com.alibaba.dashscope.common.Message;
import com.alibaba.dashscope.common.Role;
import com.alibaba.dashscope.exception.InputRequiredException;
import com.alibaba.dashscope.exception.NoApiKeyException;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author FanK
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class WorkTicketServiceImpl extends ServiceImpl<WorkTicketMapper, WorkTicket> implements IWorkTicketService {

    private final IWfProcessConfigService wfProcessConfigService;

    private final IWfNodeAssigneesService wfNodeAssigneesService;

    private final IWorkApprovalLogService workApprovalLogService;

    private final IStaffInfoService staffInfoService;

    private final IWorkMeasureService workMeasureService;

    private final ISafetyMeasuresService safetyMeasuresService;

    @Resource
    private Generation generation;

    /**
     * 分页获取作业票信息
     *
     * @param page       分页对象
     * @param workTicket 作业票信息
     * @return 结果
     */
    @Override
    public IPage<LinkedHashMap<String, Object>> queryPage(Page<WorkTicket> page, WorkTicket workTicket) {
        return baseMapper.queryPage(page, workTicket);
    }

    /**
     * 作业票审核
     *
     * @param workApprovalLog 作业票信息
     * @return 结果
     */
    @Override
    public boolean audit(WorkApprovalLog workApprovalLog) {
        WorkTicket workTicket = this.getById(workApprovalLog.getTicketId());
        if (workApprovalLog.getAction() == 3) {
            workTicket.setStatus(4);
        } else if (workApprovalLog.getAction() == 2) {
            switch (workTicket.getStatus()) {
                case 1:
                    workTicket.setStatus(1);
                    break;
                case 2:
                    workTicket.setStatus(2);
                    break;
                case 3:
                    workTicket.setStatus(3);
                    break;
                default:
            }
        } else if (workApprovalLog.getAction() == 1) {
            switch (workTicket.getStatus()) {
                case 1:
                    workTicket.setStatus(2);
                    break;
                case 2:
                    workTicket.setStatus(3);
                    break;
                case 3:
                    workTicket.setStatus(5);
                    break;
                default:
            }
        }
        workTicket.setUpdateTime(DateUtil.formatDateTime(new Date()));
        this.updateById(workTicket);
        return workApprovalLogService.updateById(workApprovalLog);
    }

    /**
     * 获取作业票信息列表
     *
     * @return 列表
     */
    @Override
    public List<LinkedHashMap<String, Object>> queryTicketDetail(Integer id) {
        List<LinkedHashMap<String, Object>> list = new ArrayList<>();
        WorkTicket workTicket = this.getById(id);
        // 安全措施
        WorkMeasure workMeasure = workMeasureService.getById(workTicket.getId());
        // 措施确认
        SafetyMeasures safetyMeasures = safetyMeasuresService.getById(workTicket.getId());

        // 获取员工信息
        List<StaffInfo> staffInfoList = staffInfoService.getStaffInfoList();
        Map<Integer, StaffInfo> staffInfoMap = staffInfoList.stream().collect(Collectors.toMap(StaffInfo::getId, v -> v));
        // 获取审核记录
        List<WorkApprovalLog> workApprovalLogs = workApprovalLogService.list(Wrappers.<WorkApprovalLog>lambdaQuery().eq(WorkApprovalLog::getTicketId, workTicket.getId()));
        Map<Integer, WorkApprovalLog> workApprovalLogMap = workApprovalLogs.stream().collect(Collectors.toMap(WorkApprovalLog::getApprovalStep, v -> v));
        Map<Integer, String> actionMap = new HashMap<Integer, String>() {
            {
                put(-1, "未审核");
                put(1, "通过");
                put(2, "驳回");
                put(3, "中止");
            }
        };

        for (int i = 1; i <= 7; i++) {
            LinkedHashMap<String, Object> map = new LinkedHashMap<>();
            if (i == 1) {
                WorkApprovalLog workApprovalLog = workApprovalLogMap.get(1);
                map.put("staffName", staffInfoMap.get(workApprovalLog.getApproverId().intValue()).getName());
                map.put("deptName", staffInfoMap.get(workApprovalLog.getApproverId().intValue()).getDeptName());
                map.put("positionName", staffInfoMap.get(workApprovalLog.getApproverId().intValue()).getPositionName());
                map.put("step", "初审");
                map.put("action", actionMap.get(workApprovalLog.getAction()));
                map.put("comments", workApprovalLog.getComments());
                map.put("createDate", workApprovalLog.getCreateTime());
            } else if (i == 2) {
                WorkApprovalLog workApprovalLog = workApprovalLogMap.get(2);
                map.put("staffName", staffInfoMap.get(workApprovalLog.getApproverId().intValue()).getName());
                map.put("deptName", staffInfoMap.get(workApprovalLog.getApproverId().intValue()).getDeptName());
                map.put("positionName", staffInfoMap.get(workApprovalLog.getApproverId().intValue()).getPositionName());
                map.put("step", "初审");
                map.put("action", actionMap.get(workApprovalLog.getAction()));
                map.put("comments", workApprovalLog.getComments());
                map.put("createDate", workApprovalLog.getCreateTime());
            } else if (i == 3) {
                WorkApprovalLog workApprovalLog = workApprovalLogMap.get(3);
                map.put("staffName", staffInfoMap.get(workApprovalLog.getApproverId().intValue()).getName());
                map.put("deptName", staffInfoMap.get(workApprovalLog.getApproverId().intValue()).getDeptName());
                map.put("positionName", staffInfoMap.get(workApprovalLog.getApproverId().intValue()).getPositionName());
                map.put("step", "初审");
                map.put("action", actionMap.get(workApprovalLog.getAction()));
                map.put("comments", workApprovalLog.getComments());
                map.put("createDate", workApprovalLog.getCreateTime());
            } else if (i == 4) {
                map.put("staffName", staffInfoMap.get(workMeasure.getConfirmerId().intValue()).getName());
                map.put("deptName", staffInfoMap.get(workMeasure.getConfirmerId().intValue()).getDeptName());
                map.put("positionName", staffInfoMap.get(workMeasure.getConfirmerId().intValue()).getPositionName());
                map.put("step", "安全措施确认");
                map.put("action", workMeasure.getIsConfirmed() == 0 ? "未确认" : "已确认");
                map.put("comments", workMeasure.getMeasureContent());
                map.put("createDate", workMeasure.getConfirmTime());
            } else if (i == 5) {
                map.put("staffName", "管理员");
                map.put("deptName", "");
                map.put("positionName", "");
                map.put("step", "安全复查确认");
                map.put("action", safetyMeasures.getIsConfirmed() == 0 ? "未确认" : "已确认");
                map.put("comments", safetyMeasures.getMeasureContent());
                map.put("createDate", safetyMeasures.getConfirmTime());
            }
            list.add(map);
        }
        return list;
    }

    /**
     * 高频风险作业票
     *
     * @return 结果
     */
    @Override
    public LinkedHashMap<String, Object> queryHighFrequencyRisk() {
        // 获取所有作业票数据
        List<WorkTicket> workTicketList = this.list();

        // 筛选高风险作业票（假设 riskLevel == "高" 表示高风险）
        List<WorkTicket> highRiskTickets = workTicketList.stream()
                .filter(ticket -> "高".equals(ticket.getRiskLevel()))
                .collect(Collectors.toList());

        // 按作业类型分组统计高风险作业票数量
        Map<String, Long> riskCountByType = highRiskTickets.stream()
                .collect(Collectors.groupingBy(WorkTicket::getType, Collectors.counting()));

        // 按预警次数降序排序，取前N个高频风险作业类型
        List<Map.Entry<String, Long>> sortedRiskTypes = riskCountByType.entrySet().stream()
                .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
                .limit(5) // 取前5个高频风险作业类型
                .collect(Collectors.toList());

        // 构造返回结果
        LinkedHashMap<String, Object> result = new LinkedHashMap<>();
        result.put("totalHighRiskTickets", highRiskTickets.size()); // 总高风险作业票数
        result.put("highFrequencyRiskTypes", sortedRiskTypes); // 高频风险作业类型及预警次数
        return result;
    }

    /**
     * 新增作业票信息
     *
     * @param workTicket 作业票信息
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean addWorkTicket(WorkTicket workTicket) throws FebsException {
        workTicket.setCreateTime(DateUtil.formatDateTime(new Date()));
        // 获取此作业类型的流程配置
        WfProcessConfig processConfig = wfProcessConfigService.getOne(Wrappers.<WfProcessConfig>lambdaQuery().eq(WfProcessConfig::getWorkType, workTicket.getType()));
        if (processConfig == null) {
            throw new FebsException("此作业类型不存在");
        }
        List<WfNodeAssignees> nodeAssignees = wfNodeAssigneesService.list(Wrappers.<WfNodeAssignees>lambdaQuery().eq(WfNodeAssignees::getProcessId, processConfig.getId()));
        if (nodeAssignees.size() == 0) {
            throw new FebsException("此作业类型不存在审批人");
        }
        workTicket.setStatus(1);
        String analysisPrompt = buildAnalysisPrompt(workTicket);
        this.save(workTicket);
        // 设置AI分析结果
        queryContent(analysisPrompt, workTicket.getId());
        // 添加审批记录
        List<WorkApprovalLog> workApprovalLogs = new ArrayList<>();
        nodeAssignees.forEach(nodeAssignee -> {
            WorkApprovalLog workApprovalLog = new WorkApprovalLog();
            workApprovalLog.setTicketId(Long.valueOf(workTicket.getId()));
            workApprovalLog.setApproverId(nodeAssignee.getUserId());
            workApprovalLog.setApprovalStep(nodeAssignee.getNodeOrder());
            workApprovalLog.setAction(-1);
            workApprovalLogs.add(workApprovalLog);
        });
        return workApprovalLogService.saveBatch(workApprovalLogs);
    }

    /**
     * 构建AI分析提示词
     */
    private String buildAnalysisPrompt(WorkTicket workTicket) {
        StringBuilder prompt = new StringBuilder();
        prompt.append("请分析以下员工提交作业信息：\n\n");
        prompt.append("作业类型：").append(workTicket.getType()).append("\n");
        prompt.append("作业内容：").append(workTicket.getWorkContent()).append("\n");
        prompt.append("作业开始时间：").append(workTicket.getStartTime()).append("\n");
        prompt.append("作业结束时间：").append(workTicket.getEndTime()).append("\n");

        // 分析要求
        prompt.append("\n请从以下几个方面进行详细分析：\n");
        // 新增内容
        prompt.append("1. **风险等级评估** - 根据员工作业方案和作业类型，评估其可能存在的风险等级（低/中等/高），格式为【风险等级：低】，并说明评估依据\n");
        prompt.append("2. **算法审核得分** - 如果涉及算法相关岗位，评估员工作业方案，并给出具体分数（0-100分），格式为【算法审核得分：100分】\n");
        prompt.append("3. **算法改进建议** - 针对员工在作业方面的准备内容、风险评估、处理方案，提出具体的改进方向和优化建议\n");
        prompt.append("\n请使用结构化的方式输出分析结果，包含具体的分数、数据和事实支撑，并确保分析客观公正。");

        return prompt.toString();
    }

    @Async("aiAnalysisExecutor")
    public void queryContent(String key, Integer id) {
        Message userMessage = Message.builder()
                .role(Role.USER.getValue())
                .content(key)
                .build();
        GenerationParam param = GenerationParam.builder()
                //指定用于对话的通义千问模型名
                .model("qwen-plus")
                .messages(Arrays.asList(userMessage))
                //
                .resultFormat(GenerationParam.ResultFormat.MESSAGE)
                //生成过程中核采样方法概率阈值，例如，取值为0.8时，仅保留概率加起来大于等于0.8的最可能token的最小集合作为候选集。
                // 取值范围为（0,1.0)，取值越大，生成的随机性越高；取值越低，生成的确定性越高。
                .topP(0.8)
                //阿里云控制台DASHSCOPE获取的api-key
                .apiKey("sk-fkebb4821588054a66aa1951d7f239f77c")
                //启用互联网搜索，模型会将搜索结果作为文本生成过程中的参考信息，但模型会基于其内部逻辑“自行判断”是否使用互联网搜索结果。
                .enableSearch(true)
                .build();
        GenerationResult generationResult = null;
        try {
            generationResult = generation.call(param);
        } catch (NoApiKeyException | InputRequiredException e) {
            throw new RuntimeException(e);
        }
        List<String> allContents = generationResult.getOutput().getChoices().stream()
                .map(choice -> choice.getMessage().getContent())
                .collect(Collectors.toList());
        String content = String.join("\n---\n", allContents);
        this.update(Wrappers.<WorkTicket>lambdaUpdate().set(WorkTicket::getAiAuditSuggestion, content).eq(WorkTicket::getId, id));
    }
}
