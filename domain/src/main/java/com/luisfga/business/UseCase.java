package com.luisfga.business;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

public abstract class UseCase {
    
    @PersistenceContext(unitName = "applicationJpaUnit")
    public EntityManager em;
    
}
