package com.luguz.controller.Admin;

import com.luguz.entity.Result.JsonResult;
import com.luguz.entity.Result.ResultCode;
import com.luguz.entity.Result.ResultUtil;
import com.luguz.entity.other.TreeSelect;
import com.luguz.entity.sys.SysMenu;
import com.luguz.service.ISysMenuService;
import com.luguz.util.TokenUtil;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 菜单信息
 *
 */
@RestController
@RequestMapping("/system/menu")
public class SysMenuController
{
    @Autowired
    private ISysMenuService iSysMenuService;

    /**
     * 加载对应角色菜单列表树
     */
    @GetMapping(value = "/roleMenuTreeselect/{roleId}")
    public JsonResult roleMenuTreeselect(@PathVariable("roleId") Long roleId, ServletRequest request)
    {
        HttpServletRequest req = (HttpServletRequest) request;
        String token = req.getHeader("Luguz-Token");
        Long usId = TokenUtil.getUserId(token);
        List<SysMenu> menus = iSysMenuService.selectMenuList(usId);
        Map<String, Object> map = new HashMap<>();
        map.put("checkedKeys", iSysMenuService.selectMenuListByRoleId(roleId));
        map.put("menus", iSysMenuService.buildMenuTreeSelect(menus));
        System.out.println(map);
        return ResultUtil.success(map, ResultCode.SUCCESS);
    }


    /**
     * 加载菜单列表树
     */
    @GetMapping(value = "/treeselect")
    public JsonResult roleMenuTreeselect(ServletRequest request)
    {
        HttpServletRequest req = (HttpServletRequest) request;
        String token = req.getHeader("Luguz-Token");
        Long usId = TokenUtil.getUserId(token);
        List<SysMenu> menus = iSysMenuService.selectMenuList(usId);
        List<TreeSelect> treeSelects = iSysMenuService.buildMenuTreeSelect(menus);
        return ResultUtil.success(treeSelects, ResultCode.SUCCESS);
    }


}