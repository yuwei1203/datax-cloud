package cn.datax.service.data.standard.service.impl;

import cn.datax.service.data.standard.api.entity.TypeEntity;
import cn.datax.service.data.standard.api.dto.TypeDto;
import cn.datax.service.data.standard.service.TypeService;
import cn.datax.service.data.standard.mapstruct.TypeMapper;
import cn.datax.service.data.standard.dao.TypeDao;
import cn.datax.common.base.BaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * <p>
 * 数据标准类别表 服务实现类
 * </p>
 *
 * @author yuwei
 * @since 2020-08-26
 */
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class TypeServiceImpl extends BaseServiceImpl<TypeDao, TypeEntity> implements TypeService {

    @Autowired
    private TypeDao typeDao;

    @Autowired
    private TypeMapper typeMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public TypeEntity saveType(TypeDto typeDto) {
        TypeEntity type = typeMapper.toEntity(typeDto);
        typeDao.insert(type);
        return type;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public TypeEntity updateType(TypeDto typeDto) {
        TypeEntity type = typeMapper.toEntity(typeDto);
        typeDao.updateById(type);
        return type;
    }

    @Override
    public TypeEntity getTypeById(String id) {
        TypeEntity typeEntity = super.getById(id);
        return typeEntity;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteTypeById(String id) {
        typeDao.deleteById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteTypeBatch(List<String> ids) {
        typeDao.deleteBatchIds(ids);
    }
}
