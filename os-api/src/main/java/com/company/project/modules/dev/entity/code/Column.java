/**
 * Copyright 2020 bejson.com
 */
package com.company.project.modules.dev.entity.code;

import lombok.Data;

@Data
public class Column {

    private String dataType;
    private String javaType;
    private boolean condition;
    private String extra;
    private String columnName;
    private String name;
    private String comment;
    private String primaryKey;
    private String element = "1";

}