package com.luguz.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.luguz.entity.Type;

import java.util.List;

public interface ITypeService extends IService<Type> {
    List<Type> getIndexTypes();

}
