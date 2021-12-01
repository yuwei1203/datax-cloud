package cn.datax.service.workflow.dao;

import cn.datax.common.base.BaseDao;
import cn.datax.service.workflow.api.entity.BusinessEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 业务流程配置表 Mapper 接口
 * </p>
 *
 * @author yuwei
 * @since 2020-09-22
 */
@Mapper
public interface BusinessDao extends BaseDao<BusinessEntity> {

}
