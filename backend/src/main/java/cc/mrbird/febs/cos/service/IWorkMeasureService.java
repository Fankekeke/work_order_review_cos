package cc.mrbird.febs.cos.service;

import cc.mrbird.febs.cos.entity.WorkMeasure;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.apache.ibatis.annotations.Param;

import java.util.LinkedHashMap;

/**
 * @author FanK
 */
public interface IWorkMeasureService extends IService<WorkMeasure> {

    /**
     * 分页获取安全措施确认信息
     *
     * @param page        分页对象
     * @param workMeasure 安全措施确认信息
     * @return 结果
     */
    IPage<LinkedHashMap<String, Object>> queryPage(Page<WorkMeasure> page, WorkMeasure workMeasure);
}
