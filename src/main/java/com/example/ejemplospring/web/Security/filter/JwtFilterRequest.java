package com.example.ejemplospring.web.Security.filter;

import com.example.ejemplospring.domain.service.SamUserDetailsService;
import com.example.ejemplospring.web.Security.JWTUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class JwtFilterRequest extends OncePerRequestFilter {
    @Autowired
    private JWTUtil jwtUtil;
    @Autowired
    private SamUserDetailsService samUserDetailsService;
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String autorizationHeader = request.getHeader("Authorization");

        if(autorizationHeader!=null && autorizationHeader.startsWith("Bearer")){
            String jwt=autorizationHeader.substring(7);
            String username=jwtUtil.extractUsername(jwt);

            if(username !=null && SecurityContextHolder.getContext().getAuthentication()==null){
                UserDetails userDetails= samUserDetailsService.loadUserByUsername(username);
                if(jwtUtil.validateToken(jwt,userDetails)){
                    UsernamePasswordAuthenticationToken authToken= new UsernamePasswordAuthenticationToken(userDetails,null,userDetails.getAuthorities());
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authToken);

                }
            }

        }
        filterChain.doFilter(request,response);
    }

}
