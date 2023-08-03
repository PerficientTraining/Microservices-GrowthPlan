package co.com.perficient.project3.service;

import co.com.perficient.project3.model.dto.SignInRequest;
import co.com.perficient.project3.model.entity.UserP3;

public interface AuthService {
    String signUp(UserP3 user);

    String signIn(SignInRequest request);
}
