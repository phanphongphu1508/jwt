package com.example.jwt.filter;

import com.example.jwt.util.JwtHelper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
public class CustomSecurityFilter extends OncePerRequestFilter {
    @Autowired
    private JwtHelper jwtHelper;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String authenHeader = request.getHeader("Authoriation");
        if (authenHeader != null && authenHeader.startsWith("Bearer ")) {
            // Authen header có giá trị
            String token = authenHeader.substring(7);
            boolean isSuccess = jwtHelper.decryptToken(token);
            if (isSuccess) {
                String role = jwtHelper.getDataToken(token);
                List<SimpleGrantedAuthority> authoritiesList = new ArrayList<>();
                SimpleGrantedAuthority authority = new SimpleGrantedAuthority(role);
                authoritiesList.add(authority);

                SecurityContext sercurityContext = SecurityContextHolder.getContext();
                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken("", "", authoritiesList);
                sercurityContext.setAuthentication(authenticationToken);
            }
            System.out.println("CustomSecurityFilter " + isSuccess);

        }
        filterChain.doFilter(request, response);
    }
}
