package com.oldlane.cheeseblog.xo.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.lang.UUID;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.oldlane.cheeseblog.base.result.Result;
import com.oldlane.cheeseblog.commons.entity.Role;
import com.oldlane.cheeseblog.commons.entity.UserRole;
import com.oldlane.cheeseblog.commons.entity.Users;
import com.oldlane.cheeseblog.utils.RegexUtils;
import com.oldlane.cheeseblog.xo.holder.UserHolder;
import com.oldlane.cheeseblog.xo.mapper.UsersMapper;
import com.oldlane.cheeseblog.xo.service.MailService;
import com.oldlane.cheeseblog.xo.service.RoleService;
import com.oldlane.cheeseblog.xo.service.UserRoleService;
import com.oldlane.cheeseblog.xo.service.UsersService;
import com.oldlane.cheeseblog.xo.vo.LoginFormVO;
import com.oldlane.cheeseblog.xo.vo.UserVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import static com.oldlane.cheeseblog.base.global.RedisConstants.*;
import static com.oldlane.cheeseblog.base.global.SystemConstants.*;

/**
* @author lenovo
* @description 针对表【users】的数据库操作Service实现
* @createDate 2022-10-09 15:33:27
*/
@Service
@Slf4j
public class UsersServiceImpl extends ServiceImpl<UsersMapper, Users>
    implements UsersService {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private MailService mailService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private UserRoleService userRoleService;


    @Override
    public Result login(LoginFormVO loginForm) {
        String username = loginForm.getUsername();
        String password = loginForm.getPassword();
        Users user = null;
        //用户名密码登录还是邮箱登录
        if (StrUtil.isNotBlank(username)) {
            user = query().eq("username", username).one();
            if (user != null) {
                if (!Objects.equals(user.getPassword(), password)) {
                    return Result.fail().message("密码错误");
                }
            } else {
                return Result.fail().message("用户不存在");
            }
        } else {
            //检验邮箱格式
            String email = loginForm.getEmail();
            boolean emailInvalid = RegexUtils.isEmailInvalid(email);
            if (emailInvalid) {
                return Result.fail().message("邮箱格式错误");
            }
            //从redis获取验证码
            String cacheCode = stringRedisTemplate.opsForValue().get(LOGIN_KEY_CODE + email);
            String code = loginForm.getCode();
            if (!code.equals(cacheCode)) {
                return Result.fail().message("验证码错误");
            }

            user = query().eq("email", email).one();
            //不存在则自动注册
            if (user == null) {
                user = register(email);
            }
        }

        String token = UUID.randomUUID().toString(true);
        UserVO userDTO = BeanUtil.copyProperties(user, UserVO.class);
        String role = getRoleByUserId(user.getId());
        userDTO.setRole(role);
        //转为map存储
        Map<String, Object> userMap = BeanUtil.beanToMap(userDTO, new HashMap<>(), CopyOptions.create().ignoreNullValue().setFieldValueEditor((fieldName, fieldValue) -> fieldValue.toString()));

        log.info("userMap: {}", userMap);
        //存入redis
        stringRedisTemplate.opsForHash().putAll(LOGIN_USER_TOKEN + token, userMap);
        //设置过期时间
        stringRedisTemplate.expire(LOGIN_USER_TOKEN + token, LOGIN_TOKEN_TTL, TimeUnit.MINUTES);

        return Result.ok(token);
    }

    @Override
    public Result sendCode(String email) {
        //检验邮箱格式
        boolean emailInvalid = RegexUtils.isEmailInvalid(email);
        if (emailInvalid) {
            return Result.fail().message("邮箱格式错误");
        }
        String code = RandomUtil.randomNumbers(6);
        log.info("验证码：{}", code);
        // 4.保存验证码到redis并设置过期时间
        stringRedisTemplate.opsForValue().set(LOGIN_KEY_CODE + email, code, LOGIN_CODE_TTL, TimeUnit.MINUTES );
        //发送验证码
        mailService.sendValidateCode(code, email, "登录验证码");
        return Result.ok();
    }

    @Override
    public Result logout(String token) {
        log.info("logout:token => {}", token);
        stringRedisTemplate.delete(LOGIN_USER_TOKEN + token);
        UserHolder.removeUser();
        return Result.ok().message("退出成功");
    }

    @Override
    public Result getAuthorTop10() {
        LambdaQueryWrapper<Users> wrapper = new LambdaQueryWrapper<>();
        wrapper.last("limit 10");
        List<Users> usersList = this.list(wrapper);
        return Result.ok(usersList);
    }

    public Users register(String email) {
        Users user = new Users();
        user.setEmail(email);
        user.setNickname(USER_NICK_NAME_PREFIX + RandomUtil.randomString(10));
        user.setUsername(USER_NICK_NAME_PREFIX + RandomUtil.randomString(10));
        // 2.保存用户
        save(user);
        UserRole userRole = new UserRole();
        userRole.setUserId(user.getId());
        userRole.setRoleId(1L);
        //第一次注册默认是普通用户
        userRoleService.save(userRole);
        return user;
    }

    /**
     * 根据用户id获取角色信息
     * @param id 用户id
     * @return 角色信息
     */
    public String getRoleByUserId(Long id) {
        LambdaQueryWrapper<UserRole> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UserRole::getUserId, id);
        UserRole one = userRoleService.getOne(queryWrapper);
        Role role = roleService.getById(one.getRoleId());
        return role.getKeyword();
    }
}




