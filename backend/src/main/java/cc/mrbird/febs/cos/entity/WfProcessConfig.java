package cc.mrbird.febs.cos.entity;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 审批流配置
 *
 * @author FanK
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class WfProcessConfig implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 模板名称
     */
    private String workName;

    /**
     * 作业类型
     */
    private String workType;

    /**
     * 节点顺序
     */
    private Integer nodeOrder;

    /**
     * 节点名称
     */
    private String nodeName;

    /**
     * 关联角色: leader/guardian/approver
     */
    private String roleKey;

    /**
     * 节点处理人
     */
    @TableField(exist = false)
    private String nodeAssignees;

}
