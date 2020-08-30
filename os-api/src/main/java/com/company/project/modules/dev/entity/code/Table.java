/**
 * Copyright 2020 bejson.com
 */
package com.company.project.modules.dev.entity.code;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class Table {

    private String comment;
    private boolean generator;
    private String name;
    private String engine;
    private Date createTime;
    private int parentMenuId;
    private List<Column> columns;
}