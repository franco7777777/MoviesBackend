package com.afroamericano.demo.config;



import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
@RequiredArgsConstructor  //genera constructores para propiedades finales
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

                  /*primera parte del proceso, JwtAuthFilter*/
    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,  //modifican la peticion, respuesta y el proceso, no pueden inicializar en null
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {

                       /*segunda parte Check Jwt Token*/

        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        final String userEmail;
        System.out.println("soy authHeader");
        System.out.println(authHeader);
        if (authHeader == null ||!authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response); //pasando datos al siguiente filtro
            return;
        }

        /*tercero Jwt Service, extraer datos*/

        jwt = authHeader.substring(7);
        userEmail = jwtService.extractUsername(jwt);
        if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(userEmail);
            System.out.println("soy user details");
            System.out.println(userDetails);
            if (jwtService.isTokenValid(jwt, userDetails)) {
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities()
                );
                authToken.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request)
                );
                System.out.println("sout authToken");
                System.out.println(authToken);
                SecurityContextHolder.getContext().setAuthentication(authToken);

                System.out.println("security context holder get authentitccation:");

                System.out.println(SecurityContextHolder.getContext().getAuthentication());
            }
        }

        filterChain.doFilter(request, response); //para que continue el proceso
    }
}