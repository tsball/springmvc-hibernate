package com.springmvc.services;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.springmvc.constants.TaskKeyConst;
import com.springmvc.models.Person;
import com.springmvc.repositories.PersonRepository;

@Service
@Transactional
public class ActivitiTaskService {
	
	@Autowired
    private RuntimeService runtimeService;

    @Autowired
    private TaskService taskService;

    @Autowired
    private PersonRepository personRepository;

    public void startProcess(Person person) {
        Map<String, Object> variables = new HashMap<String, Object>();
        variables.put("person", person);
        runtimeService.startProcessInstanceByKey(TaskKeyConst.ONE_TASK_PROCESS, variables);
    }

    public List<Task> getTasks(Long personId) {
        return taskService.createTaskQuery().taskAssignee(personId.toString()).list();
    }
}
