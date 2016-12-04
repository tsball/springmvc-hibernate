package com.springmvc.controllers;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.activiti.engine.task.Task;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.springmvc.forms.EmployeeForm;
import com.springmvc.models.Employee;
import com.springmvc.models.Role;
import com.springmvc.repositories.EmployeeRepository;
import com.springmvc.repositories.RoleRepository;
import com.springmvc.representations.TaskRepresentation;
import com.springmvc.services.ActivitiTaskService;
import com.springmvc.services.OperationLogService;
import com.springmvc.utils.DateTimeUtil;

@RestController
@RequestMapping("/employees")
public class EmployeeController {
	
	@Autowired EmployeeRepository employeeRepository;
	@Autowired ActivitiTaskService activitiTaskService;
	@Autowired OperationLogService operationLogService;
	@Autowired RoleRepository roleRepository;
	
	@RequestMapping(value = "", method = RequestMethod.GET)
	public ModelAndView listAllUsers() {
		ModelAndView mv = new ModelAndView("/employees/index");
		
		Pageable pageable = new PageRequest(0, 10, Sort.Direction.ASC, "name");
		Specification<Employee> spec = new Specification<Employee>() {  
            @Override  
            public Predicate toPredicate(Root<Employee> root, CriteriaQuery<?> query, CriteriaBuilder cb) {  
                root = query.from(Employee.class); // from table/join/fetch
                Path<String> nameExp = root.get("name");  
                return cb.like(nameExp, "%P%");  
            }
        };
		Page<Employee> page = employeeRepository.findAll(spec, pageable);
		
		mv.addObject("page", page);		
		return mv;
	}
	
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public ModelAndView show(@PathVariable("id") Long id) {
		ModelAndView mv = new ModelAndView("/employees/show");
		
		Employee employee = employeeRepository.findOne(id);
		
		mv.addObject("employee", employee);
		return mv;
	}
	
	@RequestMapping(value="/{id}/tasks", method= RequestMethod.GET)
    public ModelAndView tasks(@PathVariable("id") Long id) {
		ModelAndView mv = new ModelAndView("/employees/tasks");
		
		Employee employee = employeeRepository.findOne(id);
		
        List<Task> tasks = activitiTaskService.getTasks(id);
        List<TaskRepresentation> taskRepresentations = new ArrayList<TaskRepresentation>();
        for (Task task : tasks) {
        	taskRepresentations.add(new TaskRepresentation(task.getId(), task.getName()));
        }
        
        mv.addObject("employee", employee);
        mv.addObject("taskRepresentations", taskRepresentations);
		return mv;
    }
	
	@RequestMapping(value="/{id}/leaves", method= RequestMethod.GET)
    public ModelAndView leaves(@PathVariable("id") Long id) {
		ModelAndView mv = new ModelAndView("/employees/leaves");
		
		Employee employee = employeeRepository.findOne(id);
		
        List<Task> tasks = activitiTaskService.getTasks(id);
        List<TaskRepresentation> taskRepresentations = new ArrayList<TaskRepresentation>();
        for (Task task : tasks) {
        	taskRepresentations.add(new TaskRepresentation(task.getId(), task.getName()));
        }
        
        mv.addObject("employee", employee);
        mv.addObject("taskRepresentations", taskRepresentations);
		return mv;
    }
	
	@RequestMapping(value="/{id}/tasks", method = RequestMethod.POST)
	public ModelAndView startTask(@PathVariable("id") Long id, final RedirectAttributes redirectAttr) {
		
		Employee employee = employeeRepository.findOne(id);
		
		activitiTaskService.startProcess(employee);
		
		redirectAttr.addFlashAttribute("notice", "Start task successed!");
		
		return new ModelAndView("redirect:/employees/" + employee.getId() + "/tasks");
	}
	
	@RequestMapping(value = "/add", method = RequestMethod.GET)
	public ModelAndView add(HttpServletRequest request) {
		ModelAndView mv = new ModelAndView("/employees/add", "employee", new Employee());
		
		List<Role> roles = roleRepository.findAll();
		
		mv.addObject("roles", roles);
		return mv;
	}
	
	@RequestMapping(value = "", method = RequestMethod.POST)
	public ResponseEntity<?> create(HttpServletRequest request, @Valid EmployeeForm form, BindingResult result, final RedirectAttributes redirectAttr) {
		
		if (result.hasErrors()) {
			return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(result.getFieldErrors());
		}
		
		Employee employee = new Employee();
		BeanUtils.copyProperties(form, employee);
		employee.setRole(roleRepository.findOne(form.getRole()));
		employee.setManager(employeeRepository.findOne(form.getManager()));
		employee.setCreatedAt(DateTimeUtil.getCurrTimestamp());
		employee.setUpdatedAt(DateTimeUtil.getCurrTimestamp());
		employeeRepository.save(employee);
		
		// operation log
		operationLogService.logCreateOption(employee);
		
		return ResponseEntity.status(HttpStatus.CREATED).body(employee.getId());
	}
	
	@RequestMapping(value = "/{id}/edit", method = RequestMethod.GET)
	public ModelAndView edit(HttpServletRequest request, @PathVariable("id") Long id) {
		ModelAndView mv = new ModelAndView("/employees/edit");
		
		Employee employee = employeeRepository.findOne(id);
		List<Role> roles = roleRepository.findAll();
		
		mv.addObject("roles", roles);
		mv.addObject("employee", employee);
		return mv;
	}
	
	@RequestMapping(value = "/{id}", method = RequestMethod.PUT)
	public ResponseEntity<?> update(@PathVariable("id") Long id, @Valid EmployeeForm form, BindingResult result, final RedirectAttributes redirectAttr) {
		
		if (result.hasErrors()) {
			return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(result.getFieldErrors());
		}
		
		Employee employee = employeeRepository.findOne(id);
		Employee oldEmployee = new Employee();
		BeanUtils.copyProperties(employee, oldEmployee);
		
		if (employee == null) {
			return ResponseEntity.status(HttpStatus.NOT_EXTENDED).body("Employee can not found with id: " + id);
		}
		
		BeanUtils.copyProperties(form, employee);
		employee.setRole(roleRepository.findOne(form.getRole()));
		employee.setManager(employeeRepository.findOne(form.getManager()));
		employee.setUpdatedAt(DateTimeUtil.getCurrTimestamp());
		employeeRepository.save(employee);
		
		// operation log
		operationLogService.logUpdateOption(oldEmployee, employee);
		
		return ResponseEntity.status(HttpStatus.OK).body(employee.getId());
	}
	
	@RequestMapping(value="/{id}", method = RequestMethod.DELETE)
	public ModelAndView delete(@PathVariable("id") Long id, final RedirectAttributes redirectAttr) {
		
		employeeRepository.delete(id);
		
		redirectAttr.addFlashAttribute("notice", "Delete success!");
		
		return new ModelAndView("redirect:/employees");
	}
	
}