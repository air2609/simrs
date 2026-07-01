package com.simrs.backend.security;

import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class AuthUserInterceptor implements HandlerInterceptor {

    public static final String USER_HEADER = "X-User-Id";
    public static final String USER_ATTRIBUTE = "authenticatedUserId";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        String path = request.getRequestURI();
        String method = request.getMethod();

        if (!path.startsWith("/api/admissions")) {
            return true;
        }

        boolean writeMethod = "POST".equalsIgnoreCase(method)
                || "PUT".equalsIgnoreCase(method)
                || "PATCH".equalsIgnoreCase(method)
                || "DELETE".equalsIgnoreCase(method);

        if (!writeMethod) {
            return true;
        }

        String userId = request.getHeader(USER_HEADER);
        if (userId == null || userId.trim().isEmpty()) {
            writeUnauthorized(response);
            return false;
        }

        request.setAttribute(USER_ATTRIBUTE, userId.trim());
        return true;
    }

    private void writeUnauthorized(HttpServletResponse response) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.getWriter().write("{\"message\":\"Header X-User-Id wajib diisi\"}");
    }
}
