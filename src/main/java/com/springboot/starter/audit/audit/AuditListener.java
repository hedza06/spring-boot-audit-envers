package com.springboot.starter.audit.audit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.PrePersist;
import javax.persistence.PreRemove;
import javax.persistence.PreUpdate;

public class AuditListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(AuditListener.class);

    @PrePersist
    @PreUpdate
    @PreRemove
    public void beforeAnyOperation(Object object)
    {
        LOGGER.info("Audit activated before operation... {}, ", object.toString());
        // do something...
    }

}
