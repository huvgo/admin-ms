package com.company.project.osactiviti;

import org.activiti.api.process.model.ProcessDefinition;
import org.activiti.api.process.runtime.ProcessRuntime;
import org.activiti.api.runtime.shared.query.Page;
import org.activiti.api.runtime.shared.query.Pageable;
import org.activiti.api.task.runtime.TaskRuntime;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.runtime.ProcessInstance;
import org.junit.After;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;
import java.util.Map;


@RunWith(SpringRunner.class)
@SpringBootTest
class OsActivitiApplicationTests {

  /*  @Autowired
    private ProcessRuntime processRuntime;

    @Autowired
    private TaskRuntime taskRuntime;

    @Autowired
    private SecurityUtil securityUtil;

    @Autowired
    private RuntimeService runtimeService;*/


    @Test
    public void testDefinition() {
        //1.得到ProcessEngine对象
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();

        //2.得到RuntimeService对象
        RuntimeService runtimeService = processEngine.getRuntimeService();
        //3.设置assignee的取值   用户可以在界面上设置流程的执行人
        Map<String,Object> map = new HashMap<>();
        map.put("assignee0","zhangsan");
        map.put("assignee1","lishi");
        map.put("assignee2","wangwu");

        //4.启动流程实例，同时还要设置流程定义的assignee的值
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("holiday2", map);

        //5.输出
        System.out.println(processEngine.getName());

      /*  securityUtil.logInAs("salaboy");
        Page<ProcessDefinition> processDefinitionPage = processRuntime.processDefinitions(Pageable.of(0, 10));

        for (ProcessDefinition processDefinition : processDefinitionPage.getContent()) {
            System.out.println("processDefinition = " + processDefinition);
        }*/
    }

    /*@Test
    void contextLoads() {
    }*/

}
