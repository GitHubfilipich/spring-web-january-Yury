package ru.specialist.spring.util;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import ru.specialist.spring.entity.Post;


public class SecurityUtils {

    public static final String ACCESS_DENIED = "Access Denied";

    public static UserDetails getCurrentUserDetails(){
        Object principal = SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal();

        if (!(principal instanceof UserDetails)) {
            throw new AccessDeniedException(ACCESS_DENIED);
        }
        return (UserDetails)principal;
    }

    public static void checkAuthorityOnPost(Post post) {
        String username = SecurityUtils.getCurrentUserDetails().getUsername();
        if (!post.getUser().getUsername().equals(username)){
            throw new AccessDeniedException(ACCESS_DENIED);
        }
    }
}
