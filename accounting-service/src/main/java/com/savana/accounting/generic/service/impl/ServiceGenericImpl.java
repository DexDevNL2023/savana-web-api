package com.savana.accounting.generic.service.impl;

import  com.savana.accounting.generic.dto.reponse.BaseResponse;
import  com.savana.accounting.generic.dto.reponse.PagedResponse;
import  com.savana.accounting.generic.dto.request.BaseRequest;
import  com.savana.accounting.generic.entity.audit.BaseEntity;
import  com.savana.accounting.generic.exceptions.RessourceNotFoundException;
import  com.savana.accounting.generic.logging.LogExecution;
import  com.savana.accounting.generic.mapper.GenericMapper;
import  com.savana.accounting.generic.repository.GenericRepository;
import  com.savana.accounting.generic.service.ServiceGeneric;
import  com.savana.accounting.generic.utils.AppConstants;
import  com.savana.accounting.generic.utils.GenericUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Transactional
public abstract class ServiceGenericImpl<D extends BaseRequest, R extends BaseResponse, E extends BaseEntity<E, D>> implements ServiceGeneric<D, R, E> {

    private static final Logger logger = LoggerFactory.getLogger(ServiceGenericImpl.class);

    protected final Class<E> entityClass;
    protected final GenericRepository<D, E> repository;
    protected final GenericMapper<D, R, E> mapper;

    public ServiceGenericImpl(Class<E> entityClass, GenericRepository<D, E> repository, GenericMapper<D, R, E> mapper) {
        this.entityClass = entityClass;
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    @Transactional
    @LogExecution
    public R save(D dto) throws RessourceNotFoundException {
        logger.debug("Saving entity of type: {}", entityClass.getSimpleName());
        E e = mapper.toEntity(dto);
        logger.debug("Mapped DTO to Entity: {}", e);
        e = repository.save(e);
        logger.debug("Saved entity: {}", e);
        return getOne(e);
    }

    @Override
    @Transactional
    @LogExecution
    public E saveDefault(E entity) {
        logger.debug("Saving default entity: {}", entity);
        E savedEntity = repository.save(entity);
        logger.debug("Saved default entity: {}", savedEntity);
        return savedEntity;
    }

    @Override
    @Transactional
    @LogExecution
    public List<R> saveAll(List<D> dtos) {
        logger.debug("Saving all DTOs: {}", dtos);
        dtos.forEach(this::save);
        logger.debug("All DTOs saved successfully.");
        return getAll();
    }

    @Override
    @Transactional
    @LogExecution
    public R update(D dto, Long id) {
        logger.debug("Updating entity of type: {} with ID: {}", entityClass.getSimpleName(), id);
        E entity = getById(id);
        if (entity.equalsToDto(dto)) {
            logger.debug("Duplicate entity data for update detected: {}", dto);
            throw new RessourceNotFoundException("La ressource " + entityClass.getSimpleName() + " avec les données suivante : " + dto + " existe déjà");
        }
        entity.update(mapper.toEntity(dto));
        logger.debug("Entity updated: {}", entity);
        entity = repository.save(entity);
        logger.debug("Updated entity: {}", entity);
        return getOne(entity);
    }

    @Override
    @Transactional
    @LogExecution
    public Boolean exist(Long id) {
        logger.debug("Checking existence of entity with ID: {}", id);
        boolean exists = repository.existsById(id);
        logger.debug("Entity exists: {}", exists);
        return exists;
    }

    @Override
    @Transactional
    @LogExecution
    public List<R> getAll() {
        logger.debug("Fetching all entities of type: {}", entityClass.getSimpleName());
        List<R> entities = repository.findAll().stream()
                .filter(e -> !e.getIsDeleted())
                .map(this::getOne)
                .collect(Collectors.toList());
        logger.debug("Fetched entities: {}", entities);
        return entities;
    }

    @Override
    @Transactional
    @LogExecution
    public List<R> getAllByIds(List<Long> ids) {
        logger.debug("Fetching entities by IDs: {}", ids);
        List<R> entities = repository.findAllById(ids).stream()
                .filter(e -> !e.getIsDeleted())
                .map(this::getOne)
                .collect(Collectors.toList());
        logger.debug("Fetched entities by IDs: {}", entities);
        return entities;
    }

    @Override
    @Transactional
    @LogExecution
    public List<Long> getAllToIds(List<E> entities) {
        logger.debug("Fetching IDs for entities: {}", entities);
        List<Long> ids = entities.stream()
                .map(this::getOneToId)
                .collect(Collectors.toList());
        logger.debug("Fetched IDs: {}", ids);
        return ids;
    }

    @Override
    @Transactional
    @LogExecution
    public List<R> getAllToDto(List<E> entities) {
        logger.debug("Converting entities to DTOs: {}", entities);
        List<R> dtos = entities.stream()
                .map(this::getOne)
                .collect(Collectors.toList());
        logger.debug("Converted to DTOs: {}", dtos);
        return dtos;
    }

    @Override
    @Transactional
    @LogExecution
    public PagedResponse<R> getAllByPage(Integer page, Integer size) {
        logger.debug("Fetching paged entities - Page: {}, Size: {}", page, size);
        GenericUtils.validatePageNumberAndSize(page, size);
        logger.debug("Verifiecation okay");
        Pageable pageable = PageRequest.of(page, size, Sort.Direction.DESC, AppConstants.PERIODE_FILTABLE_FIELD);
        logger.debug("Cration de la pagination: {}", pageable);
        Page<E> list = repository.findAllByIsDeleted(false, pageable);
        logger.debug("Recuperation de la liste de donnees: {}", list);
        if (list.isEmpty()) {
            logger.debug("No entities found in paged request");
            throw new RessourceNotFoundException("La recherche de " + entityClass.getSimpleName() + " est vide!");
        }
        logger.debug("Entities found in paged request");
        List<R> listDto = list.getContent().stream()
                .map(this::getOne)
                .collect(Collectors.toList());
        logger.debug("Construction du dto: {}", listDto);
        PagedResponse<R> response = new PagedResponse<>(listDto, list.getNumber(), list.getSize(), list.getTotalElements(), list.getTotalPages(), list.isLast());
        logger.debug("Paged response created: {}", response);
        return response;
    }

    @Override
    @Transactional
    @LogExecution
    public E getById(Long id) {
        logger.debug("Fetching entity by ID: {}", id);
        E entity = repository.findById(id).filter(e -> !e.getIsDeleted()).orElse(null);
        logger.debug("Fetched entity: {}", entity);
        return entity;
    }

    @Override
    @Transactional
    @LogExecution
    public R getOne(E entity) {
        logger.debug("Converting entity to DTO: {}", entity);
        R dto = mapper.toDto(entity);
        logger.debug("Converted DTO: {}", dto);
        return dto;
    }

    @Override
    @Transactional
    @LogExecution
    public Long getOneToId(E entity) {
        Long id = entity != null ? entity.getId() : null;
        logger.debug("Fetched ID for entity: {}", id);
        return id;
    }

    @Override
    @Transactional
    @LogExecution
    public void delete(Boolean isAdmin, Long id) {
        logger.debug("Deleting entity with ID: {} as Admin: {}", id, isAdmin);
        if (!exist(id)) {
            logger.debug("Entity with ID: {} does not exist", id);
            throw new RessourceNotFoundException("La ressource " + entityClass.getSimpleName() + " avec l'id " + id + " n'existe pas");
        }
        E entity = getById(id);
        if (isAdmin) {
            repository.deleteById(entity.getId());
            logger.debug("Entity deleted from repository");
        } else {
            entity.setIsDeleted(true);
            repository.save(entity);
            logger.debug("Entity soft deleted");
        }
    }

    @Override
    @Transactional
    @LogExecution
    public void deleteAll(Boolean isAdmin, List<Long> ids) {
        logger.debug("Deleting entities with IDs: {} as Admin: {}", ids, isAdmin);
        ids.forEach(id -> delete(isAdmin, id));
        logger.debug("All specified entities deleted");
    }
}
