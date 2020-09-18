package com.company.project.modules.process.controller;


import cn.hutool.core.util.IdUtil;
import com.company.project.core.Result;
import com.company.project.core.Results;
import com.company.project.modules.base.controller.BaseController;
import com.company.project.modules.process.entity.Instance;
import com.company.project.modules.process.service.InstanceService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/process")
public class ProcessController extends BaseController {

    private final RepositoryService repositoryService;

    private final RuntimeService runtimeService;

    private final InstanceService instanceService;

    private final TaskService taskService;

    public ProcessController(RepositoryService repositoryService, RuntimeService runtimeService, InstanceService instanceService, TaskService taskService) {
        this.repositoryService = repositoryService;
        this.runtimeService = runtimeService;
        this.instanceService = instanceService;
        this.taskService = taskService;
    }


    /**
     * 流程申请
     */
    @PostMapping(value = "/start")
    public Result<?> startProcess(@RequestBody Instance instance) {
        instance.setUserId(getCurrentLoginUser().getId())
                .setDeptId(getCurrentLoginUser().getDeptId());
        instanceService.save(instance);
        //查询流程定义
        ProcessDefinition result = repositoryService.createProcessDefinitionQuery()
                .processDefinitionId(instance.getProcessDefinitionId())
                .processDefinitionTenantId("group")
                .latestVersion()
                .singleResult();

        //开启流程
        Map<String, Object> vars = new HashMap<>();
        //请假
        vars.put("days", "3");
        ProcessInstance processInstance = runtimeService.startProcessInstanceById(result.getId(), instance.getId(), vars);//流程定义的id,业务数据id,内置的参数

        //自动执行第一个任务节点
        Task task = taskService.createTaskQuery().processInstanceId(processInstance.getId()).singleResult();
        taskService.complete(task.getId());
        return Results.SUCCESS;
    }


}
