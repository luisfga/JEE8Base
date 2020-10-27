package com.luisfga.struts;

import com.luisfga.business.ConfirmRegistrationUseCase;
import com.luisfga.business.LoginUseCase;
import com.luisfga.business.PasswordRecoverUseCase;
import com.luisfga.business.PasswordResetUseCase;
import com.luisfga.business.RegisterUseCase;
import java.util.Properties;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.apache.openejb.jee.WebApp;
import org.apache.openejb.jee.jpa.unit.PersistenceUnit;
import org.apache.openejb.junit.ApplicationComposer;
import org.apache.openejb.testing.Classes;
import org.apache.openejb.testing.Configuration;
import org.apache.openejb.testing.Module;
import org.apache.struts2.StrutsTestCase;
import org.junit.Before;
import org.junit.runner.RunWith;

@RunWith(ApplicationComposer.class)
public abstract class AbstractComposedBaseTest extends StrutsTestCase {

    @PersistenceContext(unitName = "applicationJpaUnit")
    EntityManager em;

    public AbstractComposedBaseTest() {
    }

    @Before
    @Override
    public void setUp() throws Exception{
        super.setUp();
    }
    
    @Module
    @Classes(cdi = true, value = {
        RegisterUseCase.class, 
        ConfirmRegistrationUseCase.class,
        LoginUseCase.class, 
        PasswordRecoverUseCase.class, 
        PasswordResetUseCase.class})
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

        p.put("log4j.rootLogger", "fatal,C");
        p.put("log4j.category.OpenEJB", "warn");
        p.put("log4j.category.OpenEJB.options", "warn");
        p.put("log4j.category.OpenEJB.server", "warn");
        p.put("log4j.category.OpenEJB.startup", "warn");
        p.put("log4j.category.OpenEJB.startup.service", "warn");
        p.put("log4j.category.OpenEJB.startup.config", "warn");
//        p.put("log4j.category.OpenEJB.hsql", "warn");
//        p.put("log4j.category.CORBA-Adapter", "warn");
//        p.put("log4j.category.Transaction", "warn");
//        p.put("log4j.category.org.apache.activemq", "error");
//        p.put("log4j.category.org.apache.geronimo", "error");
//        p.put("log4j.category.openjpa", "warn");
//        p.put("log4j.appender.C", "org.apache.log4j.ConsoleAppender");
//        p.put("log4j.appender.C.layout", "org.apache.log4j.SimpleLayout");
//        p.put("openejb.nobanner", "false");

        return p;
    }

}
