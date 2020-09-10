package com.company.project.modules.system.service.impl;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.crypto.SecureUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.company.project.core.Assert;
import com.company.project.core.Results;
import com.company.project.core.ServiceException;
import com.company.project.modules.system.entity.Menu;
import com.company.project.modules.system.entity.Role;
import com.company.project.modules.system.entity.User;
import com.company.project.modules.system.mapper.UserMapper;
import com.company.project.modules.system.service.MenuService;
import com.company.project.modules.system.service.RoleService;
import com.company.project.modules.system.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * <p>
 * 系统用户 服务实现类
 * </p>
 *
 * @author root
 * @since 2020-08-12
 */
@Slf4j
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    private final MenuService menuService;
    private final RoleService roleService;


    public UserServiceImpl(MenuService menuService, RoleService roleService) {
        this.menuService = menuService;
        this.roleService = roleService;
    }

    @Override
    public User getByUsername(String username) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<User>().eq("username", username);

        return this.getOne(queryWrapper);
    }

    @Override
    public User login(String username, String password) {
        // 判断账号密码是否正确
        User user = this.getByUsername(username);
        Assert.requireNonNull(user, Results.INCORRECT_ACCOUNT_OR_PASSWORD);
        password = SecureUtil.md5().setSalt(user.getSalt().getBytes()).digestHex(password);
        Assert.requireTrue(password.equals(user.getPassword()), Results.INCORRECT_ACCOUNT_OR_PASSWORD);

        // 成功登录后

        // 获取所拥有角色
        List<Integer> roleIds = user.getRoleIds();
        List<Role> roles = roleService.listByIds(roleIds);
        user.setRoles(roles);

        // 获取所拥有菜单
        Set<Menu> menus = new HashSet<>();
        if (user.isSuperAdmin()) {
            List<Menu> allMenu = menuService.list();
            menus.addAll(allMenu);
        } else {
            HashSet<Integer> menuIds = new HashSet<>();
            roles.forEach(role -> menuIds.addAll(role.getMenuIds()));
            if (menuIds.isEmpty()) {
                menus = new HashSet<>();
            } else {
                menus.addAll(menuService.listByIds(menuIds));
                Set<Menu> parentMenus = new HashSet<>();
                menus.stream()
                        .filter(menu -> 0 != menu.getParentId())
                        .forEach(menu -> getAllParents(parentMenus, menu.getParentId()));
                menus.addAll(parentMenus);
            }
        }
        user.setMenus(menus);

        return user;
    }

    public void getAllParents(Set<Menu> menuSet, Integer parentId) {
        Menu parentMenu = menuService.getById(parentId);
        menuSet.add(parentMenu);
        if (0 != parentMenu.getParentId()) {
            Optional<Menu> menu = menuSet.stream().filter(m -> parentMenu.getId().equals(m.getParentId())).findAny();
            if (!menu.isPresent()) {
                getAllParents(menuSet, parentMenu.getParentId());
            }
        }
    }

    @Override
    public void encodePassword(User user) {
        String password = user.getPassword();
        Assert.requireNonNull(password, Results.INCORRECT_ACCOUNT_OR_PASSWORD);
        String salt = IdUtil.fastSimpleUUID();
        password = SecureUtil.md5().setSalt(salt.getBytes()).digestHex(password);
        user.setPassword(password);
        user.setSalt(salt);
    }

    @Value("${admin-os.domain}")
    private String domain;

    private static String CLASS_PATH;

    static {
        try {
            CLASS_PATH = Objects.requireNonNull(UserServiceImpl.class.getClassLoader().getResource("")).toURI().getPath();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public String upload(MultipartFile file, String moduleDir) {
        Assert.requireTrue(!file.isEmpty(), Results.Fail);
        String rootPath = CLASS_PATH + "static";
        String today = DateUtil.format(new DateTime(), "yyyyMMdd");
        String name = IdUtil.fastSimpleUUID();
        String suffix = FileUtil.getSuffix(file.getOriginalFilename());
        String relativePath = "/" + moduleDir + "/" + today + "/" + name + "." + suffix;
        // 目录为: classPath路径/static/moduleDir/今天日期/随机文件名
        File local = new File(rootPath, relativePath);
        log.info("头像上传路径:{}", local.getPath());
        FileUtil.mkParentDirs(local);
        try {
            file.transferTo(local);
        } catch (IOException e) {
            throw new ServiceException(Results.Fail);
        }
        return domain + relativePath;
    }

}
