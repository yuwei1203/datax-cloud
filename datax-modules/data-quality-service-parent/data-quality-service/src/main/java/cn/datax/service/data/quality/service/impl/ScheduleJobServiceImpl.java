package cn.datax.service.data.quality.service.impl;

import cn.datax.common.core.DataConstant;
import cn.datax.service.data.quality.api.entity.ScheduleJobEntity;
import cn.datax.service.data.quality.schedule.CronTaskRegistrar;
import cn.datax.service.data.quality.schedule.SchedulingRunnable;
import cn.datax.service.data.quality.service.ScheduleJobService;
import cn.datax.service.data.quality.mapstruct.ScheduleJobMapper;
import cn.datax.service.data.quality.dao.ScheduleJobDao;
import cn.datax.common.base.BaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 * 数据质量监控任务信息表 服务实现类
 * </p>
 *
 * @author yuwei
 * @since 2020-09-29
 */
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class ScheduleJobServiceImpl extends BaseServiceImpl<ScheduleJobDao, ScheduleJobEntity> implements ScheduleJobService {

    @Autowired
    private ScheduleJobDao scheduleJobDao;

    @Autowired
    private ScheduleJobMapper scheduleJobMapper;

    @Autowired
    private CronTaskRegistrar cronTaskRegistrar;

    @Override
    public ScheduleJobEntity getScheduleJobById(String id) {
        ScheduleJobEntity scheduleJobEntity = super.getById(id);
        return scheduleJobEntity;
    }

    @Override
    public void pauseScheduleJobById(String id) {
        ScheduleJobEntity scheduleJobEntity = super.getById(id);
        SchedulingRunnable task = new SchedulingRunnable(id, scheduleJobEntity.getBeanName(), scheduleJobEntity.getMethodName(), scheduleJobEntity.getMethodParams());
        cronTaskRegistrar.removeCronTask(task);
        scheduleJobEntity.setStatus(DataConstant.TrueOrFalse.FALSE.getKey());
        scheduleJobDao.updateById(scheduleJobEntity);
    }

    @Override
    public void resumeScheduleJobById(String id) {
        ScheduleJobEntity scheduleJobEntity = super.getById(id);
        SchedulingRunnable task = new SchedulingRunnable(id, scheduleJobEntity.getBeanName(), scheduleJobEntity.getMethodName(), scheduleJobEntity.getMethodParams());
        cronTaskRegistrar.addCronTask(task, scheduleJobEntity.getCronExpression());
        scheduleJobEntity.setStatus(DataConstant.TrueOrFalse.TRUE.getKey());
        scheduleJobDao.updateById(scheduleJobEntity);
    }
}
