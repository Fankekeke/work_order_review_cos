package cc.mrbird.febs.cos.entity;

import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 算法审核规则知识库
 *
 * @author FanK
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class AuditRuleLibrary implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 规则名称
     */
    private String ruleName;

    /**
     * 关联风险关键词(逗号分隔)
     */
    private String keyword;

    /**
     * 必须包含的安全措施关键词
     */
    private String mandatoryMeasure;

    /**
     * 触发规则时的警告提示词
     */
    private String riskWarning;

    /**
     * 是否启用
     */
    private Integer isActive;


}
