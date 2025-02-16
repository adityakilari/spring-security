package com.iralik.spring_security.service;

import com.iralik.spring_security.dto.AppUserInfo;

public interface AppUser {
    public AppUserInfo saveUser (AppUserInfo user);

    public String verify(AppUserInfo user);
}
