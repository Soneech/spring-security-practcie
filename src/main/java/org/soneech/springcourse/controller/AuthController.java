package org.soneech.springcourse.controller;

import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.soneech.springcourse.api.response.UserErrorResponse;
import org.soneech.springcourse.dto.AuthenticationDTO;
import org.soneech.springcourse.dto.RegistrationDTO;
import org.soneech.springcourse.exception.AuthException;
import org.soneech.springcourse.model.User;
import org.soneech.springcourse.security.JWTUtil;
import org.soneech.springcourse.service.RegistrationService;
import org.soneech.springcourse.util.UserValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final UserValidator userValidator;
    private final RegistrationService registrationService;
    private final JWTUtil jwtUtil;
    private final ModelMapper modelMapper;
    private final AuthenticationManager authenticationManager;

    @Autowired
    public AuthController(UserValidator userValidator, RegistrationService registrationService,
                          JWTUtil jwtUtil, ModelMapper modelMapper, AuthenticationManager authenticationManager) {
        this.userValidator = userValidator;
        this.registrationService = registrationService;
        this.jwtUtil = jwtUtil;
        this.modelMapper = modelMapper;
        this.authenticationManager = authenticationManager;
    }

    @PostMapping("/registration")
    public ResponseEntity<Map<String, String>> performRegistration(@RequestBody @Valid RegistrationDTO registrationDTO,
                                                          BindingResult bindingResult) {
        User user = convertToUser(registrationDTO);
        userValidator.validate(user, bindingResult);

        if (bindingResult.hasErrors())
            throw new AuthException(prepareFieldsErrorsMessage(bindingResult));

        registrationService.register(user);
        String token = jwtUtil.generateToken(user.getUsername());
        return new ResponseEntity<>(Map.of("jwt_token", token), HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> performLogin(@RequestBody AuthenticationDTO authenticationDTO)
            throws AuthenticationException {

        UsernamePasswordAuthenticationToken authToken =
                new UsernamePasswordAuthenticationToken(
                        authenticationDTO.getUsername(),
                        authenticationDTO.getPassword()
                );
        try {
            authenticationManager.authenticate(authToken);
        } catch (AuthenticationException exception) {
            throw new AuthException("Неверный логин или пароль");
        }

        String token = jwtUtil.generateToken(authenticationDTO.getUsername());
        return new ResponseEntity<>(Map.of("jwt_token", token), HttpStatus.OK);
    }

    @ExceptionHandler
    public ResponseEntity<UserErrorResponse> handleException(AuthException exception) {
        return new ResponseEntity<>(
                new UserErrorResponse(exception.getMessage(), LocalDateTime.now()),
                HttpStatus.BAD_REQUEST
        );
    }

    public User convertToUser(RegistrationDTO registrationDTO) {
        return modelMapper.map(registrationDTO, User.class);
    }

    public String prepareFieldsErrorsMessage(BindingResult bindingResult) {
        StringBuilder message = new StringBuilder();
        List<FieldError> errors = bindingResult.getFieldErrors();

        for (var error: errors) {
            message.append(error.getField())
                    .append(":").append(error.getDefaultMessage())
                    .append(";");
        }
        return message.toString();
    }
}
