package cc.mrbird.febs.cos.entity;

import java.time.LocalDateTime;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 审批流转记录表
 *
 * @author FanK
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class WorkApprovalLog implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 关联作业票ID
     */
    private Long ticketId;

    /**
     * 审批人ID
     */
    private Long approverId;

    /**
     * 操作(1:通过, 2:驳回)
     */
    private Integer action;

    /**
     * 审批意见
     */
    private String comments;

    /**
     * 当前环节(1:初审, 2:复审 3：终审)
     */
    private Integer approvalStep;

    private LocalDateTime createTime;

    /**
     * 电子签名
     */
    private String electronicSign;


}
