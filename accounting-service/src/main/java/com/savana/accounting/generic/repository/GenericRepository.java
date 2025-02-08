package com.savana.accounting.generic.repository;

import  com.savana.accounting.generic.dto.request.BaseRequest;
import  com.savana.accounting.generic.entity.audit.BaseEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface GenericRepository<D extends BaseRequest, E extends BaseEntity<E, D>> extends MongoRepository<E, Long> {
    boolean existsByFieldValue(Object value, String fieldName);
    Page<E> findAllByIsDeleted(boolean isDeleted, Pageable pageable);
}
