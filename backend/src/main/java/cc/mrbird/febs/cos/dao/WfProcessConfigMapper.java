package cc.mrbird.febs.cos.dao;

import cc.mrbird.febs.cos.entity.WfProcessConfig;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.LinkedHashMap;

/**
 * @author FanK
 */
public interface WfProcessConfigMapper extends BaseMapper<WfProcessConfig> {

    /**
     * 分页获取审批流配置信息
     *
     * @param page            分页对象
     * @param wfProcessConfig 审批流配置信息
     * @return 结果
     */
    IPage<LinkedHashMap<String, Object>> queryPage(Page<WfProcessConfig> page, @Param("wfProcessConfig") WfProcessConfig wfProcessConfig);
}
