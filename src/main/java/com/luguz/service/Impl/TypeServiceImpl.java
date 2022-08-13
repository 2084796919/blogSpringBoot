package com.luguz.service.Impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.luguz.entity.Type;
import com.luguz.mapper.TypeMapper;
import com.luguz.service.ITypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class TypeServiceImpl extends ServiceImpl<TypeMapper, Type> implements ITypeService {

    @Autowired
    private TypeMapper typeMapper;

    @Override
    public List<Type> getIndexTypes() {
        return typeMapper.getIndexTypes();
    }

}
