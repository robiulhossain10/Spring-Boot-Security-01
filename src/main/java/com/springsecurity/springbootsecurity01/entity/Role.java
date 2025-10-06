package com.springsecurity.springbootsecurity01.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.OffsetDateTime;

@Entity
@Table(name = "role") // PostgreSQL এ যদি টেবিলের নাম role থাকে, ঠিক আছে
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
public class Role {

    // ভুল ছিল rollName → ঠিক করলাম roleName
    @Id
    @Column(name = "role_name", nullable = false, updatable = false)
    private String roleName;

    @Column(name = "role_description")
    private String roleDescription;

    @CreatedDate
    @Column(name = "date_created", nullable = false, updatable = false)
    private OffsetDateTime dateCreated;

    @LastModifiedDate
    @Column(name = "last_updated", nullable = false)
    private OffsetDateTime lastUpdated;
}
