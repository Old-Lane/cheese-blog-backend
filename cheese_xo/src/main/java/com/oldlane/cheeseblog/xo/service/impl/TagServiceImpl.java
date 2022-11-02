package com.oldlane.cheeseblog.xo.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.oldlane.cheeseblog.commons.entity.Tag;
import com.oldlane.cheeseblog.xo.mapper.TagMapper;
import com.oldlane.cheeseblog.xo.service.TagService;
import org.springframework.stereotype.Service;

/**
* @author lenovo
* @description 针对表【tag】的数据库操作Service实现
* @createDate 2022-11-02 23:21:18
*/
@Service
public class TagServiceImpl extends ServiceImpl<TagMapper, Tag>
    implements TagService {

}




