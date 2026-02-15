package cc.mrbird.febs.cos.entity;

import lombok.Data;

@Data
public class AuditUser {

    private Integer processId;
    private Integer userId;

    private String staffName;
    private String staffImages;
    private String deptName;
    private String positionName;
    private String roleKey;
    private Integer nodeOrder;
}
