package com.springmvc.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.springmvc.models.User;
import com.springmvc.services.SecurityService;
import com.springmvc.services.UserService;
import com.springmvc.validators.UserValidator;

@RestController
@RequestMapping("/auth")
public class AuthController {
	
	@Autowired UserService userService;
    @Autowired SecurityService securityService;
    @Autowired UserValidator userValidator;
	
    @RequestMapping(value = "/registration", method = RequestMethod.GET)
    public ModelAndView registration() {
    	ModelAndView mv = new ModelAndView("auth/registration");
    	
        mv.addObject("userForm", new User());

        return mv;
    }

    @RequestMapping(value = "/registration", method = RequestMethod.POST)
    public ModelAndView registration(@ModelAttribute("userForm") User userForm, BindingResult bindingResult) {
        userValidator.validate(userForm, bindingResult);

        if (bindingResult.hasErrors()) {
            return new ModelAndView("auth/registration");
        }

        userService.save(userForm);

        securityService.autologin(userForm.getUsername(), userForm.getPasswordConfirm());

        return new ModelAndView("redirect:/");
    }
	
}