package com.company.project.modules.sys.controller;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.company.project.cache.UserCacheUtil;
import com.company.project.component.annotation.Log2DB;
import com.company.project.component.annotation.Permissions;
import com.company.project.core.Results;
import com.company.project.core.Result;
import com.company.project.modules.sys.entity.Notice;
import com.company.project.modules.sys.entity.User;
import com.company.project.modules.sys.service.NoticeService;
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
@RequestMapping("sys/notice")
public class NoticeController {
    private final NoticeService noticeService;

    public NoticeController(NoticeService noticeService){
        this.noticeService = noticeService;
    }

    @PostMapping
    @Log2DB
    @Permissions
    public Result<?> post(@RequestBody Notice notice){
        User user = UserCacheUtil.getCurrentUser();
        notice.setSenderId(user.getId());
        notice.setSender(user.getUsername());
        notice.setSenderAvatar(user.getAvatar());
//        if (Objects.isNull(notice.getCreateDate())) {
//            notice.setCreateDate(new Date());
//        }
        noticeService.save(notice);
        return Results.SUCCESS;
    }

    @DeleteMapping
    @Log2DB
    @Permissions
    public Result<?> delete(@RequestBody List<Long> ids){
        noticeService.removeByIds(ids);
        return Results.SUCCESS;
    }

    @PutMapping
    @Log2DB
    @Permissions
    public Result<?> put(@RequestBody Notice notice){
        noticeService.updateById(notice);
        return Results.SUCCESS;
    }

    @GetMapping("/{id}")
    @Permissions
    public Result<Notice> get(@PathVariable Integer id){
        Notice notice = noticeService.getById(id);
        return Results.success(notice);
    }

    @GetMapping
    @Permissions
    public Result<Page<Notice>> get(@RequestParam(defaultValue = "0") Integer currentPage, @RequestParam(defaultValue = "10") Integer pageSize, @RequestParam Map<String, Object> params){
        Page<Notice> page = noticeService.page(new Page<>(currentPage, pageSize, true), new QueryWrapper<Notice>()
                .like(StrUtil.isNotBlank((String) params.get("sender")), "sender", params.get("sender"))
                .eq(StrUtil.isNotBlank((String) params.get("content")), "content", params.get("content"))
                .eq(StrUtil.isNotBlank((String) params.get("createDate")), "create_date", params.get("createDate"))
                .eq(StrUtil.isNotBlank((String) params.get("type")), "type", params.get("type"))
        );
        return Results.success(page);
    }
}