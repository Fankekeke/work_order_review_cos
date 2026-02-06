package cc.mrbird.febs.cos.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 作业票主表
 *
 * @author FanK
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class WorkTicket implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 作业票编号
     */
    private String ticketCode;

    /**
     * 作业类型(1:动火, 2:高处, 3:受限空间)
     */
    private Integer type;

    /**
     * 状态(0:草稿, 1:待审批, 2:作业中, 3:已完工, 4:已驳回)
     */
    private Integer status;

    /**
     * 申请人ID
     */
    private Long applicantId;

    /**
     * 作业内容描述
     */
    private String workContent;

    /**
     * 作业地点
     */
    private String location;

    /**
     * 计划开始时间
     */
    private String startTime;

    /**
     * 计划结束时间
     */
    private String endTime;

    /**
     * 风险等级(低/中等/高)
     */
    private String riskLevel;

    /**
     * 算法审核得分
     */
    private BigDecimal aiAuditScore;

    /**
     * 算法改进建议
     */
    private String aiAuditSuggestion;

    private String createTime;

    private String updateTime;


}
