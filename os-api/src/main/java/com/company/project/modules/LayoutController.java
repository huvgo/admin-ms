package com.company.project.modules;

import cn.hutool.core.collection.CollUtil;
import com.company.project.core.Assert;
import com.company.project.core.Result;
import com.company.project.core.Results;
import com.company.project.modules.common.service.UserCacheService;
import com.company.project.modules.system.entity.Menu;
import com.company.project.modules.system.entity.User;
import com.company.project.modules.system.util.MenuUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 布局 前端控制器
 */
@RestController
@RequestMapping("/layout")
public class LayoutController {

    private final UserCacheService userCacheService;

    public LayoutController(UserCacheService userCacheService) {
        this.userCacheService = userCacheService;
    }

    /**
     * 侧边栏
     */
    @GetMapping("/sidebar")
    public Result<Set<Menu>> sidebar(@RequestHeader(value = "X-Token") String token) throws JsonProcessingException {
        User user = userCacheService.getUser(token);
        Assert.requireNonNull(user, Results.LOGIN_EXPIRED);
        Set<Menu> menus = user.getMenus();
        if (CollUtil.isEmpty(menus)) {
            return Results.SUCCESS.setData(menus);
        }
        // 过滤掉按钮 只保留菜单
        List<Menu> collect = menus.stream().filter(menu -> menu.getType() != 2).collect(Collectors.toList());
        // 构建tree列表
        List<Menu> tree = MenuUtil.buildTree(collect, 0);
        menus = new LinkedHashSet<>(tree);
        return Results.SUCCESS.setData(menus);
    }
}
