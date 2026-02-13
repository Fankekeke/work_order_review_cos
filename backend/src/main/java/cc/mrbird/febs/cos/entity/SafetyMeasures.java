package cc.mrbird.febs.cos.entity;

import java.time.LocalDateTime;
import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 安全措施落实复核表
 *
 * @author FanK
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class SafetyMeasures implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 关联作业单ID
     */
    private Long permitId;

    /**
     * 安全措施内容
     */
    private String measureContent;

    /**
     * 负责人是否落实
     */
    private Integer isConfirmed;

    /**
     * 监护人是否复核
     */
    private Integer isReviewed;

    /**
     * 落实时间
     */
    private String confirmTime;

    /**
     * 复核时间
     */
    private String reviewTime;

    @TableField(exist = false)
    private String staffName;
    @TableField(exist = false)
    private String type;
    @TableField(exist = false)
    private String riskLevel;
}
