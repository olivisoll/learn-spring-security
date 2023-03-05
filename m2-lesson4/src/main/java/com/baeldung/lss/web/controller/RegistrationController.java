package com.baeldung.lss.web.controller;

import com.baeldung.lss.model.PasswordResetToken;
import com.baeldung.lss.model.User;
import com.baeldung.lss.model.VerificationToken;
import com.baeldung.lss.registration.OnRegistrationCompleteEvent;
import com.baeldung.lss.service.IUserService;
import com.baeldung.lss.validation.EmailExistsException;
import com.google.common.collect.ImmutableMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.core.env.Environment;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Calendar;
import java.util.UUID;

@Controller
class RegistrationController {

    @Autowired
    private ApplicationEventPublisher eventPublisher;

    @Autowired
    private IUserService userService;

    @Autowired
    private Environment env;

    @Autowired
    private JavaMailSender mailSender;

    @RequestMapping(value = "signup")
    public ModelAndView registrationForm() {
        return new ModelAndView("registrationPage", "user", new User());
    }

    @RequestMapping(value = "user/register")
    public ModelAndView registerUser(@Valid final User user, final BindingResult result, final HttpServletRequest request) {
        if (result.hasErrors()) {
            return new ModelAndView("registrationPage", "user", user);
        }
        try {
            final User registered = userService.registerNewUser(user);

            final String appUrl = "http://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
            eventPublisher.publishEvent(new OnRegistrationCompleteEvent(registered, appUrl));
        } catch (final EmailExistsException e) {
            result.addError(new FieldError("user", "email", e.getMessage()));
            return new ModelAndView("registrationPage", "user", user);
        }
        return new ModelAndView("loginPage");
    }

    @RequestMapping(value = "/registrationConfirm")
    public ModelAndView confirmRegistration(final Model model, @RequestParam("token") final String token, final RedirectAttributes redirectAttributes) {
        final VerificationToken verificationToken = userService.getVerificationToken(token);
        if (verificationToken == null) {
            redirectAttributes.addFlashAttribute("errorMessage", "Invalid account confirmation token.");
            return new ModelAndView("redirect:/login");
        }

        final User user = verificationToken.getUser();
        final Calendar cal = Calendar.getInstance();
        if ((verificationToken.getExpiryDate().getTime() - cal.getTime().getTime()) <= 0) {
            redirectAttributes.addFlashAttribute("errorMessage", "Your registration token has expired. Please register again.");
            return new ModelAndView("redirect:/login");
        }

        user.setEnabled(true);
        userService.saveRegisteredUser(user);
        redirectAttributes.addFlashAttribute("message", "Your account verified successfully");
        return new ModelAndView("redirect:/login");
    }

    @RequestMapping(value = "user/resetPassword", method = RequestMethod.POST)
    @ResponseBody
    public ModelAndView resetPassword(final HttpServletRequest request, @RequestParam("email") final String userEmail,
                                      final RedirectAttributes redirectAttributes) {
        final User user = userService.findUserByEmail(userEmail);
        if (user != null) {
            final String token = UUID.randomUUID().toString();
            final PasswordResetToken myToken = new PasswordResetToken(token, user);
            userService.createPasswordResetTokenForUser(user, token);
            final String appUrl = "http://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
            final SimpleMailMessage email = constructResetTokenEmail(appUrl, token, user);
            mailSender.send(email);
        }
        redirectAttributes.addFlashAttribute("message",
                "You should receive a Password Reset Email shortly");
        return new ModelAndView("redirect:/login");
    }

    @RequestMapping(value = "user/changePassword", method = RequestMethod.GET)
    public ModelAndView showChangePasswordPage(@RequestParam("id") final long id, @RequestParam("token") final String token, final RedirectAttributes redirectAttributes) {
        final PasswordResetToken passToken = userService.getPasswordResetToken(token);
        final ModelAndView view = new ModelAndView("resetPassword");
        view.addObject("token", token);
        return view;
    }


    @RequestMapping(value = "user/savePassword", method = RequestMethod.POST)
    @ResponseBody
    public ModelAndView savePassword(
            @RequestParam("password") final String password,
            @RequestParam("passwordConfirmation") final String passwordConfirmation,
            @RequestParam("token") final String token,
            final RedirectAttributes redirectAttributes) {
        if (!password.equals(passwordConfirmation)) {
            return new ModelAndView("resetPassword", ImmutableMap.of("errorMessage", "Passwords do not match"));
        }
        final PasswordResetToken p = userService.getPasswordResetToken(token);
        if (p == null) {
            redirectAttributes.addFlashAttribute("message", "Invalid token");
        } else {
            final User user = p.getUser();
            if (user == null) {
                redirectAttributes.addFlashAttribute("message", "Unknown user");
            } else {
                userService.changeUserPassword(user, password);
                redirectAttributes.addFlashAttribute("message", "Password reset successfully");
            }
        }
        return new ModelAndView("redirect:/login");
    }

    // NON-API

    private SimpleMailMessage constructResetTokenEmail(final String contextPath, final String token, final User user) {
        final String url = contextPath + "/user/changePassword?id=" + user.getId() + "&token=" + token;
        final SimpleMailMessage email = new SimpleMailMessage();
        email.setTo(user.getEmail());
        email.setSubject("Reset Password");
        email.setText("Please open the following URL to reset your password: \r\n" + url);
        email.setFrom(env.getProperty("support.email"));
        return email;
    }

}
