package com.company.project.modules;

import com.company.project.core.Assert;
import com.company.project.core.Result;
import com.company.project.modules.sys.entity.Menu;
import com.company.project.modules.sys.entity.User;
import com.company.project.modules.sys.service.CacheService;
import com.company.project.modules.sys.util.MenuUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 布局 前端控制器
 */
@RestController
@RequestMapping("/layout")
public class LayoutController {


    private final CacheService cacheService;

    public LayoutController(CacheService cacheService) {
        this.cacheService = cacheService;
    }

    /**
     * 侧边栏
     */
    @GetMapping("/sidebar")
    public Result<List<Menu>> sidebar(@RequestHeader(value = "X-Token") String token) throws JsonProcessingException {
        User user = cacheService.getUser(token);
        Assert.requireNonNull(user, "登录过期,请重新登陆");
        List<Menu> menuList = user.getMenuList();
        // 过滤掉按钮 只保留菜单
        List<Menu> collect = menuList.stream().filter(menu -> menu.getType() != 2).collect(Collectors.toList());
        // 构建tree列表
        List<Menu> tree = MenuUtil.buildTree(collect, 0);
        return Result.success(tree);
    }
}
