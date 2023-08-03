package co.com.perficient.project3.controller;

import co.com.perficient.project3.model.dto.SignInRequest;
import co.com.perficient.project3.model.entity.UserP3;
import co.com.perficient.project3.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static co.com.perficient.project3.utils.constant.Constants.SIGNIN;
import static co.com.perficient.project3.utils.constant.Constants.SIGNUP;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {

    @Autowired
    private AuthService authService;

    @PostMapping(value = SIGNUP, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> signUp(@RequestBody UserP3 userP3) {
        String token = authService.signUp(userP3);
        return new ResponseEntity<>("user created successfully: " + token, HttpStatus.CREATED);
    }

    @PostMapping(value = SIGNIN, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> signIn(@RequestBody SignInRequest request) {
        return ResponseEntity.ok(authService.signIn(request));
    }
}
