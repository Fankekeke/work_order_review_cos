package cc.mrbird.febs.cos.entity;

import java.time.LocalDateTime;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 作业票附件表
 *
 * @author FanK
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class WorkAttachment implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long ticketId;

    private String fileName;

    private String fileUrl;

    /**
     * 上传节点(1:申请阶段, 2:开工确认, 3:结束报告)
     */
    private Integer uploadNode;

    private Long uploaderId;

    private LocalDateTime createTime;


}
