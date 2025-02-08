package com.savana.auth.generic.service;

import com.savana.auth.generic.dto.reponse.BaseResponse;
import com.savana.auth.generic.dto.reponse.PagedResponse;
import com.savana.auth.generic.dto.request.BaseRequest;
import com.savana.auth.generic.entity.audit.BaseEntity;

import java.util.List;

public interface ServiceGeneric<D extends BaseRequest, R extends BaseResponse, E extends BaseEntity<E, D>> {
    R save(D dto);

    E saveDefault(E entity);

    List<R> saveAll(List<D> dtos);

    void delete(Boolean isAdmin, Long id);

    void deleteAll(Boolean isAdmin, List<Long> ids);

    Boolean exist(Long id);

    E getById(Long id);

    R getOne(E entity);

    Long getOneToId(E entity);

    List<R> getAll();

    List<R> getAllByIds(List<Long> ids);

    List<Long> getAllToIds(List<E> entities);

    List<R> getAllToDto(List<E> entities);

    PagedResponse<R> getAllByPage(Integer page, Integer size);

    R update(D dto, Long id);
}
