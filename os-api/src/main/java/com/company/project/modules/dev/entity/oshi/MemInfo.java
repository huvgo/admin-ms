package com.company.project.modules.dev.entity.oshi;

import cn.hutool.core.util.NumberUtil;
import lombok.Setter;

/**
 * 內存相关信息
 *
 * @author fengshuonan
 * @Date 2019-07-13 13:42
 */
@Setter
public class MemInfo {
    /**
     * 内存总量
     */
    private double total;

    /**
     * 已用内存
     */
    private double used;

    /**
     * 剩余内存
     */
    private double free;

    public String getTotal() {
        return NumberUtil.div(total, (1024 * 1024 * 1024), 2) + "MB";
    }

    public String getUsed() {
        return NumberUtil.div(used, (1024 * 1024 * 1024), 2) + "MB";
    }


    public String getFree() {
        return NumberUtil.div(free, (1024 * 1024 * 1024), 2) + "MB";
    }

    public String getUsage() {
        return NumberUtil.mul(NumberUtil.div(used, total, 4), 100) + "%";
    }
}
