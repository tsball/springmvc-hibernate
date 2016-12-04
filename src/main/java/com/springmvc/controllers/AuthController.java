package com.springmvc.controllers;

import javax.validation.Valid;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.google.common.collect.Sets;
import com.springmvc.forms.UserForm;
import com.springmvc.models.Employee;
import com.springmvc.models.Role;
import com.springmvc.models.User;
import com.springmvc.repositories.EmployeeRepository;
import com.springmvc.repositories.RoleRepository;
import com.springmvc.repositories.UserRepository;
import com.springmvc.services.ISecurityService;
import com.springmvc.utils.DateTimeUtil;
import com.springmvc.validators.UserValidator;

@RestController
@RequestMapping("/auth")
public class AuthController {
	
    @Autowired ISecurityService securityService;
    @Autowired UserValidator userValidator;
    @Autowired UserRepository userRepository;
    @Autowired RoleRepository roleRepository;
    @Autowired EmployeeRepository employeeRepository;
    @Autowired BCryptPasswordEncoder bCryptPasswordEncoder;
    @Autowired UserValidator userValidtor;
	
    @RequestMapping(value = "/registration", method = RequestMethod.GET)
    public ModelAndView registration() {
    	ModelAndView mv = new ModelAndView("auth/registration");
    	
    	Iterable<Role> roles = roleRepository.findAll(new Sort(Sort.Direction.ASC, "name"));
		Iterable<Employee> employees = employeeRepository.findAll();
		
		mv.addObject("roles", roles);
		mv.addObject("employees", employees);
        mv.addObject("userForm", new UserForm());

        return mv;
    }

    @RequestMapping(value = "/registration", method = RequestMethod.POST)
    public ResponseEntity<?> registration(@Valid UserForm form, BindingResult result) {
    	userValidtor.validate(form, result);
		
		if (result.hasErrors()) {
			return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(result.getFieldErrors());
		}
		
		Iterable<Role> roles = roleRepository.findAll(new Sort(Sort.Direction.ASC, "name"));
		
		// add a user
		User user = new User();
		BeanUtils.copyProperties(form, user);
		user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
		user.setCreatedAt(DateTimeUtil.getCurrTimestamp());
		user.setUpdatedAt(DateTimeUtil.getCurrTimestamp());
		user.setRoles(Sets.newHashSet(roles));
		user.setEmployee(employeeRepository.findOne(form.getEmployee()));
		userRepository.save(user);
		
		// auto log in
		securityService.autologin(form.getUsername(), form.getPassword());
		
		return ResponseEntity.status(HttpStatus.CREATED).body(user.getId());
    }
	
}