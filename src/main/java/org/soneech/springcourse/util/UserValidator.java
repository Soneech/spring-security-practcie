package org.soneech.springcourse.util;

import org.soneech.springcourse.model.User;
import org.soneech.springcourse.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.Optional;

@Component
public class UserValidator implements Validator {
    private final UserService userService;

    @Autowired
    public UserValidator(UserService userService) {
        this.userService = userService;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return User.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        User user = (User) target;
        Optional<User> foundUser = userService.findByUsername(user.getUsername());

        if (foundUser.isPresent() && user.getId() != foundUser.get().getId()) {
            errors.rejectValue("username", "", "Пользователь с таким именем существует");
        }
    }
}
