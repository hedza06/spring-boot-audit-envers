package com.springboot.starter.audit.audit;

import org.hibernate.envers.DefaultRevisionEntity;
import org.hibernate.envers.RevisionEntity;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@RevisionEntity(CustomRevisionListener.class)
@Table(name = "revision_info")
public class CustomRevisionEntity extends DefaultRevisionEntity {

    private String auditor;

    public String getAuditor() {
        return auditor;
    }

    public void setAuditor(String auditor) {
        this.auditor = auditor;
    }
}
