package com.example.blog.service.impl;

import com.example.blog.dto.LoginDto;
import com.example.blog.dto.RegisterDto;
import com.example.blog.entity.Role;
import com.example.blog.entity.User;
import com.example.blog.exception.BlogAPIException;
import com.example.blog.repository.RoleRepository;
import com.example.blog.repository.UserRepository;
import com.example.blog.security.JwtTokenProvider;
import com.example.blog.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@Service
public class AuthServiceImp implements AuthService {

    private AuthenticationManager authenticationManager;
    private UserRepository userRepository;
    private RoleRepository roleRepository;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    public AuthServiceImp(AuthenticationManager authenticationManager, UserRepository userRepository, RoleRepository roleRepository) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    @Override
    public String login(LoginDto loginDto) {
        // xác thực xem co đúng user hay không,
        // nêu không đúng username, password ném ra lỗi authentication
        // nếu đúng thì tạo 1 cái xác thực để setAuthentication là người đó đã đăng nhập
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                loginDto.getUsernameOrEmail(), loginDto.getPassword()));

        // chính là cái này setAuthentication
        // SecurityContextHolder là nới lưu trữ thông tin người dùng đã xác thực
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // lấy username người dùng từ security context
        SecurityContext context = SecurityContextHolder.getContext();
        Authentication authen = context.getAuthentication();
        String username = authentication.getName();

        // trả vể token
        String token = jwtTokenProvider.generateToken(authentication);

        return token;
    }

    @Override
    public String register(RegisterDto registerDto) {

        if(userRepository.existsByUsername(registerDto.getUsername())){
            throw  new BlogAPIException(HttpStatus.BAD_REQUEST,"Username already exist");
        }
        if(userRepository.existsByEmail(registerDto.getEmail())){
            throw  new BlogAPIException(HttpStatus.BAD_REQUEST,"Email already exist");
        }

        User user = new User();
        user.setEmail(registerDto.getEmail());
        user.setName(registerDto.getName());
        user.setUsername(registerDto.getUsername());
        user.setPassword(passwordEncoder.encode(registerDto.getPassword()));

        Role role = roleRepository.findByName("ROLE_USER").get();
        user.setRoles(Arrays.asList(role));
        userRepository.save(user);

        return "User register successfully";
    }



}
