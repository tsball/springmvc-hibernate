package com.springmvc.controllers;

import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

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

import com.springmvc.forms.NotificationForm;
import com.springmvc.forms.RoleForm;
import com.springmvc.models.Notification;
import com.springmvc.repositories.AuthorityRepository;
import com.springmvc.repositories.NotificationRepository;
import com.springmvc.services.SecurityService;
import com.springmvc.utils.DateTimeUtil;
import com.springmvc.validators.RoleValidator;

@RestController
@RequestMapping("/notifications")
public class NotificationController {
	
	@Autowired NotificationRepository notificationRepository;
	@Autowired AuthorityRepository authorityRepository;
	@Autowired RoleValidator roleValidtor;
	@Autowired EntityManager entityManager;
	@Autowired SecurityService securityService;
	
	@RequestMapping(value = "send", method = RequestMethod.GET)
	public ModelAndView send(HttpServletRequest request) {
		ModelAndView mv = new ModelAndView("/notifications/send");
		
		mv.addObject("userId", securityService.findLoggedInUser().getId());
		return mv;
	}
	
	@RequestMapping(value = "received_list", method = RequestMethod.GET)
	public ModelAndView receiveList(HttpServletRequest request) {
		ModelAndView mv = new ModelAndView("/notifications/index");
		
		// page number
		String pageNumStr = request.getParameter("page");
		int pageNum = pageNumStr == null? 0 : Integer.parseInt(pageNumStr) - 1;
		
		Pageable pageable = new PageRequest(pageNum, 5, Sort.Direction.DESC, "updated_at");
		Page<Notification> page = notificationRepository.findAll(pageable);
		
		mv.addObject("page", page);
		return mv;
	}
	
	@RequestMapping(value = "sent_list", method = RequestMethod.GET)
	public ModelAndView sendList(HttpServletRequest request) {
		ModelAndView mv = new ModelAndView("/notifications/index");
		
		// page number
		String pageNumStr = request.getParameter("page");
		int pageNum = pageNumStr == null? 0 : Integer.parseInt(pageNumStr) - 1;
		
		Pageable pageable = new PageRequest(pageNum, 5, Sort.Direction.DESC, "updated_at");
		Page<Notification> page = notificationRepository.findAll(pageable);
		
		mv.addObject("page", page);
		return mv;
	}
	
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public ModelAndView show(@PathVariable("id") Long id) {
		ModelAndView mv = new ModelAndView("/notifications/show");
		
		Notification notification = notificationRepository.findOne(id);
		
		mv.addObject("notification", notification);
		return mv;
	}
	
	@RequestMapping(value = "", method = RequestMethod.POST)
	public ResponseEntity<?> create(HttpServletRequest request, @Valid NotificationForm form, BindingResult result, final RedirectAttributes redirectAttr) {
		roleValidtor.validate(form, result);
		
		if (result.hasErrors()) {
			return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(result.getFieldErrors());
		}
		
		Notification notification = new Notification();
		BeanUtils.copyProperties(form, notification);
		notification.setCreatedAt(DateTimeUtil.getCurrTimestamp());
		notification.setUpdatedAt(DateTimeUtil.getCurrTimestamp());
		notificationRepository.save(notification);
		
		return ResponseEntity.status(HttpStatus.CREATED).body(notification.getId());
	}
	
	@RequestMapping(value = "/{id}/edit", method = RequestMethod.GET)
	public ModelAndView edit(HttpServletRequest request, @PathVariable("id") Long id) {
		ModelAndView mv = new ModelAndView("/notifications/edit");
		
		Notification notification = notificationRepository.findOne(id);
		
		mv.addObject("notification", notification);
		return mv;
	}
	
	@RequestMapping(value = "/{id}", method = RequestMethod.PUT)
	public ResponseEntity<?> update(@PathVariable("id") Long id, @Valid RoleForm form, BindingResult result, final RedirectAttributes redirectAttr) {
		
		if (result.hasErrors()) {
			return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(result.getFieldErrors());
		}
				
		Notification notification = notificationRepository.findOne(id);
		BeanUtils.copyProperties(form, notification);
		notification.setUpdatedAt(DateTimeUtil.getCurrTimestamp());
		notificationRepository.save(notification);
		
		return ResponseEntity.status(HttpStatus.OK).body(notification.getId());
	}
	
	@RequestMapping(value="/{id}", method = RequestMethod.DELETE)
	public ModelAndView delete(@PathVariable("id") Long id, final RedirectAttributes redirectAttr) {
		
		notificationRepository.delete(id);
		
		redirectAttr.addFlashAttribute("notice", "Delete success!");
		
		return new ModelAndView("redirect:/notifications");
	}
	
}