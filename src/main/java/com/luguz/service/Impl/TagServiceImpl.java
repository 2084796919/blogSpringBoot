package com.luguz.service.Impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.luguz.entity.Tag;
import com.luguz.mapper.TagMapper;
import com.luguz.service.ITagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class TagServiceImpl extends ServiceImpl<TagMapper, Tag> implements ITagService {
    @Autowired
    private TagMapper tagMapper;

    @Override
    public List<Tag> getIndexTags() {
        return tagMapper.getIndexTag();
    }

}
