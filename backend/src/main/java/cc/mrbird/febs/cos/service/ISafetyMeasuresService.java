package cc.mrbird.febs.cos.service;

import cc.mrbird.febs.cos.entity.SafetyMeasures;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.apache.ibatis.annotations.Param;

import java.util.LinkedHashMap;

/**
 * @author FanK
 */
public interface ISafetyMeasuresService extends IService<SafetyMeasures> {

    /**
     * 分页获取安全措施复核信息
     *
     * @param page           分页对象
     * @param safetyMeasures 安全措施复核信息
     * @return 结果
     */
    IPage<LinkedHashMap<String, Object>> queryPage(Page<SafetyMeasures> page, SafetyMeasures safetyMeasures);
}
