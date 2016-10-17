package com.springmvc.validators;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.springmvc.forms.RoleForm;
import com.springmvc.models.User;
import com.springmvc.repositories.RoleRepository;

@Component
public class RoleValidator implements Validator {
    @Autowired RoleRepository roleRepository;

    @Override
    public boolean supports(Class<?> aClass) {
        return User.class.equals(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
    	RoleForm role = (RoleForm) o;

        if (roleRepository.findByName(role.getName()) != null) {
            errors.rejectValue("name", "Duplicate.role.name");
        }
    }
}
