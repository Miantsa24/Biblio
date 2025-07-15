package com.example.biblio.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class AuthFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        String requestURI = request.getRequestURI();

        // Autoriser l'accès aux pages publiques sans vérification de session
        if (requestURI.equals("/") || 
            requestURI.equals("/admin/login") || 
            requestURI.equals("/adherant/login") || 
            requestURI.equals("/logout") || 
            requestURI.equals("/adherant/logout") || 
            requestURI.matches("/adherant/livres/\\d+") || 
            requestURI.matches("/adherants/\\d+")) { // Autoriser /adherants/{idAdherant}
            filterChain.doFilter(request, response);
            return;
        }

        // Vérifier si l'utilisateur est connecté
        if (session == null) {
            response.sendRedirect("/");
            return;
        }

        // Routes pour adhérants
        if (requestURI.startsWith("/adherant/")) {
            if (session == null || session.getAttribute("adherantLoggedIn") == null) {
        response.sendRedirect("/adherant/login");
        return;
    }
        }
        // Routes pour administrateurs
        else {
            if (session.getAttribute("adminLoggedIn") == null) {
                response.sendRedirect("/admin/login");
                return;
            }
        }

        filterChain.doFilter(request, response);
    }
}