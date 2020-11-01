package com.luisfga.struts.config;

import com.opensymphony.xwork2.UnknownHandler;
import com.opensymphony.xwork2.config.Configuration;
import com.opensymphony.xwork2.config.ConfigurationException;
import com.opensymphony.xwork2.config.ConfigurationProvider;
import com.opensymphony.xwork2.inject.ContainerBuilder;
import com.opensymphony.xwork2.util.location.LocatableProperties;

public class ApplicationConfigurationProvider implements ConfigurationProvider{

    private Configuration configuration;
    
    @Override
    public void destroy() {}

    @Override
    public void init(Configuration configuration) throws ConfigurationException {
        this.configuration = configuration;
    }

    @Override
    public boolean needsReload() {
        return false;
    }

    @Override
    public void register(ContainerBuilder cb, LocatableProperties lp) throws ConfigurationException {
        
        lp.setProperty("struts.custom.i18n.resources", "global");
        lp.setProperty("struts.localizedTextProvider", "global-only");
        lp.setProperty("struts.action.extension", ",");
        cb.factory(UnknownHandler.class, "handler", ApplicationUnknownHandler.class);
        
        lp.setProperty("struts.devMode", "true");
        
    }

    @Override
    public void loadPackages() throws ConfigurationException {
        
    }
    
}