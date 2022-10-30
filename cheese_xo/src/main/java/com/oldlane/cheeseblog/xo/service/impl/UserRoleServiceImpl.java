package com.oldlane.cheeseblog.xo.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.oldlane.cheeseblog.commons.entity.UserRole;
import com.oldlane.cheeseblog.xo.mapper.UserRoleMapper;
import com.oldlane.cheeseblog.xo.service.UserRoleService;
import org.springframework.stereotype.Service;

/**
* @author lenovo
* @description 针对表【user_role(用户角色关联表)】的数据库操作Service实现
* @createDate 2022-10-16 20:24:49
*/
@Service
public class UserRoleServiceImpl extends ServiceImpl<UserRoleMapper, UserRole>
    implements UserRoleService {

}




