package com.company.project.modules.sys.controller;

import cn.hutool.core.lang.Dict;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.company.project.cache.UserCacheService;
import com.company.project.component.annotation.Log2Db;
import com.company.project.component.annotation.Permissions;
import com.company.project.core.Assert;
import com.company.project.core.Result;
import com.company.project.modules.base.controller.BaseController;
import com.company.project.modules.base.service.FileService;
import com.company.project.modules.sys.entity.User;
import com.company.project.modules.sys.service.MenuService;
import com.company.project.modules.sys.service.RoleService;
import com.company.project.modules.sys.service.UserService;
import com.company.project.cache.UserCacheUtil;
import com.company.project.cache.UserCacheLocalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 系统用户 前端控制器
 * </p>
 *
 * @author root
 * @since 2020-08-18
 */
@RestController
@RequestMapping("/sys/user")
public class UserController extends BaseController {
    private final UserService userService;
    private final FileService fileService;
    private final UserCacheService userCacheService;


    public UserController(UserService userService, FileService fileService, UserCacheService userCacheService){
        this.userService = userService;
        this.fileService = fileService;
        this.userCacheService = userCacheService;
    }

    @Permissions
    @PostMapping
    @Log2Db
    public Result<Object> post(@RequestBody User user){
        userService.encodePassword(user);
        userService.save(user);
        return Result.success();
    }

    @Permissions
    @DeleteMapping
    @Log2Db
    public Result<Object> delete(@RequestBody List<Long> ids){
        userService.removeByIds(ids);
        return Result.success();
    }

    @Permissions
    @PutMapping
    @Log2Db
    public Result<Object> put(@RequestBody User user){
        if(StrUtil.isNotBlank(user.getPassword())){
            userService.encodePassword(user);
        }
        userService.updateById(user);
        return Result.success();
    }

    @Permissions
    @GetMapping("/{id}")
    @Log2Db
    public Result<User> get(@PathVariable Integer id){
        User user = userService.getById(id);
        return Result.success(user);
    }

    @Permissions
    @GetMapping
    @Log2Db
    public Result<Page<User>> get(@RequestParam(defaultValue = "0") Integer currentPage, @RequestParam(defaultValue = "10") Integer pageSize, @RequestParam Map<String, Object> params){
        boolean deptIdCondition = StrUtil.isNotEmpty((String) params.get("deptId"));

        QueryWrapper<User> queryWrapper = new QueryWrapper<User>().eq(deptIdCondition, "dept_Id", params.get("deptId")).or()
                .apply(deptIdCondition, "FIND_IN_SET({0},dept_Ids)", params.get("deptId"));
        Page<User> page = userService.page(new Page<>(currentPage, pageSize, true), queryWrapper);
        return Result.success(page);
    }

    /*
    密码:111111
    盐:860effd2852141adad4dd5b256209b4a
    加密后密码:44944f63ddca4e2d1c77329df9e0d751
     */
    @PostMapping("/login")
    public Result<Object> login(@RequestBody User user){
        User currentUser = userService.login(user.getUsername(), user.getPassword());
        // 重新刷新缓存
        String token = userCacheService.getToken(currentUser.getUsername());
        if(token == null){
            token = IdUtil.fastSimpleUUID();
            userCacheService.putToken(currentUser.getUsername(), token);
        }
        userCacheService.putUser(token, currentUser);
        return Result.success(Dict.create().set("token", token));
    }

    @GetMapping("/token")
    public Result<Object> token(String token){
        User user = userCacheService.getUser(token);
        Assert.requireNonNull(user, "登录过期,请重新登陆");
        return Result.success(user);
    }

    @PostMapping("/logout")
    public Result<Object> logout(){
        return Result.success();
    }


    @PostMapping("/upload")
    public Result<Object> upload(@RequestParam("file") MultipartFile file){
        String moduleDir = "avatar";
        String path = fileService.upload(file, moduleDir);
        User user = new User();
        user.setId(UserCacheUtil.getCurrentUser().getId());
        user.setAvatar(path);
        userService.updateById(user);
        return Result.success(path);
    }

}
