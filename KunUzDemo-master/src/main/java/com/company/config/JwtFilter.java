package com.company.config;

import com.company.dto.ProfileJwtDTO;
import com.company.util.JwtUtil;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class JwtFilter extends GenericFilterBean {

    @Override
    public void doFilter(ServletRequest servletRequest,
                         ServletResponse servletResponse,
                         FilterChain filterChain) throws IOException, ServletException {
        final HttpServletRequest request = (HttpServletRequest) servletRequest;
        final HttpServletResponse response = (HttpServletResponse) servletResponse;

        final String authHeader = request.getHeader("Authorization");

        if (authHeader == null || authHeader.isEmpty() || !authHeader.startsWith("Bearer ")){
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setHeader("Message", "Token Qanii");
            return;
        }
        try {
            String[] jwtArray = authHeader.split(" ");
            if (jwtArray.length != 2){
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                return;
            }
            ProfileJwtDTO dto = JwtUtil.decodeJwt(jwtArray[1]);
            request.setAttribute("profileJwtDto", dto);
        } catch (RuntimeException e){
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        }
        filterChain.doFilter(request, response);
    }
}
