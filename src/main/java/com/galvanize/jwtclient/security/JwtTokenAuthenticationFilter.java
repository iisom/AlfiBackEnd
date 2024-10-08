package com.galvanize.jwtclient.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;


public class JwtTokenAuthenticationFilter extends  OncePerRequestFilter {

    public static final Logger logger = LoggerFactory.getLogger(JwtTokenAuthenticationFilter.class);

    private final JwtProperties jwtProperties;

    public JwtTokenAuthenticationFilter(JwtProperties jwtProperties) {
        this.jwtProperties = jwtProperties;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {

        // 1. get the authentication header. Tokens are supposed to be passed in the authentication header
        String header = request.getHeader(jwtProperties.getHeader());

        // 2. validate the header and check the prefix
        if(header == null || !header.startsWith(jwtProperties.getPrefix())) {
            chain.doFilter(request, response);  		// If not valid, go to the next filter.
            return;
        }

        logger.debug(String.format("%s header = %s", jwtProperties.getHeader(), header));

        // If there is no token provided and hence the user won't be authenticated.
        // It's Ok. Maybe the user accessing a public path or asking for a token.

        // All secured paths that needs a token are already defined and secured in config class.
        // And If user tried to access without access token, then he won't be authenticated and an exception will be thrown.

        // 3. Get the token
        String token = header.replace(jwtProperties.getPrefix(), "");

        try {	// exceptions might be thrown in creating the claims if for example the token is expired

            // 4. Validate the token
            Claims claims = Jwts.parser()
                    .setSigningKey(jwtProperties.getSecret().getBytes())
                    .parseClaimsJws(token)
                    .getBody();

            String username = claims.getSubject();
            if(username != null) {
                logger.debug(String.format("Creating token for user %s", username));
                User user = new User(claims.get("guid", Long.class),username, claims.get("email", String.class),
                        claims.get("first_name", String.class), claims.get("last_name", String.class), null);

                @SuppressWarnings("unchecked")
                List<String> authorities = (List<String>) claims.get("authorities");
                //authorities.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList()));

                // 5. Create auth object
                // UsernamePasswordAuthenticationToken: A built-in object, used by spring to represent the current authenticated / being authenticated user.
                // It needs a list of authorities, which has type of GrantedAuthority interface, where SimpleGrantedAuthority is an implementation of that interface
                UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
                        user, null, authorities.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList()));

                // 6. Authenticate the user
                // Now, user is authenticated
                SecurityContextHolder.getContext().setAuthentication(auth);
                logger.debug(String.format("DONE creating token for user %s", username));
            }

        } catch (Exception e) {
            // In case of failure. Make sure it's clear; so guarantee user won't be authenticated
            SecurityContextHolder.clearContext();
        }

        // go to the next filter in the filter chain
        chain.doFilter(request, response);
    }

}

