package com.company.project.modules.com;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

@Data
public class BaseEntity {
    @TableId(type= IdType.AUTO)
    private Integer id;
}
