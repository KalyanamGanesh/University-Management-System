package com.UMS.University_Management_System.auth;

import com.UMS.University_Management_System.entity.AppUser;

public record UserResponse(Long id, String name, String email, String provider) {
    public static UserResponse from(AppUser user) {
        return new UserResponse(user.getId(), user.getName(), user.getEmail(), user.getProvider());
    }
}
