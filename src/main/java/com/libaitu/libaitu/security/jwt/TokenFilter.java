package com.libaitu.libaitu.security.jwt;


import com.libaitu.libaitu.security.UserDetailServiceImpl;
import com.libaitu.libaitu.security.UserDetailsImpl;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;


import java.io.IOException;

public class TokenFilter extends OncePerRequestFilter {

    @Autowired
    private JwtProvider jwtProvider;
    @Autowired
    private UserDetailServiceImpl userDetailService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String jwt=parseJwt(request);
        if(jwt!=null&& jwtProvider.validateToken(jwt)){
            String barcode=jwtProvider.getBarCodeFromToken(jwt);
            UserDetailsImpl userDetails= (UserDetailsImpl) userDetailService.loadUserByUsername(barcode);
            UsernamePasswordAuthenticationToken obj=new UsernamePasswordAuthenticationToken(userDetails,null,userDetails.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(obj);

        }
        filterChain.doFilter(request,response);
    }

    public String parseJwt(HttpServletRequest request){
        String header=request.getHeader("Authorization");
        if(header!=null&&header.startsWith("Bearer ")){
            return header.substring(7,header.length());
        }
        return null;
    }
}


