package ${package}.entity;

import com.company.project.core.Entity;
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
public class ${upperFirstName} extends Entity {

<#list table as row>
    /**
    * ${row.comment}
    */
    private ${row.javaType} ${row.field};

</#list>
}

