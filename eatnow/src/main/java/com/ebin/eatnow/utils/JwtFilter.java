package com.ebin.eatnow.utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.ebin.eatnow.dtos.RestaurantDto;
import com.ebin.eatnow.dtos.UserDto;
import com.ebin.eatnow.services.RestaurantService;
import com.ebin.eatnow.services.UserService;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;

@Component
public class JwtFilter extends OncePerRequestFilter {
    @Autowired
    RestaurantService restaurantService;

    @Autowired
    UserService userService;

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
                RestaurantDto restaurantDto = restaurantService.getRestaurantbyId(restaurantId);
                authorities.add(new SimpleGrantedAuthority("restaurant"));
                user = new User(restaurantDto.getId(), "", authorities);
            } else if (role.equals("user")) {
                String userId = claims.getSubject();
                UserDto userDto = userService.getUserById(userId);
                authorities.add(new SimpleGrantedAuthority("user"));
                user = new User(userDto.getId(), "", authorities);
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
