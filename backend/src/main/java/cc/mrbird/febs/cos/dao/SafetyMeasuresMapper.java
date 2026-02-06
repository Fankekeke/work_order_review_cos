package cc.mrbird.febs.cos.dao;

import cc.mrbird.febs.cos.entity.SafetyMeasures;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.LinkedHashMap;

/**
 * @author FanK
 */
public interface SafetyMeasuresMapper extends BaseMapper<SafetyMeasures> {

    /**
     * 分页获取安全措施复核信息
     *
     * @param page           分页对象
     * @param safetyMeasures 安全措施复核信息
     * @return 结果
     */
    IPage<LinkedHashMap<String, Object>> queryPage(Page<SafetyMeasures> page, @Param("safetyMeasures") SafetyMeasures safetyMeasures);
}
