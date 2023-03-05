package com.baeldung.lss.web.controller;

import com.baeldung.lss.persistence.UserRepository;
import com.baeldung.lss.web.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;

@Controller
final class RegistrationController {

    @Autowired
    private UserRepository userRepository;

    @RequestMapping(value = "signup")
    public ModelAndView registrationForm(){
        return new ModelAndView("registrationPage", "user", new User());
    }

    @RequestMapping(value="user/register")
    public ModelAndView registerUser(@Valid final User user, final BindingResult result) {
        if (result.hasErrors()){
            return new ModelAndView("registrationPage", "user", user);
        }
        userRepository.save(user);

        return new ModelAndView("redirect:/login");
    }
}
