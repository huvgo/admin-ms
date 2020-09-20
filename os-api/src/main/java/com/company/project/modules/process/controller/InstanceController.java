package com.company.project.modules.process.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.company.project.core.Result;
import com.company.project.core.Results;
import com.company.project.modules.base.controller.BaseController;
import com.company.project.modules.process.entity.Instance;
import com.company.project.modules.process.entity.TaskInstance;
import com.company.project.modules.process.service.InstanceService;
import com.company.project.modules.process.service.TaskInstanceService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
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
@RequestMapping("/process/instance")
public class InstanceController extends BaseController {
    private final InstanceService instanceService;

    private final RepositoryService repositoryService;

    private final RuntimeService runtimeService;

    private final TaskService taskService;

    private final TaskInstanceService taskInstanceService;

    public InstanceController(InstanceService instanceService, RepositoryService repositoryService, RuntimeService runtimeService, TaskService taskService, TaskInstanceService taskInstanceService) {
        this.instanceService = instanceService;
        this.repositoryService = repositoryService;
        this.runtimeService = runtimeService;
        this.taskService = taskService;
        this.taskInstanceService = taskInstanceService;
    }

    @PostMapping
    public Result<?> post(@RequestBody Instance instance) {
        instanceService.save(instance);
        return Results.SUCCESS;
    }

    @DeleteMapping
    public Result<?> delete(@RequestBody List<Long> ids) {
        instanceService.removeByIds(ids);
        return Results.SUCCESS;
    }

    @PutMapping
    public Result<?> put(@RequestBody Instance instance) {
        instanceService.updateById(instance);
        return Results.SUCCESS;
    }

    @GetMapping("/{id}")
    public Result<Instance> get(@PathVariable Integer id) {
        Instance instance = instanceService.getById(id);
        return Results.SUCCESS.setData(instance);
    }

    @GetMapping
    public Result<Page<Instance>> get(@RequestParam(defaultValue = "0") Integer currentPage, @RequestParam(defaultValue = "10") Integer pageSize, @RequestParam Map<String, Object> params) {
        QueryWrapper<Instance> queryWrapper = new QueryWrapper<>();
        Page<Instance> page = instanceService.page(new Page<>(currentPage, pageSize, true), queryWrapper);
        return Results.SUCCESS.setData(page);
    }


    /**
     * 流程申请
     */
    @PostMapping(value = "/start")
    public Result<?> startProcess(@RequestBody Instance instance) {
        instance.setUserId(getCurrentLoginUser().getId())
                .setDeptId(getCurrentLoginUser().getDeptId());

        //查询流程定义
        ProcessDefinition result = repositoryService.createProcessDefinitionQuery()
                .processDefinitionId(instance.getProcessDefinitionId())
                .processDefinitionTenantId("group")
                .latestVersion()
                .singleResult();

        //开启流程
        Map<String, Object> vars = new HashMap<>();
        //请假
        vars.put("assignee0", getCurrentLoginUser().getId());
        vars.put("assignee1", "4");
        vars.put("assignee2", "1");
        ProcessInstance processInstance = runtimeService.startProcessInstanceById(result.getId(), instance.getId(), vars);//流程定义的id,业务数据id,内置的参数

        //自动执行第一个任务节点
        Task task = taskService.createTaskQuery().processInstanceId(processInstance.getId()).singleResult();
        taskService.complete(task.getId());
        Task nextTask = taskService.createTaskQuery().processInstanceId(processInstance.getId()).singleResult();
        String assignee = nextTask.getAssignee();
        instance.setCurrNodeUserId(assignee);
        instance.setStatus("0");
        instanceService.save(instance);
        return Results.SUCCESS;
    }


    /**
     * 提交审核
     */
    @PostMapping(value = "/commit")
    public Result<?> commit(@RequestBody TaskInstance taskInstance) {
        // 保存审批内容
        taskInstanceService.save(taskInstance);
        String instanceId = taskInstance.getInstanceId();

        // 查询任务
        Instance instance = instanceService.getById(instanceId);

        // 如果审批结果为2则表示同意
        if ("2".equals(taskInstance.getHandleType())) {
            // 完成当前节点任务 开启下一节点
            Task task = taskService.createTaskQuery().processInstanceId(instanceId).singleResult();
            taskService.complete(task.getId());
            // 获取下一节点
            Task nextTask = taskService.createTaskQuery().processInstanceId(instanceId).singleResult();
            if (nextTask != null) {
                String assignee = nextTask.getAssignee();
                instance.setCurrNodeUserId(assignee);
            } else {
                instance.setStatus("1");

            }

        } else {
            // 审批结果不同意或者撤销
            runtimeService.deleteProcessInstance(instanceId, taskInstance.getHandleOpinion());
        }
        instanceService.updateById(instance);

        return Results.SUCCESS;
    }


}