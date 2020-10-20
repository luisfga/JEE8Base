/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.luisfga.business.exceptions;

import com.luisfga.shiro.ApplicationShiroJdbcRealm;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.realm.jdbc.JdbcRealm;

/**
 * Extensão de {@link AuthenticationException} por conta da assinatura do método
 * {@link JdbcRealm#doGetAuthenticationInfo(org.apache.shiro.authc.AuthenticationToken) }
 * 
 * Esta excessão é lançada pela implementação 
 * {@link ApplicationShiroJdbcRealm#doGetAuthenticationInfo(org.apache.shiro.authc.AuthenticationToken) }
 * @author Luis
 */
public class PendingEmailConfirmationException extends AuthenticationException{
    
}
