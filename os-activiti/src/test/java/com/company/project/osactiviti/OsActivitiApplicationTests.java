package com.company.project.osactiviti;

import org.activiti.api.process.model.ProcessDefinition;
import org.activiti.api.process.model.builders.ProcessPayloadBuilder;
import org.activiti.api.process.runtime.ProcessRuntime;
import org.activiti.api.runtime.shared.query.Page;
import org.activiti.api.runtime.shared.query.Pageable;
import org.activiti.api.task.model.Task;
import org.activiti.api.task.model.builders.TaskPayloadBuilder;
import org.activiti.api.task.runtime.TaskRuntime;
import org.activiti.engine.RuntimeService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;
import java.util.Map;


@RunWith(SpringRunner.class)
@SpringBootTest
public class OsActivitiApplicationTests {
    private Logger logger = LoggerFactory.getLogger(OsActivitiApplicationTests.class);

    @Autowired
    private ProcessRuntime processRuntime;

    @Autowired
    private TaskRuntime taskRuntime;

    @Autowired
    private SecurityUtil securityUtil;

    @Autowired
    private RuntimeService runtimeService;


    @Test
    public void testDefinition() {

        securityUtil.logInAs("salaboy");
        Page<ProcessDefinition> processDefinitionPage = processRuntime.processDefinitions(Pageable.of(0, 10));

        for (ProcessDefinition processDefinition : processDefinitionPage.getContent()) {
            System.out.println("processDefinition = " + processDefinition);
        }
    }

    /**
     * 测试创建实例
     */
    @Test
    public void testCreateInstance() {
        securityUtil.logInAs("salaboy");
        Map<String, Object> map = new HashMap<>();
        map.put("assignee0", "salaboy"); // 申请人
        map.put("assignee1", "other"); // 部门经理审批
        map.put("assignee2", "admin"); // 总经理审批人
        processRuntime.start(ProcessPayloadBuilder.start()
                .withProcessDefinitionKey("holiday2")
                .withVariables(map)
                .withName("0请假申请")
                .build()
        );
    }

    /**
     * 测试获取任务
     */
    @Test
    public void testGetTask() {
        securityUtil.logInAs("salaboy");
        // 获取10个任务
        Page<Task> tasks = taskRuntime.tasks(Pageable.of(0, 10));
        if (tasks.getTotalItems() > 0) {
            for (Task t : tasks.getContent()) {
                logger.info("> Claiming task: " + t.getId());
                // 完成任务
                taskRuntime.complete(TaskPayloadBuilder.complete().withTaskId(t.getId()).build());
            }
        } else {
            logger.info("> There are no task for me to work on.");
        }
    }


    @Test
    public void testGetTask2() {
        securityUtil.logInAs("other");
        // 获取10个任务
        Page<Task> tasks = taskRuntime.tasks(Pageable.of(0, 10));
        if (tasks.getTotalItems() > 0) {
            for (Task t : tasks.getContent()) {
                logger.info("> Claiming task: " + t.getId());
                // 完成任务
                taskRuntime.complete(TaskPayloadBuilder.complete().withTaskId(t.getId()).build());
            }
        } else {
            logger.info("> There are no task for me to work on.");
        }
    }


    @Test
    public void testGetTask3() {
        securityUtil.logInAs("admin");
        // 获取10个任务
        Page<Task> tasks = taskRuntime.tasks(Pageable.of(0, 10));
        if (tasks.getTotalItems() > 0) {
            for (Task t : tasks.getContent()) {
                logger.info("> Claiming task: " + t.getId());
                // 完成任务
                taskRuntime.complete(TaskPayloadBuilder.complete().withTaskId(t.getId()).build());
            }
        } else {
            logger.info("> There are no task for me to work on.");
        }
    }
}
