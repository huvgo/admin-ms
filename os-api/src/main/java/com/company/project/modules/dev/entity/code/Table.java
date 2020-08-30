/**
 * Copyright 2020 bejson.com
 */
package com.company.project.modules.dev.entity.code;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class Table {

    private String comment;
    private boolean generator;
    private String name;
    private String engine;
    @JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss", timezone = "GMT+8")
    private Date createTime;
    private int parentMenuId;
    private List<Column> columns;
}