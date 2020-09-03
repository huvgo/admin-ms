package com.company.project.modules.sys.controller;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.company.project.cache.UserCacheUtil;
import com.company.project.component.annotation.Log2Db;
import com.company.project.core.Result;
import com.company.project.modules.sys.entity.Notification;
import com.company.project.modules.sys.entity.User;
import com.company.project.modules.sys.service.NotificationService;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 系统通知 前端控制器
 * </p>
 *
 * @author author
 * @since 2020-09-02
 */
@RestController
@RequestMapping("sys/notification")
public class NotificationController {
    private final NotificationService notificationService;

    public NotificationController(NotificationService notificationService){
        this.notificationService = notificationService;
    }

    @PostMapping
    @Log2Db
    public Result<?> post(@RequestBody Notification notification){
        User user = UserCacheUtil.getCurrentUser();
        notification.setSenderId(user.getId());
        notification.setSender(user.getUsername());
        notification.setCreateDate(new Date());
        notificationService.save(notification);
        return Result.success();
    }

    @DeleteMapping
    @Log2Db
    public Result<?> delete(@RequestBody List<Long> ids){
        notificationService.removeByIds(ids);
        return Result.success();
    }

    @PutMapping
    @Log2Db
    public Result<?> put(@RequestBody Notification notification){
        notificationService.updateById(notification);
        return Result.success();
    }

    @GetMapping("/{id}")
    public Result<Notification> get(@PathVariable Integer id){
        Notification notification = notificationService.getById(id);
        return Result.success(notification);
    }

    @GetMapping
    public Result<Page<Notification>> get(@RequestParam(defaultValue = "0") Integer current, @RequestParam(defaultValue = "10") Integer size, @RequestParam Map<String, Object> params){
        Page<Notification> page = notificationService.page(new Page<>(current, size, true), new QueryWrapper<Notification>()
                .like(StrUtil.isNotBlank((String) params.get("sender")), "sender", params.get("sender"))
                .eq(StrUtil.isNotBlank((String) params.get("content")), "content", params.get("content"))
                .eq(StrUtil.isNotBlank((String) params.get("createDate")), "create_date", params.get("createDate"))
                .eq(StrUtil.isNotBlank((String) params.get("type")), "type", params.get("type"))
        );
        return Result.success(page);
    }
}