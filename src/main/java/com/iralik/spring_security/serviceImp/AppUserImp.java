package com.iralik.spring_security.serviceImp;

import com.iralik.spring_security.dto.AppUserInfo;
import com.iralik.spring_security.service.AppUser;
import com.iralik.spring_security.model.Users;
import com.iralik.spring_security.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AppUserImp implements AppUser {

    private final UserRepo userRepo;

    private final AuthenticationManager authManager;

    private final Jwt jwt;

    @Autowired
    AppUserImp(UserRepo userRepo, AuthenticationManager authManager, Jwt jwt) {
        this.userRepo = userRepo;
        this.authManager = authManager;
        this.jwt = jwt;
    }

    private final BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder(12);


    @Override
    public AppUserInfo saveUser(AppUserInfo user) {
        Optional<Users> appUser = userRepo.findById(user.getId());
        if (appUser.isPresent()) {
            throw new RuntimeException("User with ID " + user.getId() + " already exists.");
        }
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        Users savedUser = userRepo.save(convertToUserEntity(user));
        return convertToAppUserInfo(savedUser);
    }

    private Users convertToUserEntity(AppUserInfo userInfo) {
        Users user = new Users();
        user.setId(userInfo.getId());
        user.setUserName(userInfo.getUserName());
        user.setPassword(userInfo.getPassword());
        return user;
    }

    private AppUserInfo convertToAppUserInfo(Users savedUser) {
        AppUserInfo userInfo = new AppUserInfo();
        userInfo.setId(savedUser.getId());
        userInfo.setUserName(savedUser.getUserName());
        userInfo.setPassword(savedUser.getPassword());
        return userInfo;
    }

    @Override
    public String verify(AppUserInfo user) {
        Users currentUser = userRepo.findByUserName(user.getUserName());
        if (currentUser != null) {
            BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
            if (passwordEncoder.matches(user.getPassword(), currentUser.getPassword())) {
                Authentication auth = authManager.authenticate
                        (new UsernamePasswordAuthenticationToken(user.getUserName(), user.getPassword()));
                if (auth.isAuthenticated()) {
                    return jwt.createToken(user.getUserName());
                } else {
                    return "Authentication failed";
                }
            }else {
                return "Invalid password";
            }
        }
        return "User not found";
    }
}
