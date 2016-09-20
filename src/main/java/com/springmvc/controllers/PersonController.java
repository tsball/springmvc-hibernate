package com.springmvc.controllers;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.activiti.engine.task.Task;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.springmvc.forms.PersonForm;
import com.springmvc.forms.UserForm;
import com.springmvc.models.Person;
import com.springmvc.repositories.PersonRepository;
import com.springmvc.representations.TaskRepresentation;
import com.springmvc.services.ActivitiTaskService;
import com.springmvc.utils.DateTimeUtil;

@RestController
@RequestMapping("/person")
public class PersonController {
	
	@Autowired PersonRepository personRepository;
	@Autowired ActivitiTaskService activitiTaskService;
	
	@RequestMapping(value = "", method = RequestMethod.GET)
	public ModelAndView listAllUsers() {
		ModelAndView mv = new ModelAndView("/person/index");
		
		Iterable<Person> people = personRepository.findAll();
		
		mv.addObject("people", people);
		return mv;
	}
	
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public ModelAndView show(@PathVariable("id") Long id) {
		ModelAndView mv = new ModelAndView("/person/show");
		
		Person person = personRepository.findOne(id);
		
		mv.addObject("person", person);
		return mv;
	}
	
	@RequestMapping(value="/{id}/tasks", method= RequestMethod.GET)
    public ModelAndView tasks(@PathVariable("id") Long id) {
		ModelAndView mv = new ModelAndView("/person/tasks");
		
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
		
		return new ModelAndView("redirect:/person/" + person.getId() + "/tasks");
	}
	
	@RequestMapping(value = "/add", method = RequestMethod.GET)
	public ModelAndView add(HttpServletRequest request) {
		ModelAndView mv = new ModelAndView("/person/add", "person", new Person());
		
		return mv;
	}
	
	@RequestMapping(value = "", method = RequestMethod.POST)
	public ModelAndView create(HttpServletRequest request, @Valid @ModelAttribute("person") PersonForm form, BindingResult result, final RedirectAttributes redirectAttr) {
		
		if (result.hasErrors()) {
			redirectAttr.addFlashAttribute("notice", result.getFieldErrors());
			return new ModelAndView("/person/add");
		}
		
		// add a user
		Person user = new Person();
		BeanUtils.copyProperties(form, user);
		user.setCreatedAt(DateTimeUtil.getCurrTimestamp());
		user.setUpdatedAt(DateTimeUtil.getCurrTimestamp());
		personRepository.save(user);
		
		redirectAttr.addFlashAttribute("notice", "Created success!");
		
		return new ModelAndView("redirect:/person");
	}
	
	@RequestMapping(value = "/{id}/edit", method = RequestMethod.GET)
	public ModelAndView edit(HttpServletRequest request, @PathVariable("id") Long id) {
		ModelAndView mv = new ModelAndView("/person/edit");
		
		Person person = personRepository.findOne(id);
		
		mv.addObject("person", person);
		return mv;
	}
	
	@RequestMapping(value = "/{id}", method = RequestMethod.PUT)
	public ModelAndView update(@PathVariable("id") Long id, @Valid UserForm form, BindingResult result, final RedirectAttributes redirectAttr) {
		
		if (result.hasErrors()) {
			redirectAttr.addFlashAttribute("alert", result.getFieldErrors());
			return new ModelAndView("redirect:/person/" + id + "/edit");
		}
		
		Person person = personRepository.findOne(id);
		BeanUtils.copyProperties(form, person);
		person.setUpdatedAt(DateTimeUtil.getCurrTimestamp());
		personRepository.save(person);
		
		redirectAttr.addFlashAttribute("notice", "Update successed!");
		return new ModelAndView("redirect:/person");
	}
	
	@RequestMapping(value="/{id}", method = RequestMethod.DELETE)
	public ModelAndView delete(@PathVariable("id") Long id, final RedirectAttributes redirectAttr) {
		
		personRepository.delete(id);
		
		redirectAttr.addFlashAttribute("notice", "Delete success!");
		
		return new ModelAndView("redirect:/person");
	}
	
}