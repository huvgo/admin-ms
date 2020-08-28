package com.company.project.modules.dev.util;

import java.util.HashMap;

public class Convert {


    private static final HashMap<String, String> jdbcType2JavaTypeMap = new HashMap<>();

    private static final String STRING = "String";
    private static final String INTEGER = "Integer";

    static {
        jdbcType2JavaTypeMap.put("tinyint", INTEGER);
        jdbcType2JavaTypeMap.put("smallint", INTEGER);
        jdbcType2JavaTypeMap.put("mediumint", INTEGER);
        jdbcType2JavaTypeMap.put("int", INTEGER);
        jdbcType2JavaTypeMap.put(INTEGER, INTEGER);
        jdbcType2JavaTypeMap.put("bigint", "Long");
        jdbcType2JavaTypeMap.put("float", "Float");
        jdbcType2JavaTypeMap.put("double", "Double");
        jdbcType2JavaTypeMap.put("decimal", "BigDecimal");
        jdbcType2JavaTypeMap.put("bit", "Boolean");


        jdbcType2JavaTypeMap.put("char", STRING);
        jdbcType2JavaTypeMap.put("varchar", STRING);
        jdbcType2JavaTypeMap.put("tinytext", STRING);
        jdbcType2JavaTypeMap.put("text", STRING);
        jdbcType2JavaTypeMap.put("mediumtext", STRING);
        jdbcType2JavaTypeMap.put("longtext", STRING);

        jdbcType2JavaTypeMap.put("date", "Date");
        jdbcType2JavaTypeMap.put("datetime", "Date");
        jdbcType2JavaTypeMap.put("timestamp", "Date");
    }

    public static String jdbcType2JavaType(String jdbcType) {
        return jdbcType2JavaTypeMap.get(jdbcType);
    }
}
