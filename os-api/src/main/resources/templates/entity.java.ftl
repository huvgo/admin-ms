package ${package}.entity;

import com.company.project.modules.common.Entity;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;



/**
 * <p>
 * ${tableComment!}
 * </p>
 *
 * @author ${author}
 * @since ${date}
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName(value = "${tableName}")
public class ${upperFirstName} extends Entity<${table[0].javaType}> {

<#list table as row>
    <#if row.field != "id">
        /**
        * ${row.comment}
        */
        private ${row.javaType} ${row.field};

    </#if>
</#list>
}

