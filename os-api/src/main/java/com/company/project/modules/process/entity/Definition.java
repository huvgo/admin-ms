package com.company.project.modules.process.entity;

import lombok.Data;
import org.activiti.engine.ProcessEngineConfiguration;
import org.activiti.engine.impl.bpmn.data.IOSpecification;
import org.activiti.engine.impl.persistence.entity.SuspensionState;

import java.util.Map;

@Data
public class Definition {

    private String id;
    private String name;
    private String description;
    private String key;
    private int version;
    private String category;
    private String deploymentId;
    private String resourceName;
    private String tenantId = ProcessEngineConfiguration.NO_TENANT_ID;
    private Integer historyLevel;
    private String diagramResourceName;
    private boolean isGraphicalNotationDefined;
    private Map<String, Object> variables;
    private boolean hasStartFormKey;
    private int suspensionState = SuspensionState.ACTIVE.getStateCode();
    private boolean isIdentityLinksInitialized;
    private IOSpecification ioSpecification;
    private Integer appVersion;

    // Backwards compatibility
    private String engineVersion;


    public Boolean getSuspensionState() {
        return suspensionState == 1;
    }
}
