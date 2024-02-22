package com.safeway.userservice.controller;

import com.safeway.userservice.dto.ErrorResponse;
import com.safeway.userservice.exception.BaseException;
import com.safeway.userservice.exception.ErrorEnum;
import com.safeway.userservice.exception.UnAuthrizeException;
import com.safeway.userservice.sequrity.UserDetailsImpl;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Set;

public class BaseController {

    public boolean checkAuthorizedUser(String validCode) {
        Set<String> permissions = ((UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getPermissions();
        if (!permissions.contains(validCode)) {
            throw new UnAuthrizeException(ErrorEnum.ERROR_FORBIDDEN);
        }
        return true;
    }

}
