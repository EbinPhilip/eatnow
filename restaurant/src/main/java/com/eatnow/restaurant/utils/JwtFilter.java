package com.eatnow.restaurant.utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;

@Component
public class JwtFilter extends OncePerRequestFilter {

	@Value("${jwt.secret}")
	private String secret;

    @Value("${jwt.issuer}")
	private String issuer;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
            HttpServletResponse response,
            FilterChain chain)
            throws ServletException, IOException {
        
        Claims claims = null;
        if (request.getHeader("Authorization") != null) {
    
            String jwtToken = request.getHeader("Authorization").replace("Bearer ", "");
            try {
                claims = Jwts.parserBuilder()
                        .setSigningKey(secret.getBytes())
                        .requireIssuer(issuer)
                        .build()
                        .parseClaimsJws(jwtToken)
                        .getBody();
            } catch (JwtException e) {
                logger.warn(e.getMessage());
            }
        }

        if (claims != null) {

            String role = claims.get("role", String.class);
            User user = null;
            List<SimpleGrantedAuthority> authorities = new ArrayList<>();

            if (role.equals("restaurant")) {
                String restaurantId = claims.getSubject();
                authorities.add(new SimpleGrantedAuthority("ROLE_RESTAURANT"));
                user = new User(restaurantId, "", authorities);
            } else if (role.equals("user")) {
                String userId = claims.getSubject();
                authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
                user = new User(userId, "", authorities);
            }

            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                    user, null, authorities);
            authentication
                    .setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        chain.doFilter(request, response);
    }
}
