package com.company.project.modules.process.controller;


import com.company.project.core.Result;
import com.company.project.core.Results;
import org.activiti.engine.RepositoryService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/process")
public class ProcessController {

    private final RepositoryService repositoryService;

    public ProcessController(RepositoryService repositoryService) {
        this.repositoryService = repositoryService;
    }


    /**
     * 流程申请
     */
    @GetMapping(value = "/start")
    public Result<?> startProcess() {

        return Results.SUCCESS;
    }


}
