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

import com.springmvc.forms.RoleForm;
import com.springmvc.models.Role;
import com.springmvc.models.RoleCode;
import com.springmvc.repositories.RoleRepository;
import com.springmvc.utils.DateTimeUtil;
import com.springmvc.validators.RoleValidator;

@RestController
@RequestMapping("/roles")
public class RoleController {
	
	@Autowired RoleRepository roleRepository;
	@Autowired RoleValidator roleValidtor;
	@Autowired EntityManager entityManager;
	
	@RequestMapping(value = "", method = RequestMethod.GET)
	public ModelAndView index(HttpServletRequest request) {
		ModelAndView mv = new ModelAndView("/roles/index");
		
		// page number
		String pageNumStr = request.getParameter("page");
		int pageNum = pageNumStr == null? 0 : Integer.parseInt(pageNumStr) - 1;
		
		Pageable pageable = new PageRequest(pageNum, 5, Sort.Direction.ASC, "name");
		Page<Role> page = roleRepository.findAll(pageable);
		
		mv.addObject("page", page);
		return mv;
	}
	
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public ModelAndView show(@PathVariable("id") Long id) {
		ModelAndView mv = new ModelAndView("/roles/show");
		
		Role role = roleRepository.findOne(id);
		
		mv.addObject("role", role);
		return mv;
	}
	
	@RequestMapping(value = "/add", method = RequestMethod.GET)
	public ModelAndView add(HttpServletRequest request) {
		ModelAndView mv = new ModelAndView("/roles/add");
		
		RoleCode[] roleCodes = RoleCode.values();
		
		mv.addObject("roleCodes", roleCodes);
		return mv;
	}
	
	@RequestMapping(value = "", method = RequestMethod.POST)
	public ResponseEntity<?> create(HttpServletRequest request, @Valid RoleForm form, BindingResult result, final RedirectAttributes redirectAttr) {
		roleValidtor.validate(form, result);
		
		if (result.hasErrors()) {
			return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(result.getFieldErrors());
		}
		
		Role role = new Role();
		BeanUtils.copyProperties(form, role);
		role.setCreatedAt(DateTimeUtil.getCurrTimestamp());
		role.setUpdatedAt(DateTimeUtil.getCurrTimestamp());
		roleRepository.save(role);
		
		return ResponseEntity.status(HttpStatus.CREATED).body(role.getId());
	}
	
	@RequestMapping(value = "/{id}/edit", method = RequestMethod.GET)
	public ModelAndView edit(HttpServletRequest request, @PathVariable("id") Long id) {
		ModelAndView mv = new ModelAndView("/roles/edit");
		
		Role role = roleRepository.findOne(id);
		RoleCode[] roleCodes = RoleCode.values();
		
		mv.addObject("roleCodes", roleCodes);
		mv.addObject("role", role);
		return mv;
	}
	
	@RequestMapping(value = "/{id}", method = RequestMethod.PUT)
	public ResponseEntity<?> update(@PathVariable("id") Long id, @Valid RoleForm form, BindingResult result, final RedirectAttributes redirectAttr) {
		
		if (result.hasErrors()) {
			return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(result.getFieldErrors());
		}
		
		Role role = roleRepository.findOne(id);
		BeanUtils.copyProperties(form, role);
		role.setUpdatedAt(DateTimeUtil.getCurrTimestamp());
		roleRepository.save(role);
		
		return ResponseEntity.status(HttpStatus.OK).body(role.getId());
	}
	
	@RequestMapping(value="/{id}", method = RequestMethod.DELETE)
	public ModelAndView delete(@PathVariable("id") Long id, final RedirectAttributes redirectAttr) {
		
		roleRepository.delete(id);
		
		redirectAttr.addFlashAttribute("notice", "Delete success!");
		
		return new ModelAndView("redirect:/roles");
	}
	
}