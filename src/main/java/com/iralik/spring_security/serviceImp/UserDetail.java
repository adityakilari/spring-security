package com.iralik.spring_security.serviceImp;

import com.iralik.spring_security.model.UserPrincipal;
import com.iralik.spring_security.model.Users;
import com.iralik.spring_security.repository.UserRepo;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.function.Function;

@Service
@Slf4j
public class UserDetail implements UserDetailsService {


    private final UserRepo userRepo;
    @Autowired
    UserDetail(UserRepo userRepo){
        this.userRepo = userRepo;
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Users user = userRepo.findByUserName(username);
        if(user == null){
            throw new UsernameNotFoundException("User not in the database");
        }
        return new UserPrincipal(user);
    }
}
