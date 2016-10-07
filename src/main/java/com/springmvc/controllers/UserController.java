package com.springmvc.controllers;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import javax.validation.Valid;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.springmvc.forms.UserEditForm;
import com.springmvc.forms.UserForm;
import com.springmvc.models.Role;
import com.springmvc.models.User;
import com.springmvc.repositories.RoleRepository;
import com.springmvc.repositories.UserRepository;
import com.springmvc.utils.DateTimeUtil;
import com.springmvc.validators.UserValidator;

@RestController
@RequestMapping("/users")
public class UserController {
	
	@Autowired UserRepository userRepository;
	@Autowired RoleRepository roleRepository;
	@Autowired BCryptPasswordEncoder bCryptPasswordEncoder;
	@Autowired UserValidator userValidtor;
	
	@RequestMapping(value = "", method = RequestMethod.GET)
	public ModelAndView listAllUsers() {
		ModelAndView mv = new ModelAndView("/users/index");
		
		Iterable<User> userList = userRepository.findAll(new Sort(Sort.Direction.ASC, "username"));
		
		mv.addObject("userList", userList);
		return mv;
	}
	
	@RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ModelAndView show(@PathVariable("id") Long id) {
		ModelAndView mv = new ModelAndView("/users/show");
		
		User user = userRepository.findOne(id);
		
		mv.addObject("user", user);
		return mv;
	}
	
	@RequestMapping(value = "/add", method = RequestMethod.GET)
	public ModelAndView add(HttpServletRequest request) {
		ModelAndView mv = new ModelAndView("/users/add", "user", new UserForm());
		
		Iterable<Role> roles = roleRepository.findAll(new Sort(Sort.Direction.ASC, "name"));
		
		mv.addObject("roles", roles);
		return mv;
	}
	
	@RequestMapping(value = "", method = RequestMethod.POST)
	public ModelAndView create(HttpServletRequest request, @Valid @ModelAttribute("user") UserForm form, BindingResult result, final RedirectAttributes redirectAttr) {
		userValidtor.validate(form, result);
		
		if (result.hasErrors()) {
			redirectAttr.addFlashAttribute("notice", result.getFieldErrors());
			return new ModelAndView("/users/add");
		}
		
		Iterable<Role> roles = roleRepository.findAll(new Sort(Sort.Direction.ASC, "name"));
		
		// add a user
		User user = new User();
		BeanUtils.copyProperties(form, user);
		user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
		user.setCreatedAt(DateTimeUtil.getCurrTimestamp());
		user.setUpdatedAt(DateTimeUtil.getCurrTimestamp());
		user.setRoles(Sets.newHashSet(roles));
		userRepository.save(user);
		
		redirectAttr.addFlashAttribute("notice", "Created success!");
		
		return new ModelAndView("redirect:/users");
	}
	
	@RequestMapping(value = "/{id}/edit", method = RequestMethod.GET)
	public ModelAndView edit(HttpServletRequest request, @PathVariable("id") Long id) {
		ModelAndView mv = new ModelAndView("/users/edit");
		
		User user = userRepository.findOne(id);
		Iterable<Role> roles = roleRepository.findAll();
		
		mv.addObject("user", user);
		mv.addObject("roles", roles);
		return mv;
	}
	
	@RequestMapping(value = "/{id}", method = RequestMethod.PUT)
	public ModelAndView update(@PathVariable("id") Long id, @Valid UserEditForm form, BindingResult result, final RedirectAttributes redirectAttr) {
		
		if (result.hasErrors()) {
			redirectAttr.addFlashAttribute("alert", result.getFieldErrors());
			return new ModelAndView("redirect:/users/" + id + "/edit");
		}
		
		Iterable<Role> roles = roleRepository.findAll(form.getRoles());
		
		User user = userRepository.findOne(id);
		BeanUtils.copyProperties(form, user);
		user.setUpdatedAt(DateTimeUtil.getCurrTimestamp());
		user.setRoles(Sets.newHashSet(roles));
		userRepository.save(user);
		
		redirectAttr.addFlashAttribute("notice", "更新成功!");
		return new ModelAndView("redirect:/users");
	}
	
	@RequestMapping(value="/{id}", method = RequestMethod.DELETE)
	public ModelAndView delete(@PathVariable("id") Long id, final RedirectAttributes redirectAttr) {
		
		userRepository.delete(id);
		
		redirectAttr.addFlashAttribute("notice", "Delete success!");
		
		return new ModelAndView("redirect:/users");
	}
	
	@RequestMapping("/save-fail-test")
	@ResponseBody
	@Transactional
	public Map<String, Object> saveFailTest(HttpServletRequest request) throws Exception {
		// add a user
		User user = new User();
		user.setUsername("lily");
		user.setPassword("password");
		user.setCreatedAt(DateTimeUtil.getCurrTimestamp());
		user.setUpdatedAt(DateTimeUtil.getCurrTimestamp());
		userRepository.save(user);
		
		if (user.getId() > 0) throw new RuntimeException("Please rollback!");
		
		return Maps.newHashMap();
	}
	
}