package com.oldlane.cheeseblog.web.restapi;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.lang.UUID;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.oldlane.cheeseblog.base.result.Result;
import com.oldlane.cheeseblog.commons.entity.Role;
import com.oldlane.cheeseblog.commons.entity.UserRole;
import com.oldlane.cheeseblog.commons.entity.Users;
import com.oldlane.cheeseblog.utils.JwtUtil;
import com.oldlane.cheeseblog.utils.RegexUtils;
import com.oldlane.cheeseblog.xo.holder.UserHolder;
import com.oldlane.cheeseblog.xo.service.MailService;
import com.oldlane.cheeseblog.xo.service.RoleService;
import com.oldlane.cheeseblog.xo.service.UserRoleService;
import com.oldlane.cheeseblog.xo.service.UsersService;
import com.oldlane.cheeseblog.xo.vo.LoginFormVO;
import com.oldlane.cheeseblog.xo.vo.UserVO;
import io.jsonwebtoken.Claims;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import static com.oldlane.cheeseblog.base.global.RedisConstants.*;
import static com.oldlane.cheeseblog.base.global.SystemConstants.*;

/**
 * Date: 2022/10/30 22:30
 * Description: 登录接口
 */
@RestController
@Slf4j
@Api(tags = "登录接口")
@RequestMapping("/login")
public class LoginRestApi {

    @Autowired
    private UsersService usersService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private MailService mailService;

    @Autowired
    private UserRoleService userRoleService;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;


    @ApiOperation(value = "用户登录", notes = "用户登录")
    @PostMapping("/login")
    public Result login(@RequestBody LoginFormVO loginFormVO) {
        String username = loginFormVO.getUsername();
        String password = loginFormVO.getPassword();
        Users user = null;
        //用户名密码登录还是邮箱登录
        if (StrUtil.isNotBlank(username)) {
            LambdaQueryWrapper<Users> usersLambdaQueryWrapper = new LambdaQueryWrapper<>();
            usersLambdaQueryWrapper.eq(Users::getUsername, username);
            user = usersService.getOne(usersLambdaQueryWrapper);
            if (user != null) {
                if (!Objects.equals(user.getPassword(), password)) {
                    return Result.fail().message("密码错误");
                }
            } else {
                return Result.fail().message("用户不存在");
            }
        } else {
            //检验邮箱格式
            String email = loginFormVO.getEmail();
            boolean emailInvalid = RegexUtils.isEmailInvalid(email);
            if (emailInvalid) {
                return Result.fail().message("邮箱格式错误");
            }
            //从redis获取验证码
            String cacheCode = stringRedisTemplate.opsForValue().get(LOGIN_KEY_CODE + email);
            String code = loginFormVO.getCode();
            if (!code.equals(cacheCode)) {
                return Result.fail().message("验证码错误");
            }
            LambdaQueryWrapper<Users> usersLambdaQueryWrapper = new LambdaQueryWrapper<>();
            usersLambdaQueryWrapper.eq(Users::getEmail, email);
            user = usersService.getOne(usersLambdaQueryWrapper);
            //不存在则自动注册
            if (user == null) {
                user = register(email);
            }
        }

        String subject = UUID.randomUUID().toString(true);
        String token = JwtUtil.createJWT(subject, user.getId(), true, null);

        UserVO userVO = BeanUtil.copyProperties(user, UserVO.class);
        String role = getRoleByUserId(user.getId());
        userVO.setRole(role);
        //转为map存储
        Map<String, Object> userMap = BeanUtil.beanToMap(userVO, new HashMap<>(), CopyOptions.create().ignoreNullValue().setFieldValueEditor((fieldName, fieldValue) -> fieldValue.toString()));

        log.info("userMap: {}", userMap);
        //存入redis
        stringRedisTemplate.opsForHash().putAll(LOGIN_USER_TOKEN + subject, userMap);
        //设置过期时间
        stringRedisTemplate.expire(LOGIN_USER_TOKEN + subject, LOGIN_TOKEN_TTL, TimeUnit.MINUTES);
        log.info("token: {}", token);
        HashMap<String, Object> resultMap = new HashMap<>();
        resultMap.put("token", token);
        resultMap.put("userInfo", userVO);

        return Result.ok(resultMap);
    }

    @ApiOperation("发送验证码")
    @PostMapping("/code")
    public Result sendCode(@RequestParam("email") String email) {
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

    @ApiOperation(value = "退出登录", notes = "退出登录")
    @PostMapping("/logout")
    public Result logout(@ApiParam(name = "token", value = "token令牌", required = false) @RequestHeader("Authorization") String token) {
        log.info("logout:token => {}", token);
        Claims claims = JwtUtil.parseJWT(token);
        assert claims != null;
        String subject = claims.get("user_open_id", String.class);
        stringRedisTemplate.delete(LOGIN_USER_TOKEN + subject);
        return Result.ok().message("退出成功");
    }

    public Users register(String email) {
        Users user = new Users();
        user.setEmail(email);
        user.setNickname(USER_NICK_NAME_PREFIX + RandomUtil.randomString(10));
        user.setUsername(USER_NICK_NAME_PREFIX + RandomUtil.randomString(10));
        // 2.保存用户
        usersService.save(user);
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
