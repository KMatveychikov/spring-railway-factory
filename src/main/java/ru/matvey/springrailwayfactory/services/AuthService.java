package ru.matvey.springrailwayfactory.services;

import lombok.RequiredArgsConstructor;


import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.matvey.springrailwayfactory.model.User;
import ru.matvey.springrailwayfactory.model.dto.auth.AuthRequest;
import ru.matvey.springrailwayfactory.model.dto.auth.AuthResponse;
import ru.matvey.springrailwayfactory.model.dto.auth.RegisterRequest;
import ru.matvey.springrailwayfactory.model.dto.auth.UserResponse;
import ru.matvey.springrailwayfactory.repository.UserRepository;


import java.util.ArrayList;
import java.util.List;


@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthResponse register(RegisterRequest request) {
        User user = User.builder()
                .email(request.getEmail())
                .username(request.getName())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(request.getRole())
                .build();
        log.info("new user register {}", user);
        userRepository.save(user);
        String jwtToken = jwtService.generateToken(user);
        return AuthResponse.builder()
                .token(jwtToken)
                .build();
    }

    public AuthResponse authenticate(AuthRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );
       User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow();
        String jwtToken = jwtService.generateToken(user);
        log.info("User {} has authorized", user.getName());
        return AuthResponse.builder()
                .token(jwtToken)
                .user(convertUserToResponse(user))
                .build();
    }

    public String getCurrentUserEmail() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return auth.getName();
    }

    public User getCurrentUser() {
        return userRepository.findByEmail(getCurrentUserEmail()).orElseThrow();
    }

    public List<UserResponse> getAllUsersResponse() {
        List<User> users = userRepository.findAll();
        List<UserResponse> userResponses = new ArrayList<>();
        users.forEach(u -> userResponses.add(convertUserToResponse(u)));
        return userResponses;
    }
    public List<User> getAllUsers(){
        return userRepository.findAll();
    }

    public UserResponse convertUserToResponse(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .email(user.getEmail())
                .role(user.getRole())
                .name(user.getName()).build();
    }
    public UserResponse getUserResponseByEmail(String email) {
      return convertUserToResponse(userRepository.findByEmail(email).orElseThrow()) ;
    }
    public User getUserById(Long userId){
        return userRepository.findById(userId).orElseThrow();
    }
}
