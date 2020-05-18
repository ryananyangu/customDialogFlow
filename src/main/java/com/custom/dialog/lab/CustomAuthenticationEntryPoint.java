///*
// * To change this license header, choose License Headers in Project Properties.
// * To change this template file, choose Tools | Templates
// * and open the template in the editor.
// */
package com.custom.dialog.lab;
//
import com.custom.dialog.lab.utils.Props;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.JSONObject;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
//import org.springframework.security.web.access.AccessDeniedHandler;
//
///**
// *
// * @author jovixe
// */
//public class CustomAccessDenied implements AccessDeniedHandler {
//
//
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {
        private static final Props SETTINGS = new Props();

        
    @Override
    public void commence(HttpServletRequest req, HttpServletResponse res, AuthenticationException authException) throws IOException, ServletException {
        res.setContentType("application/json;charset=UTF-8");
        res.setStatus(403);
        res.getWriter().write(SETTINGS.getStatusResponse("401", authException.getLocalizedMessage()).toString());
    }
}
