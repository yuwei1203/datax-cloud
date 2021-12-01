package cn.datax.service.data.quality.dao;

import cn.datax.common.base.BaseDao;
import cn.datax.service.data.quality.api.entity.ScheduleJobEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 数据质量监控任务信息表 Mapper 接口
 * </p>
 *
 * @author yuwei
 * @since 2020-09-29
 */
@Mapper
public interface ScheduleJobDao extends BaseDao<ScheduleJobEntity> {

}
