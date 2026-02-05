package cc.mrbird.febs.cos.entity;

import java.time.LocalDateTime;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 作业票安全措施确认表
 *
 * @author FanK
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class WorkMeasure implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 关联作业票ID
     */
    private Long ticketId;

    /**
     * 安全措施描述
     */
    private String measureContent;

    /**
     * 是否确认(0:未确认, 1:已确认)
     */
    private Integer isConfirmed;

    /**
     * 确认人ID
     */
    private Long confirmerId;

    /**
     * 确认时间
     */
    private LocalDateTime confirmTime;

    /**
     * 现场核实照片地址
     */
    private String confirmPhotoUrl;


}
