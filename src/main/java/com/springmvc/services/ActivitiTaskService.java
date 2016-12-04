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
import com.springmvc.models.Employee;
import com.springmvc.repositories.EmployeeRepository;

@Service
@Transactional
public class ActivitiTaskService {
	
	@Autowired
    private RuntimeService runtimeService;

    @Autowired
    private TaskService taskService;

    @Autowired
    private EmployeeRepository employeeRepository;

    public void startProcess(Employee employee) {
        Map<String, Object> variables = new HashMap<String, Object>();
        variables.put("employee", employee);
        runtimeService.startProcessInstanceByKey(TaskKeyConst.ONE_TASK_PROCESS, variables);
    }

    public List<Task> getTasks(Long employeeId) {
        return taskService.createTaskQuery().taskAssignee(employeeId.toString()).list();
    }
}
