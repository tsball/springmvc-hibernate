package com.springmvc.controllers;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.activiti.engine.task.Task;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.springmvc.forms.PersonForm;
import com.springmvc.models.Person;
import com.springmvc.repositories.PersonRepository;
import com.springmvc.representations.TaskRepresentation;
import com.springmvc.services.ActivitiTaskService;
import com.springmvc.services.OperationLogService;
import com.springmvc.utils.DateTimeUtil;

@RestController
@RequestMapping("/people")
public class PersonController {
	
	@Autowired PersonRepository personRepository;
	@Autowired ActivitiTaskService activitiTaskService;
	@Autowired OperationLogService operationLogService;
	
	@RequestMapping(value = "", method = RequestMethod.GET)
	public ModelAndView listAllUsers() {
		ModelAndView mv = new ModelAndView("/people/index");
		
		Pageable pageable = new PageRequest(0, 10, Sort.Direction.ASC, "name");
		Page<Person> page = personRepository.findList(pageable);
		
		mv.addObject("page", page);		
		return mv;
	}
	
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public ModelAndView show(@PathVariable("id") Long id) {
		ModelAndView mv = new ModelAndView("/people/show");
		
		Person person = personRepository.findOne(id);
		
		mv.addObject("person", person);
		return mv;
	}
	
	@RequestMapping(value="/{id}/tasks", method= RequestMethod.GET)
    public ModelAndView tasks(@PathVariable("id") Long id) {
		ModelAndView mv = new ModelAndView("/people/tasks");
		
		Person person = personRepository.findOne(id);
		
        List<Task> tasks = activitiTaskService.getTasks(id);
        List<TaskRepresentation> taskRepresentations = new ArrayList<TaskRepresentation>();
        for (Task task : tasks) {
        	taskRepresentations.add(new TaskRepresentation(task.getId(), task.getName()));
        }
        
        mv.addObject("person", person);
        mv.addObject("taskRepresentations", taskRepresentations);
		return mv;
    }
	
	@RequestMapping(value="/{id}/leaves", method= RequestMethod.GET)
    public ModelAndView leaves(@PathVariable("id") Long id) {
		ModelAndView mv = new ModelAndView("/people/leaves");
		
		Person person = personRepository.findOne(id);
		
        List<Task> tasks = activitiTaskService.getTasks(id);
        List<TaskRepresentation> taskRepresentations = new ArrayList<TaskRepresentation>();
        for (Task task : tasks) {
        	taskRepresentations.add(new TaskRepresentation(task.getId(), task.getName()));
        }
        
        mv.addObject("person", person);
        mv.addObject("taskRepresentations", taskRepresentations);
		return mv;
    }
	
	@RequestMapping(value="/{id}/tasks", method = RequestMethod.POST)
	public ModelAndView startTask(@PathVariable("id") Long id, final RedirectAttributes redirectAttr) {
		
		Person person = personRepository.findOne(id);
		
		activitiTaskService.startProcess(person);
		
		redirectAttr.addFlashAttribute("notice", "Start task successed!");
		
		return new ModelAndView("redirect:/people/" + person.getId() + "/tasks");
	}
	
	@RequestMapping(value = "/add", method = RequestMethod.GET)
	public ModelAndView add(HttpServletRequest request) {
		ModelAndView mv = new ModelAndView("/people/add", "person", new Person());
		
		return mv;
	}
	
	@RequestMapping(value = "", method = RequestMethod.POST)
	public ResponseEntity<?> create(HttpServletRequest request, @Valid PersonForm form, BindingResult result, final RedirectAttributes redirectAttr) {
		
		if (result.hasErrors()) {
			return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(result.getFieldErrors());
		}
		
		Person person = new Person();
		BeanUtils.copyProperties(form, person);
		person.setCreatedAt(DateTimeUtil.getCurrTimestamp());
		person.setUpdatedAt(DateTimeUtil.getCurrTimestamp());
		personRepository.save(person);
		
		// operation log
		operationLogService.logCreateOption(person);
		
		return ResponseEntity.status(HttpStatus.CREATED).body(person.getId());
	}
	
	@RequestMapping(value = "/{id}/edit", method = RequestMethod.GET)
	public ModelAndView edit(HttpServletRequest request, @PathVariable("id") Long id) {
		ModelAndView mv = new ModelAndView("/people/edit");
		
		Person person = personRepository.findOne(id);
		
		mv.addObject("person", person);
		return mv;
	}
	
	@RequestMapping(value = "/{id}", method = RequestMethod.PUT)
	public ResponseEntity<?> update(@PathVariable("id") Long id, @Valid PersonForm form, BindingResult result, final RedirectAttributes redirectAttr) {
		
		if (result.hasErrors()) {
			return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(result.getFieldErrors());
		}
		
		Person person = personRepository.findOne(id);
		Person oldPerson = new Person();
		BeanUtils.copyProperties(person, oldPerson);
		
		if (person == null) {
			return ResponseEntity.status(HttpStatus.NOT_EXTENDED).body("Person can not found with id: " + id);
		}
		
		BeanUtils.copyProperties(form, person);
		person.setUpdatedAt(DateTimeUtil.getCurrTimestamp());
		personRepository.save(person);
		
		// operation log
		operationLogService.logUpdateOption(oldPerson, person);
		
		return ResponseEntity.status(HttpStatus.OK).body(person.getId());
	}
	
	@RequestMapping(value="/{id}", method = RequestMethod.DELETE)
	public ModelAndView delete(@PathVariable("id") Long id, final RedirectAttributes redirectAttr) {
		
		personRepository.delete(id);
		
		redirectAttr.addFlashAttribute("notice", "Delete success!");
		
		return new ModelAndView("redirect:/people");
	}
	
}