package com.reservation.backend.services.common;

import com.reservation.backend.entities.User;
import com.reservation.backend.exceptions.AppException;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class GenericService {
    public User getCurrentUserAsEntity() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new AppException("No User currently logged while executing this operation", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return (User) authentication.getPrincipal();
    }
}
