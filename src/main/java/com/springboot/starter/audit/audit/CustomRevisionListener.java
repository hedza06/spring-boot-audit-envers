package com.springboot.starter.audit.audit;

import org.hibernate.envers.RevisionListener;
import org.springframework.stereotype.Component;

@Component
public class CustomRevisionListener implements RevisionListener {

    @Override
    public void newRevision(Object revisionEntity)
    {
        CustomRevisionEntity entity = (CustomRevisionEntity) revisionEntity;
        entity.setAuditor("Heril"); // TODO: fetch user from security context and set its username.
    }
}
