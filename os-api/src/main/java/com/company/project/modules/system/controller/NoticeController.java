package com.company.project.modules.system.controller;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.company.project.cache.UserCacheUtil;
import com.company.project.component.annotation.RequirePermission;
import com.company.project.component.annotation.SaveLog;
import com.company.project.core.Result;
import com.company.project.core.Results;
import com.company.project.modules.system.entity.Notice;
import com.company.project.modules.system.entity.User;
import com.company.project.modules.system.service.NoticeService;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * <p>
 * 系统通知 前端控制器
 * </p>
 *
 * @author author
 * @since 2020-09-02
 */
@RestController
@RequestMapping("/system/notice")
public class NoticeController {
    private final NoticeService noticeService;
    private final UserNoticeServer userNoticeServer;

    public NoticeController(NoticeService noticeService, UserNoticeServer userNoticeServer) {
        this.noticeService = noticeService;
        this.userNoticeServer = userNoticeServer;
    }

    @PostMapping
    @SaveLog
    @RequirePermission
    public Result<?> post(@RequestBody Notice notice) {
        User user = UserCacheUtil.getCurrentUser();
        notice.setSenderId(user.getId());
        notice.setSender(user.getUsername());
        notice.setSenderAvatar(user.getAvatar());
        if (Objects.isNull(notice.getPushTime())) {
            notice.setPushTime(new Date());
        }
        noticeService.save(notice);

        //主动向用户发送通知
        userNoticeServer.sendNotice();

        return Results.SUCCESS;
    }

    @DeleteMapping
    @SaveLog
    @RequirePermission
    public Result<?> delete(@RequestBody List<Long> ids) {
        noticeService.removeByIds(ids);
        return Results.SUCCESS;
    }

    @PutMapping
    @SaveLog
    @RequirePermission
    public Result<?> put(@RequestBody Notice notice) {
        noticeService.updateById(notice);
        return Results.SUCCESS;
    }

    @GetMapping("/{id}")
    @RequirePermission
    public Result<Notice> get(@PathVariable Integer id) {
        Notice notice = noticeService.getById(id);
        return Results.SUCCESS.setData(notice);
    }

    @GetMapping
    @RequirePermission
    public Result<Page<Notice>> get(@RequestParam(defaultValue = "0") Integer currentPage, @RequestParam(defaultValue = "10") Integer pageSize, @RequestParam Map<String, Object> params) {
        Page<Notice> page = noticeService.page(new Page<>(currentPage, pageSize, true), new QueryWrapper<Notice>()
                .like(StrUtil.isNotBlank((String) params.get("sender")), "sender", params.get("sender"))
                .eq(StrUtil.isNotBlank((String) params.get("content")), "content", params.get("content"))
                .eq(StrUtil.isNotBlank((String) params.get("createDate")), "create_date", params.get("createDate"))
                .eq(StrUtil.isNotBlank((String) params.get("type")), "type", params.get("type"))
        );
        return Results.SUCCESS.setData(page);
    }
}