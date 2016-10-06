package com.springmvc.controllers;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.springmvc.models.User;
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
    @Autowired BCryptPasswordEncoder bCryptPasswordEncoder;
	
    @RequestMapping(value = "/registration", method = RequestMethod.GET)
    public ModelAndView registration() {
    	ModelAndView mv = new ModelAndView("auth/registration");
    	
        mv.addObject("userForm", new User());

        return mv;
    }

    @RequestMapping(value = "/registration", method = RequestMethod.POST)
    public ModelAndView registration(@ModelAttribute("userForm") User form, BindingResult bindingResult) {
        userValidator.validate(form, bindingResult);

        if (bindingResult.hasErrors()) {
            return new ModelAndView("auth/registration");
        }

        User user = new User();
        BeanUtils.copyProperties(form, user);
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        user.setCreatedAt(DateTimeUtil.getCurrTimestamp());
        user.setUpdatedAt(DateTimeUtil.getCurrTimestamp());
        userRepository.save(user);

        securityService.autologin(form.getUsername(), form.getPassword());

        return new ModelAndView("redirect:/");
    }
	
}