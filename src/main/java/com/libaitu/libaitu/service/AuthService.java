package com.libaitu.libaitu.service;



import com.libaitu.libaitu.dto.LoginRequest;
import com.libaitu.libaitu.dto.LoginResponse;
import com.libaitu.libaitu.dto.RegResponse;
import com.libaitu.libaitu.dto.RegistrationReq;
import com.libaitu.libaitu.entity.ERole;
import com.libaitu.libaitu.entity.EmailRegistrationConfirmToken;
import com.libaitu.libaitu.entity.Roles;
import com.libaitu.libaitu.entity.User;
import com.libaitu.libaitu.exception.*;
import com.libaitu.libaitu.repository.EmailRegistrationConfirmTokenRepository;
import com.libaitu.libaitu.repository.RefreshTokenRepository;
import com.libaitu.libaitu.repository.RoleRepository;
import com.libaitu.libaitu.repository.UserRepository;
import com.libaitu.libaitu.security.UserDetailsImpl;
import com.libaitu.libaitu.security.jwt.JwtProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AuthService {
    @Autowired
    private AuthenticationManager authManager;
    @Autowired
    private JwtProvider jwtProvider;
    @Autowired
    private RefreshTokenService refreshTokenService;
    @Autowired
    private RefreshTokenRepository refreshTokenRepository;
    @Autowired
    private PasswordEncoder encoder;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    MailService mailService;
    @Autowired
    EmailRegistrationConfirmTokenRepository emailRegistrationConfirmTokenRepository;
    @Autowired
    EmailVerificationTokenService emailVerificationTokenService;
    @Autowired
    EmailRegistrationConfirmTokenService emailRegistrationConfirmTokenService;
    private static final Logger log = LoggerFactory.getLogger(AuthService.class);


    public LoginResponse signIn(LoginRequest loginRequest) {
        log.info("auth service");
        Authentication authentication = authManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getBarcode(), loginRequest.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        UserDetailsImpl userPrincipals = (UserDetailsImpl) authentication.getPrincipal();
        String token = jwtProvider.generateToken(userPrincipals.getUsername());
        List<String> roles = userPrincipals.getAuthorities().stream()
                .map(item -> item.getAuthority())
                .collect(Collectors.toList());
        String refreshToken = refreshTokenService.createRefreshToken(userPrincipals.getUsername());
        return new LoginResponse(token, refreshToken, userPrincipals.getUsername(), "Successfully sign in", roles.get(0), userPrincipals.getUserId());
    }

    @Transactional
    public void logout(String username) {
        refreshTokenRepository.deleteByUserUsername(username);
    }

    @Transactional
    public RegResponse registration(RegistrationReq req, String code) throws InvalidEmailCodeException, TokenExpiredException, NotFoundException {
        EmailRegistrationConfirmToken emailToken = emailRegistrationConfirmTokenRepository.findByEmail(req.getEmail()).orElseThrow(()->new NotFoundException("code not found"));
        if(!emailToken.getToken().equals(code)) {
            throw  new InvalidEmailCodeException("invalid code");
        }
        if(emailToken.getExpiresAt().isBefore(LocalDateTime.now())) {
            throw  new TokenExpiredException("Code expired");
        }

                User user = new User();
                user.setUsername(req.getUsername());
                user.setPassword(encoder.encode(req.getPassword()));
                List<String> strRoles = new ArrayList<>();
                if (req.getRole() != null) {
                    strRoles.add(req.getRole());
                }
                List<Roles> roles = new ArrayList<>();
                if (strRoles.size() == 0) {
                    Roles userRole = roleRepository.findByRole(ERole.ROLE_USER).orElseThrow(() -> new RuntimeException("Role not found"));
                    roles.add(userRole);
                } else {
                    if (strRoles.get(0).equals("admin")) {
                        Roles adminRole = roleRepository.findByRole(ERole.ROLE_ADMIN).orElseThrow(() -> new RuntimeException("Role not found"));
                        roles.add(adminRole);
                    }

                }
                user.setRole(roles.get(0));
                user.setFullName(req.getFullName());
                user.setEmail(req.getEmail());
                userRepository.save(user);

                String token = jwtProvider.generateToken(user.getUsername());
                String refreshToken = refreshTokenService.createRefreshToken(user.getUsername());

                return new RegResponse(token, refreshToken, user.getUsername(), user.getRole().getRole().toString(), user.getUserId());
    }




    public void restorePassword(String email){
        User user=userRepository.findByEmail(email).orElseThrow(()->new UsernameNotFoundException("email invalid"));
        String token =emailVerificationTokenService.saveToken(user,emailVerificationTokenService.createToken());
        mailService.send(email,"Please follow to http://localhost:3000/restore/"+token+" to change the password","Password restore");
    }


    public long getEmailConfirmationCodeForRegistration(RegistrationReq req) throws EmailAlreadyExistException, UsernameAlreadyExistException {
        if (userRepository.existsByEmail(req.getEmail())) {
            throw new EmailAlreadyExistException("email already exists");
        } else if (userRepository.existsByUsername(req.getUsername())) {
            throw new UsernameAlreadyExistException("username already exists");
        }
        EmailRegistrationConfirmToken res = emailRegistrationConfirmTokenService.saveToken(req,emailRegistrationConfirmTokenService.createToken());
        mailService.send(req.getEmail(),"Your confirmation code : "+ res.getToken(),"Email confirm");
        return Duration.between(LocalDateTime.now(), res.getExpiresAt()).getSeconds();
    }

}
