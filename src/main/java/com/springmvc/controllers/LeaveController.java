package com.springmvc.controllers;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.activiti.engine.IdentityService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.runtime.ProcessInstance;
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

import com.google.common.collect.Maps;
import com.springmvc.constants.FlowVariableConst;
import com.springmvc.forms.LeaveForm;
import com.springmvc.models.Leave;
import com.springmvc.repositories.LeaveRepository;
import com.springmvc.services.ActivitiTaskService;
import com.springmvc.utils.DateTimeUtil;

@RestController
@RequestMapping("/leave")
public class LeaveController {
	
	@Autowired LeaveRepository leaveRepository;
	@Autowired ActivitiTaskService activitiTaskService;
	@Autowired IdentityService identityService;
	@Autowired RuntimeService runtimeService;
	@Autowired TaskService taskService;
	
	@RequestMapping(value = "", method = RequestMethod.GET)
	public ModelAndView listAllUsers() {
		ModelAndView mv = new ModelAndView("/leave/index");
		
		Iterable<Leave> leaves = leaveRepository.findAll();
		
		mv.addObject("leaves", leaves);
		return mv;
	}
	
	@RequestMapping(value = "/add", method = RequestMethod.GET)
	public ModelAndView add(HttpServletRequest request) {
		ModelAndView mv = new ModelAndView("/leave/add", "leave", new Leave());
		
		return mv;
	}
	
	@RequestMapping(value = "", method = RequestMethod.POST)
	public ModelAndView create(HttpServletRequest request, @Valid @ModelAttribute("leave") LeaveForm form, BindingResult result, final RedirectAttributes redirectAttr) {
		
		if (result.hasErrors()) {
			redirectAttr.addFlashAttribute("notice", result.getFieldErrors());
			return new ModelAndView("/leave/add");
		}
		
		// save a leave
		Leave leave = new Leave();
		BeanUtils.copyProperties(form, leave);
		leave.setCreatedAt(DateTimeUtil.getCurrTimestamp());
		leave.setUpdatedAt(DateTimeUtil.getCurrTimestamp());
		leaveRepository.save(leave);
		
		//用来设置启动流程的人员ID，引擎会自动把用户ID保存到activiti:initiator中
		leave.setPersonId(1L); // set current session id
        identityService.setAuthenticatedUserId(leave.getPersonId().toString());
        
        String businessKey = leave.getId().toString();
        Map<String, Object> variables = Maps.newHashMap();
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("leave", businessKey, variables);
        String processInstanceId = processInstance.getId();
        leave.setProcessInstanceId(processInstanceId);
        leaveRepository.save(leave);
		
		redirectAttr.addFlashAttribute("notice", "Created success!");
		
		return new ModelAndView("redirect:/leave");
	}
	
	@RequestMapping(value = "/{id}/agree", method = RequestMethod.PATCH)
	public ModelAndView agree(@PathVariable("id") Long id, final RedirectAttributes redirectAttr) {
		Leave leave = leaveRepository.findOne(id);
		
		// 部门领导(这里并不准确，这里根据候选人去筛选第一个待审核的task, 准确应该根据process instance id去查找)
		// Task task = taskService.createTaskQuery().taskCandidateGroup(RoleTypeConst.DEPARTMENT_LEADER).singleResult();
		Task task = taskService.createTaskQuery().processInstanceId(leave.getProcessInstanceId()).singleResult();
		String userId = "1"; // current user session id
		taskService.claim(task.getId(), userId);
		
		Map<String, Object> variables = Maps.newHashMap();
		variables.put(FlowVariableConst.AGREE, true);
		taskService.complete(task.getId(), variables);
		
		redirectAttr.addFlashAttribute("notice", "Update successed!");
		return new ModelAndView("redirect:/leave");
	}
	
	@RequestMapping(value = "/{id}/reject", method = RequestMethod.PATCH)
	public ModelAndView reject(@PathVariable("id") Long id, final RedirectAttributes redirectAttr) {
		Leave leave = leaveRepository.findOne(id);
		
		// 部门领导
		// Task task = taskService.createTaskQuery().taskCandidateGroup(RoleTypeConst.DEPARTMENT_LEADER).singleResult();
		Task task = taskService.createTaskQuery().processInstanceId(leave.getProcessInstanceId()).singleResult();
		String userId = "1"; // current user session id
		taskService.claim(task.getId(), userId);
		
		Map<String, Object> variables = Maps.newHashMap();
		variables.put(FlowVariableConst.AGREE, false);
		taskService.complete(task.getId(), variables);
		
		redirectAttr.addFlashAttribute("notice", "Update successed!");
		return new ModelAndView("redirect:/leave");
	}
	
	@RequestMapping(value="/{id}", method = RequestMethod.DELETE)
	public ModelAndView delete(@PathVariable("id") Long id, final RedirectAttributes redirectAttr) {
		
		leaveRepository.delete(id);
		
		redirectAttr.addFlashAttribute("notice", "Delete success!");
		
		return new ModelAndView("redirect:/leave");
	}
	
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public ModelAndView show(@PathVariable("id") Long id) {
		ModelAndView mv = new ModelAndView("/leave/show");
		
		Leave leave = leaveRepository.findOne(id);
		
		ProcessInstance processInstance = runtimeService.createProcessInstanceQuery()
				.processInstanceBusinessKey(leave.getId().toString()).singleResult();
		
		mv.addObject("leave", leave);
		mv.addObject("processInstance", processInstance);
		return mv;
	}
	
}