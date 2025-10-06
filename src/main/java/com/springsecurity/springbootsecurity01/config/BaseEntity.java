package com.springsecurity.springbootsecurity01.config;

import jakarta.persistence.*;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@MappedSuperclass
public class BaseEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime createdAt;
    @PrePersist
    protected void onCreate() {
        if (createdAt == null) {
            this.createdAt = LocalDateTime.now();
        }
    }
    private LocalDateTime updatedAt;
    @PreUpdate
    protected void onUpdate() {
        if (updatedAt == null) {
            this.updatedAt = LocalDateTime.now();
        }
    }
}