package com.company.project.modules.activiti.controller;


import com.company.project.core.Result;
import com.company.project.core.Results;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.DeploymentBuilder;
import org.activiti.engine.repository.ProcessDefinition;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/activiti/process")
public class ProcessController {

    private final RepositoryService repositoryService;

    public ProcessController(RepositoryService repositoryService) {
        this.repositoryService = repositoryService;
    }


    @PostMapping("/deploy")
    public Result<?> deploy(@RequestParam("file") MultipartFile file) {
        try {
            DeploymentBuilder deploymentBuilder = repositoryService.createDeployment().addBytes(file.getOriginalFilename(), file.getBytes()).tenantId("group");
            deploymentBuilder.deploy();
        } catch (IOException e) {
            return Results.Fail;
        }
        return Results.SUCCESS;
    }


    /**
     * 查询所有流程定义
     *
     */
    @GetMapping("/definition")
    public Result<?> definition() {
        List<ProcessDefinition> processDefinitionList = repositoryService.createProcessDefinitionQuery().processDefinitionTenantId("group").latestVersion().list();
        return Results.SUCCESS.setData(processDefinitionList);
    }

    /**
     * 流程挂起
     *
     */
    @GetMapping("/suspend/{processDefinitionId}")
    public Result<?> suspend(@PathVariable("processDefinitionId") String processDefinitionId) {
        repositoryService.suspendProcessDefinitionById(processDefinitionId);
        return Results.SUCCESS;
    }

    /**
     * 流程激活
     *
     */
    @GetMapping("/activate/{processDefinitionId}")
    public Result<?> activate(@PathVariable String processDefinitionId) {
        repositoryService.activateProcessDefinitionById(processDefinitionId);
        return Results.SUCCESS;
    }
}
