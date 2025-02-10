package com.savana.accounting.compte.mapper;

import com.savana.accounting.compte.dto.reponse.CompteResponse;
import com.savana.accounting.compte.dto.request.CompteRequest;
import com.savana.accounting.compte.entities.Compte;
import com.savana.accounting.generic.mapper.GenericMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CompteMapper extends GenericMapper<CompteRequest, CompteResponse, Compte> {

    @Mapping(target = "journaux", ignore = true)
    Compte toEntity(CompteRequest dto);

    @Mapping(target = "journaux", ignore = true)
    CompteResponse toDto(Compte entity);
}
