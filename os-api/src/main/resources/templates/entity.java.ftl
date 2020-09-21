package ${package}.entity;

import com.company.project.modules.base.entity.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

<#list fields as field>
    <#if field.javaType = "Date">
        import java.util.Date;
        <#break>
    </#if>
</#list>

/**
* <p>
    * ${table.comment!}
    * </p>
*
* @author ${author}
* @since ${date}
*/
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName(value = "${table.name}")
public class ${upperFirstName} extends BaseEntity {
public class ${upperFirstName} extends BaseEntity {

    <#list fields as field>
            /**
            * ${field.comment}
            */
            private ${field.javaType} ${field.name};

    </#list>
    }


