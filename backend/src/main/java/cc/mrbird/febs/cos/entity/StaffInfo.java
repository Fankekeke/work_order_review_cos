package cc.mrbird.febs.cos.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 员工管理
 *
 * @author FanK
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class StaffInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 员工编号
     */
    private String code;

    /**
     * 员工姓名
     */
    private String name;

    /**
     * 性别（1.男 2.女）
     */
    private Integer sex;

    /**
     * 状态（1.接单中 2.离开）
     */
    private Integer status;

    /**
     * 照片
     */
    private String images;

    /**
     * 创建时间
     */
    private LocalDateTime createDate;

    /**
     * 所属账户
     */
    private Long userId;

    /**
     * 邮箱地址
     */
    private String email;

    /**
     * 联系方式
     */
    private String phone;

    /**
     * 身份证地址
     */
    private String address;

    /**
     * 民族
     */
    private String ethnicity;

    /**
     * 真实姓名
     */
    private String realName;

    /**
     * 身份证号码
     */
    private String idNumber;

    /**
     * 出生日期
     */
    private LocalDate birthDate;

    /**
     * 身份证正面
     */
    private String idCardFrontImages;

    /**
     * 身份证反面
     */
    private String idCardReverseImages;


}
