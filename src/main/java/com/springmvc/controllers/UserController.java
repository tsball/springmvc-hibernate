package com.springmvc.controllers;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import javax.validation.Valid;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
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
import com.springmvc.daos.UserDao;
import com.springmvc.forms.UserForm;
import com.springmvc.models.User;
import com.springmvc.utils.DateTimeUtil;

@RestController
@RequestMapping("/user")
public class UserController {
	
	@Autowired UserDao userDao;
	
	@RequestMapping(value = "", method = RequestMethod.GET)
	public ModelAndView listAllUsers() {
		ModelAndView mv = new ModelAndView("/user/index");
		
		Iterable<User> userList = userDao.findAll();
		
		mv.addObject("userList", userList);
		return mv;
	}
	
	@RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ModelAndView show(@PathVariable("id") Long id) {
		ModelAndView mv = new ModelAndView("/user/show");
		
		User user = userDao.findOne(id);
		
		mv.addObject("user", user);
		return mv;
	}
	
	@RequestMapping(value = "/add", method = RequestMethod.GET)
	public ModelAndView add(HttpServletRequest request) {
		ModelAndView mv = new ModelAndView("/user/add", "user", new User());
		
		return mv;
	}
	
	@RequestMapping(value = "", method = RequestMethod.POST)
	public ModelAndView create(HttpServletRequest request, @Valid @ModelAttribute("user") UserForm form, BindingResult result, final RedirectAttributes redirectAttr) {
		
		if (result.hasErrors()) {
			redirectAttr.addFlashAttribute("notice", result.getFieldErrors());
			return new ModelAndView("/user/add");
		}
		
		// encode the password in the form
		// PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    	// form.setPassword(passwordEncoder.encode(form.getPassword()));
		
		// add a user
		User user = new User();
		BeanUtils.copyProperties(form, user);
		user.setCreatedAt(DateTimeUtil.getCurrTimestamp());
		user.setUpdatedAt(DateTimeUtil.getCurrTimestamp());
		userDao.save(user);
		
		redirectAttr.addFlashAttribute("notice", "Created success!");
		
		return new ModelAndView("redirect:/user");
	}
	
	@RequestMapping(value = "/{id}/edit", method = RequestMethod.GET)
	public ModelAndView edit(HttpServletRequest request, @PathVariable("id") Long id) {
		ModelAndView mv = new ModelAndView("/user/edit");
		
		User user = userDao.findOne(id);
		
		mv.addObject("user", user);
		return mv;
	}
	
	@RequestMapping(value = "/{id}", method = RequestMethod.PUT)
	public ModelAndView update(@PathVariable("id") Long id, @Valid UserForm form, BindingResult result, final RedirectAttributes redirectAttr) {
		
		if (result.hasErrors()) {
			redirectAttr.addFlashAttribute("alert", result.getFieldErrors());
			return new ModelAndView("redirect:/user/" + id + "/edit");
		}
		
		User user = userDao.findOne(id);
		BeanUtils.copyProperties(form, user);
		user.setUpdatedAt(DateTimeUtil.getCurrTimestamp());
		userDao.save(user);
		
		redirectAttr.addFlashAttribute("notice", "更新成功!");
		return new ModelAndView("redirect:/user");
	}
	
	@RequestMapping(value="/{id}", method = RequestMethod.DELETE)
	public ModelAndView delete(@PathVariable("id") Long id, final RedirectAttributes redirectAttr) {
		
		userDao.delete(id);
		
		redirectAttr.addFlashAttribute("notice", "Delete success!");
		
		return new ModelAndView("redirect:/user");
	}
	
	@RequestMapping("/save-fail-test")
	@ResponseBody
	@Transactional
	public Map<String, Object> saveFailTest(HttpServletRequest request) throws Exception {
		// add a user
		User user = new User();
		user.setName("lily");
		user.setNickname("Lily");
		user.setPassword("password");
		user.setCreatedAt(DateTimeUtil.getCurrTimestamp());
		user.setUpdatedAt(DateTimeUtil.getCurrTimestamp());
		userDao.save(user);
		
		if (user.getId() > 0) throw new RuntimeException("Please rollback!");
		
		return Maps.newHashMap();
	}
	
}