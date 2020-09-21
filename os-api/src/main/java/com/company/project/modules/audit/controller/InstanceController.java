package com.company.project.modules.audit.controller;

import cn.hutool.core.lang.Dict;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.company.project.core.Result;
import com.company.project.core.Results;
import com.company.project.modules.audit.entity.Instance;
import com.company.project.modules.audit.entity.TaskInstance;
import com.company.project.modules.audit.service.InstanceService;
import com.company.project.modules.audit.service.TaskInstanceService;
import com.company.project.modules.base.controller.BaseController;
import com.company.project.modules.system.entity.User;
import com.company.project.modules.system.service.UserService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author codeGenerator
 * @since 2020-09-17
 */
@RestController
@RequestMapping("/audit/instance")
public class InstanceController extends BaseController {
    private final InstanceService instanceService;

    private final RepositoryService repositoryService;

    private final RuntimeService runtimeService;

    private final TaskService taskService;

    private final TaskInstanceService taskInstanceService;


    private final UserService userService;

    public InstanceController(InstanceService instanceService, RepositoryService repositoryService, RuntimeService runtimeService, TaskService taskService, TaskInstanceService taskInstanceService, UserService userService) {
        this.instanceService = instanceService;
        this.repositoryService = repositoryService;
        this.runtimeService = runtimeService;
        this.taskService = taskService;
        this.taskInstanceService = taskInstanceService;
        this.userService = userService;
    }


    /**
     * 获取我的待办列表
     */
    @GetMapping
    public Result<Page<Instance>> page(@RequestParam(defaultValue = "0") Integer currentPage, @RequestParam(defaultValue = "10") Integer pageSize, @RequestParam Map<String, Object> params) {
        QueryWrapper<Instance> queryWrapper = new QueryWrapper<>();
        if (!getCurrentLoginUser().isSuperAdmin()) {
            queryWrapper.eq("curr_node_user_id", getCurrentLoginUser().getId());
            queryWrapper.eq("status", 1);
        }
        Page<Instance> page = instanceService.page(new Page<>(currentPage, pageSize, true), queryWrapper);
        for (Instance record : page.getRecords()) {
            User user = userService.getById(record.getUserId());
            record.setOther(Dict.create().set("applicantName", user.getName()));
        }
        return Results.SUCCESS.setData(page);
    }


    /**
     * 流程申请
     */
    @PostMapping(value = "/apply")
    public Result<?> startProcess(@RequestBody Instance instance) {
        instance.setUserId(getCurrentLoginUser().getId()).setDeptId(getCurrentLoginUser().getDeptId());
        instanceService.save(instance);
        //查询流程定义
        ProcessDefinition result = repositoryService.createProcessDefinitionQuery()
                .processDefinitionId(instance.getProcessDefinitionId())
                .latestVersion()
                .singleResult();

        //开启流程
        Map<String, Object> vars = new HashMap<>();
        //请假
        vars.put("assignee0", getCurrentLoginUser().getId());
        vars.put("assignee1", "4");
        vars.put("assignee2", "1");
        ProcessInstance processInstance = runtimeService.startProcessInstanceById(result.getId(), instance.getId(), vars);//流程定义的id,业务数据id,内置的参数
        instance.setProcessId(processInstance.getId());
        //自动执行第一个任务节点
        Task task = taskService.createTaskQuery().processInstanceId(processInstance.getId()).singleResult();
        taskService.complete(task.getId());
        Task nextTask = taskService.createTaskQuery().processInstanceId(processInstance.getId()).singleResult();
        String assignee = nextTask.getAssignee();
        instance.setCurrNodeUserId(assignee);
        instance.setStatus(1);
        instanceService.updateById(instance);
        return Results.SUCCESS;
    }


    /**
     * 提交审核
     */
    @PostMapping(value = "/approve")
    public Result<?> approve(@RequestBody TaskInstance taskInstance) {

        taskInstanceService.save(taskInstance);

        Instance instance = instanceService.getById(taskInstance.getInstanceId());

        // 如果审批结果为2则表示同意
        if (taskInstance.getHandleType() == 2) {
            // 完成当前节点任务 开启下一节点
            Task task = taskService.createTaskQuery().processInstanceId(taskInstance.getProcessId()).singleResult();
            taskService.complete(task.getId());
            // 获取下一节点
            Task nextTask = taskService.createTaskQuery().processInstanceId(taskInstance.getProcessId()).singleResult();
            if (nextTask != null) {
                String assignee = nextTask.getAssignee();
                instance.setCurrNodeUserId(assignee);
            } else {
                instance.setStatus(1);
            }

        } else {
            // 审批结果不同意或者撤销
            runtimeService.deleteProcessInstance(instance.getProcessId(), "审批结果不同意或者撤销");
        }
        instanceService.updateById(instance);

        return Results.SUCCESS;
    }


}