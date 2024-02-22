package com.safeway.userservice.controller;

import com.safeway.userservice.exception.ErrorEnum;
import com.safeway.userservice.exception.UnAuthorizeException;
import com.safeway.userservice.sequrity.UserDetailsImpl;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Set;

public class BaseController {

    public boolean checkAuthorizedUser(String validCode) {
        Set<String> permissions = ((UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getPermissions();
//        if (!permissions.contains(validCode)) {
//            throw new UnAuthorizeException(ErrorEnum.ERROR_FORBIDDEN);
//        }
        return true;
    }

}
