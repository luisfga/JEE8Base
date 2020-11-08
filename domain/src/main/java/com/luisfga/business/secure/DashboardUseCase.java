/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.luisfga.business.secure;

import com.luisfga.business.UseCase;
import javax.ejb.Stateless;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.shiro.authz.annotation.RequiresAuthentication;

/**
 *
 * @author Luis
 */
@Stateless
@RequiresAuthentication
public class DashboardUseCase extends UseCase{
    
    Logger logger = LogManager.getLogger();
    
}
