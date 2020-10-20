package com.luisfga.business;

import com.luisfga.business.helper.MailHelper;
import java.util.Properties;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import junit.framework.TestCase;
import org.apache.openejb.jee.WebApp;
import org.apache.openejb.jee.jpa.unit.PersistenceUnit;
import org.apache.openejb.junit.ApplicationComposer;
import org.apache.openejb.testing.Classes;
import org.apache.openejb.testing.Configuration;
import org.apache.openejb.testing.Module;
import org.junit.runner.RunWith;

@RunWith(ApplicationComposer.class)
public abstract class AbstractComposedBaseTest extends TestCase{
    
    @PersistenceContext(unitName = "applicationJpaUnit")
    EntityManager em;
    
    public AbstractComposedBaseTest() {
    }
 
    @Module
    @Classes(cdi = true, value = { 
        RegisterUseCase.class, 
        PasswordResetUseCase.class, 
        PasswordRecoverUseCase.class, 
        LoginUseCase.class, 
        ConfirmRegistrationUseCase.class,
        MailHelper.class,
        TestSupportBean.class
    })
    public WebApp war() {
        return new WebApp();
    }
    
    @Module
    public PersistenceUnit persistence() {
        PersistenceUnit unit = new PersistenceUnit("applicationJpaUnit");
        unit.setJtaDataSource("applicationDS");
        unit.setNonJtaDataSource("applicationDSNonJta");
        unit.setExcludeUnlistedClasses(Boolean.FALSE);
        unit.setProperty("openjpa.jdbc.SynchronizeMappings", "buildSchema(ForeignKeys=true)");
        return unit;
    }
    

    @Configuration
    public Properties config() throws Exception {
        Properties p = new Properties();
        p.put("applicationDS", "new://Resource?type=DataSource");
        p.put("applicationDS.JdbcDriver", "org.hsqldb.jdbcDriver");
        p.put("applicationDS.JdbcUrl", "jdbc:hsqldb:mem:applicationDB");
        
        return p;
    }
    
}
