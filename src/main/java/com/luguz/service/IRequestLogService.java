package com.luguz.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.github.pagehelper.PageInfo;
import com.luguz.entity.RequestLog;


public interface IRequestLogService extends IService<RequestLog> {
    PageInfo<RequestLog> getListByPage(Integer pageNum, Integer pageSize, Wrapper<RequestLog> queryWrapper);
}
