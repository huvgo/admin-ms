package com.company.project.modules.audit.controller;

import cn.hutool.core.bean.BeanUtil;
import com.company.project.core.Result;
import com.company.project.core.Results;
import com.company.project.modules.audit.vo.Definition;
import com.company.project.modules.base.controller.BaseController;
import org.flowable.engine.RepositoryService;
import org.flowable.engine.repository.DeploymentBuilder;
import org.flowable.engine.repository.ProcessDefinition;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/audit/process")
public class ProcessController extends BaseController {

    private final RepositoryService repositoryService;

    public ProcessController(RepositoryService repositoryService) {
        this.repositoryService = repositoryService;
    }

    /**
     * 添加（部署）一个流程定义
     */
    @PostMapping
    public Result<?> deploy(@RequestParam("file") MultipartFile file) {
        try {
            DeploymentBuilder deploymentBuilder = repositoryService.createDeployment().addBytes(file.getOriginalFilename(), file.getBytes());
            deploymentBuilder.deploy();
        } catch (IOException e) {
            return Results.Fail;
        }
        return Results.SUCCESS;
    }

    /**
     * 查询所有流程定义
     */
    @GetMapping
    public Result<?> list() {
        List<ProcessDefinition> processDefinitionList = repositoryService.createProcessDefinitionQuery().latestVersion().list();
        List<Definition> definitionList = new ArrayList<>();
        processDefinitionList.forEach(processDefinition -> {
            Definition definition = new Definition();
            BeanUtil.copyProperties(processDefinition, definition);
            definitionList.add(definition);
        });
        return Results.SUCCESS.setData(definitionList);
    }

    /**
     * 流程挂起
     */
    @GetMapping("/suspend/{processDefinitionId}")
    public Result<?> suspend(@PathVariable("processDefinitionId") String processDefinitionId) {
        repositoryService.suspendProcessDefinitionById(processDefinitionId);
        return Results.SUCCESS;
    }

    /**
     * 流程激活
     */
    @GetMapping("/activate/{processDefinitionId}")
    public Result<?> activate(@PathVariable String processDefinitionId) {
        repositoryService.activateProcessDefinitionById(processDefinitionId);
        return Results.SUCCESS;
    }

    @DeleteMapping
    public Result<?> delete(@RequestBody List<String> deploymentIds) {
        for (String deploymentId : deploymentIds) {
            repositoryService.deleteDeployment(deploymentId);
        }
        return Results.SUCCESS;
    }
}
