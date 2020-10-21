package com.luisfga.business;

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
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.UnavailableSecurityManagerException;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.subject.support.SubjectThreadState;
import org.apache.shiro.util.LifecycleUtils;
import org.apache.shiro.util.ThreadState;
import org.junit.AfterClass;
import org.junit.runner.RunWith;

@RunWith(ApplicationComposer.class)
public abstract class AbstractShiroEnabledComposedBaseTest extends TestCase{
    
    @PersistenceContext(unitName = "applicationJpaUnit")
    EntityManager em;
    
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
    
    private static ThreadState subjectThreadState;

    public AbstractShiroEnabledComposedBaseTest() {
    }

    /**
     * Allows subclasses to set the currently executing {@link Subject} instance.
     *
     * @param subject the Subject instance
     */
    protected void setSubject(Subject subject) {
        clearSubject();
        subjectThreadState = createThreadState(subject);
        subjectThreadState.bind();
    }

    protected Subject getSubject() {
        return SecurityUtils.getSubject();
    }

    protected ThreadState createThreadState(Subject subject) {
        return new SubjectThreadState(subject);
    }

    /**
     * Clears Shiro's thread state, ensuring the thread remains clean for future test execution.
     */
    protected void clearSubject() {
        doClearSubject();
    }

    private static void doClearSubject() {
        if (subjectThreadState != null) {
            subjectThreadState.clear();
            subjectThreadState = null;
        }
    }

    protected static void setSecurityManager(org.apache.shiro.mgt.SecurityManager securityManager) {
        SecurityUtils.setSecurityManager(securityManager);
    }

    protected static SecurityManager getSecurityManager() {
        return SecurityUtils.getSecurityManager();
    }

    @AfterClass
    public static void tearDownShiro() {
        doClearSubject();
        try {
            SecurityManager securityManager = getSecurityManager();
            LifecycleUtils.destroy(securityManager);
        } catch (UnavailableSecurityManagerException e) {
            //we don't care about this when cleaning up the test environment
            //(for example, maybe the subclass is a unit test and it didn't
            // need a SecurityManager instance because it was using only
            // mock Subject instances)
        }
        setSecurityManager(null);
    }

}
