package com.luisfga.business;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

abstract class UseCase {
    
    @PersistenceContext(unitName = "applicationJpaUnit")
    EntityManager em;
    
}
