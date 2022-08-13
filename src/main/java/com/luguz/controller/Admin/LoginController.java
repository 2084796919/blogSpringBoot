package com.luguz.controller.Admin;

import com.luguz.config.exception.CaptchaExpireException;
import com.luguz.entity.Result.JsonResult;
import com.luguz.entity.Result.ResultCode;
import com.luguz.entity.Result.ResultUtil;
import com.luguz.entity.User;
import com.luguz.entity.sys.SysMenu;
import com.luguz.service.ISysMenuService;
import com.luguz.service.IUserService;
import com.luguz.util.Constants;
import com.luguz.util.RedisUtil;
import com.luguz.util.TokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class LoginController {
    @Autowired
    private IUserService userService;
    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private ISysMenuService iSysMenuService;


    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public JsonResult login(@RequestParam("username") String username, @RequestParam("password") String password, @RequestParam("code") String code, @RequestParam("uuid") String uuid) {
        String verifyKey = Constants.CAPTCHA_CODE_KEY + uuid;
        String captcha = (String) redisUtil.get(verifyKey);
        redisUtil.del(verifyKey);
        if (captcha == null) {
            throw new CaptchaExpireException("验证码不存在");
        }
        if (!code.equalsIgnoreCase(captcha)) {
            throw new CaptchaExpireException("验证码不匹配");
        }
        Map<String, Object> map = new HashMap<>();
        User user = userService.verifyLogin(username, password);
        if (user != null) {
            String token = TokenUtil.sign(user);
            map.put("token", token);
            System.out.println(token);
            return ResultUtil.success(map, ResultCode.SUCCESS);
        } else {
            return ResultUtil.faile(ResultCode.USER_LOGIN_ERROR);
        }

    }

    @RequestMapping(value = {"/getInfo"}, method = RequestMethod.GET)
    public JsonResult getInfo(ServletRequest request) {
        HttpServletRequest req = (HttpServletRequest) request;
        String token = req.getHeader("Luguz-Token");
        Long usId = null;
        User user = null;
        if ((usId = TokenUtil.getUserId(token)) != null) {
            user = userService.getById(usId);
            user.setPassword(null);
            user.setUsId(null);
            Map<String, Object> map = new HashMap<>();
            map.put("user", user);
            map.put("roles", new String[]{"admin"});
            map.put("permissions", new String[]{"*:*:*"});
            return ResultUtil.success(map, ResultCode.SUCCESS);
        }
        return ResultUtil.faile(ResultCode.DATA_IS_WRONG);
    }

    @RequestMapping(value = "/getRouters", method = RequestMethod.GET)
    public JsonResult getRouters(ServletRequest request) {
        HttpServletRequest req = (HttpServletRequest) request;
        String token = req.getHeader("Luguz-Token");
        Long usId = TokenUtil.getUserId(token);
        if (usId == null) {
            return ResultUtil.faile(ResultCode.Token_AUTH_ERROR);
        }
        List<SysMenu> menus = iSysMenuService.selectMenuTreeByUserId(usId);

//        System.out.println(menus);
        return ResultUtil.success(iSysMenuService.buildMenus(menus), ResultCode.SUCCESS);
    }


}
