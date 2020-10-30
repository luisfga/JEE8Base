/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.luisfga.shiro;

import io.jsonwebtoken.Claims;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.DatatypeConverter;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.web.filter.AccessControlFilter;
import io.jsonwebtoken.Jwts;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author Luis
 */
public class JWTVerifyingFilter extends AccessControlFilter {
 
    private final Logger logger = LogManager.getLogger();
    
    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse arg1, Object arg2) throws Exception {
        boolean accessAllowed = false;
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        String jwt = httpRequest.getHeader("Authorization");
        logger.debug("Verificando header 'Authorization' : " + jwt);
        if (jwt == null || !jwt.startsWith("Bearer ")) {
            logger.debug("Check 1 : accessAllowed " + accessAllowed);
            return accessAllowed;
        }
        jwt = jwt.substring(jwt.indexOf(" "));
        
        Claims claims = Jwts.parser().setSigningKey(DatatypeConverter.parseBase64Binary("secret")).parseClaimsJws(jwt).getBody();
        
        String username = claims.getSubject();
        logger.debug("username " + username);
        Date expiration = claims.getExpiration();
        logger.debug("expiration " + expiration);
        
        LocalDate expirationOnLocalDate = expiration.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        logger.debug("expiration is after now? " + expirationOnLocalDate.isAfter(LocalDate.now()));
        
        accessAllowed = !username.isEmpty() && expirationOnLocalDate.isAfter(LocalDate.now());
        
        logger.debug("Check 2 : accessAllowed " + accessAllowed);
        return accessAllowed;
    }
 
    @Override
    protected boolean onAccessDenied(ServletRequest arg0, ServletResponse arg1) throws Exception {
        HttpServletResponse response = (HttpServletResponse) arg1;
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        return false;
    }
 
}
