package com.springmvc.validators;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.springmvc.forms.UserForm;
import com.springmvc.models.User;
import com.springmvc.repositories.UserRepository;

@Component
public class UserValidator implements Validator {
    @Autowired UserRepository userRepository;

    @Override
    public boolean supports(Class<?> aClass) {
        return User.class.equals(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
    	UserForm user = (UserForm) o;

        // ValidationUtils.rejectIfEmptyOrWhitespace(errors, "username", "NotEmpty");
        if (userRepository.findByUsername(user.getUsername()) != null) {
            errors.rejectValue("username", "Duplicate.user.username");
        }
        
        if (!user.getPassword().equals(user.getPasswordConfirm())) {
            errors.rejectValue("passwordConfirm", "Diff.user.passwordConfirm");
        }
    }
}
