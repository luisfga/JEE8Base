package com.luisfga;

import com.luisfga.business.entities.AppPermission;
import com.luisfga.business.entities.AppRole;
import com.luisfga.business.entities.AppUser;
import java.io.IOException;
import java.time.LocalDate;
import java.time.Month;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceUnit;
import javax.persistence.Query;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import javax.transaction.HeuristicMixedException;
import javax.transaction.HeuristicRollbackException;
import javax.transaction.NotSupportedException;
import javax.transaction.RollbackException;
import javax.transaction.SystemException;
import javax.transaction.UserTransaction;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.shiro.authc.credential.DefaultPasswordService;
import org.hsqldb.persist.HsqlProperties;
import org.hsqldb.server.ServerAcl;
import org.hsqldb.server.WebServer;

/* Usada apenas em desenvolvimento para checar/carregar o que for preciso no banco de dados */
@WebListener
public class DevelopmentDatabaseSetup implements ServletContextListener {

    Logger logger = LogManager.getLogger();
    
    @Resource
    private UserTransaction tx;

    @PersistenceUnit(unitName = "applicationJpaUnit")
    EntityManagerFactory emf;

    WebServer server;
    
    @Override
    public void contextInitialized(ServletContextEvent sce) {

//        startHSQLDBServerMode();

        checkRequiredData();
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
//        server.shutdown();
    }

    private void startHSQLDBServerMode() {
        try {
            System.out.println("Starting Database");
            HsqlProperties p = new HsqlProperties();
            p.setProperty("server.database.0", "file:../data/ShiroStrutsJEE8/shiro_struts_jee8");
            p.setProperty("server.dbname.0", "applicationDB");
            p.setProperty("server.port", "9001");
            server = new WebServer();
            server.setProperties(p);
            server.setLogWriter(null); // can use custom writer
            server.setErrWriter(null); // can use custom writer
            server.start();
        } catch (ServerAcl.AclFormatException | IOException ioex) {
            logger.error("Erro ao tentar iniciar o HSQLDB em modo SERVER", ioex);
        }
    }
    
    private void checkRequiredData() {

        try {

            tx.begin();
            
            EntityManager em = emf.createEntityManager();

            //Dentre as ROLES devem estar contidas 'Normal User' e 'Admin'
            //A role 'Admin' deve ter as permissões 'admin:create', 'admin:read', 'admin:update', 'admin:delete';
            
            checkNormalUserRole(em);

            checkAdminRole(em);

            checkAdminUser(em);

            tx.commit();

        } catch (IllegalStateException | SecurityException | NotSupportedException 
                | SystemException | RollbackException | HeuristicMixedException 
                | HeuristicRollbackException  ex) {
            logger.error(ex.getMessage());
            
        }
    }

    private void checkAdminUser(EntityManager em) {
        //Confere usuário Admin
        logger.info("Checando usuário Admin");
        Query findAdminUser = em.createNamedQuery("AppUser.findByEmail");
        findAdminUser.setParameter("email", "admin@jee8.com");
        AppUser appUser;
        
        try {
            appUser = (AppUser) findAdminUser.getSingleResult();
            logger.info("Usuário Admin OK!");
        } catch (NoResultException ex) {
            logger.info("Usuário Admin não encontrado!");
            appUser = new AppUser();
            
            appUser.setBirthday(LocalDate.of(1970, Month.JANUARY, 1));
            appUser.setEmail("admin@jee8.com");
            appUser.setJoinTime(OffsetDateTime.now());
            DefaultPasswordService defaultPasswordService = new DefaultPasswordService();
            appUser.setPassword(defaultPasswordService.encryptPassword("123"));
            appUser.setUserName("admin");
            appUser.setStatus("ok");
            
            //associar role Admin ao usuário admin
            Query findAdminRole = em.createNamedQuery("AppRole.findByName");
            findAdminRole.setParameter("roleName", "Admin");
            
            //não PODE estar nula, então não precisa checar NoResultException
            AppRole adminRole = (AppRole) findAdminRole.getSingleResult();
            
            List<AppRole> userRoles = new ArrayList<>();
            userRoles.add(adminRole);
            appUser.setRoles(userRoles);
            
            em.persist(appUser);
            logger.info("Usuário Admin SALVO!");
        }
    }

    private void checkAdminRole(EntityManager em) {
        logger.info("Checando ROLE 'Admin'");
        Query findAdminRole = em.createNamedQuery("AppRole.findByName");
        findAdminRole.setParameter("roleName", "Admin");
        AppRole role;
        try {
            role = (AppRole) findAdminRole.getSingleResult();
            logger.info("Role 'Admin' OK!");
        } catch (NoResultException ex) {
            logger.info("Role 'Admin' não encontrada!");
            
            //se não tem role admin também não tem as permissões
            logger.info("Criando permissões (admin:create, admin:read, admin:update, admin:delete)");
            List<AppPermission> permissoes = new ArrayList();
            AppPermission adminCreate = new AppPermission();
            adminCreate.setPermissionName("admin:create");
            adminCreate.setDeletable(Boolean.FALSE);
            permissoes.add(adminCreate);
            AppPermission adminRead = new AppPermission();
            adminRead.setPermissionName("admin:read");
            adminRead.setDeletable(Boolean.FALSE);
            permissoes.add(adminRead);
            AppPermission adminUpdate = new AppPermission();
            adminUpdate.setPermissionName("admin:update");
            adminUpdate.setDeletable(Boolean.FALSE);
            permissoes.add(adminUpdate);
            AppPermission adminDelete = new AppPermission();
            adminDelete.setPermissionName("admin:delete");
            adminDelete.setDeletable(Boolean.FALSE);
            permissoes.add(adminDelete);
            
            role = new AppRole();
            role.setRoleName("Admin");
            role.setPermissions(permissoes);
            role.setDeletable(Boolean.FALSE);
            em.persist(role);
            logger.info("Role 'Admin' com suas permissões SALVA!");
        }
    }

    private void checkNormalUserRole(EntityManager em) {
        
        logger.info("Checando ROLE 'Normal User'");
        Query findNormalUserRole = em.createNamedQuery("AppRole.findByName");
        findNormalUserRole.setParameter("roleName", "Normal User");
        AppRole role;
        try {
            role = (AppRole) findNormalUserRole.getSingleResult();
            logger.info("Role 'Normal User' OK!");
        } catch (NoResultException ex) {
            logger.info("Role 'Normal User' não encontrada!");
            role = new AppRole();
            role.setRoleName("Normal User");
            role.setDeletable(Boolean.FALSE);
            em.persist(role);
            logger.info("Role 'Normal User' SALVA!");
        }
    }
}
