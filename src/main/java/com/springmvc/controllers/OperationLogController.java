package com.springmvc.controllers;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.springmvc.models.OperationLog;
import com.springmvc.repositories.OperationLogRepository;

@RestController
@RequestMapping("/operation_logs")
public class OperationLogController {
	
	@Autowired OperationLogRepository operationLogRepository;
	
	@RequestMapping(value = "", method = RequestMethod.GET)
	public ModelAndView index(HttpServletRequest request) {
		ModelAndView mv = new ModelAndView("/operation_logs/index");
		
		// page number
		String pageNumStr = request.getParameter("page");
		int pageNum = pageNumStr == null? 0 : Integer.parseInt(pageNumStr) - 1;
		
		Pageable pageable = new PageRequest(pageNum, 5);
		Page<OperationLog> page = operationLogRepository.findAll(pageable);
		
		mv.addObject("page", page);
		return mv;
	}
	
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public ModelAndView show(@PathVariable("id") Long id) {
		ModelAndView mv = new ModelAndView("/operation_logs/show");
		
		OperationLog operationLog = operationLogRepository.findOne(id);
		
		mv.addObject("operationLog", operationLog);
		return mv;
	}
	
	@RequestMapping(value="/{id}", method = RequestMethod.DELETE)
	public ModelAndView delete(@PathVariable("id") Long id, final RedirectAttributes redirectAttr) {
		
		operationLogRepository.delete(id);
		
		redirectAttr.addFlashAttribute("notice", "Delete success!");
		
		return new ModelAndView("redirect:/operation_logs");
	}
	
}