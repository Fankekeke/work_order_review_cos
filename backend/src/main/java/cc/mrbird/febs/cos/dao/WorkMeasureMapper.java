package cc.mrbird.febs.cos.dao;

import cc.mrbird.febs.cos.entity.WorkMeasure;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.LinkedHashMap;

/**
 * @author FanK
 */
public interface WorkMeasureMapper extends BaseMapper<WorkMeasure> {

    /**
     * 分页获取安全措施确认信息
     *
     * @param page        分页对象
     * @param workMeasure 安全措施确认信息
     * @return 结果
     */
    IPage<LinkedHashMap<String, Object>> queryPage(Page<WorkMeasure> page, @Param("workMeasure") WorkMeasure workMeasure);
}
