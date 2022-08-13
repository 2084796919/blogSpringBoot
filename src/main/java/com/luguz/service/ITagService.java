package com.luguz.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.luguz.entity.Tag;

import java.util.List;

public interface ITagService extends IService<Tag> {
      List<Tag> getIndexTags();

}
