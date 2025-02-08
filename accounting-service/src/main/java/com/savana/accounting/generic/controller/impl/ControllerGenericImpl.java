package com.savana.accounting.generic.controller.impl;

import  com.savana.accounting.generic.controller.ControllerGeneric;
import  com.savana.accounting.generic.dto.reponse.BaseResponse;
import  com.savana.accounting.generic.dto.reponse.PagedResponse;
import  com.savana.accounting.generic.dto.reponse.RessourceResponse;
import  com.savana.accounting.generic.dto.request.BaseRequest;
import  com.savana.accounting.generic.entity.audit.BaseEntity;
import  com.savana.accounting.generic.service.ServiceGeneric;
import  com.savana.accounting.generic.utils.AppConstants;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@RefreshScope
@ResponseBody
@RestController
@CrossOrigin("*")
public abstract class ControllerGenericImpl<D extends BaseRequest, R extends BaseResponse, E extends BaseEntity<E, D>>
        implements ControllerGeneric<D, R, E> {

    private final ServiceGeneric<D, R, E> service;

    protected ControllerGenericImpl(ServiceGeneric<D, R, E> service) {
        this.service = service;
    }

    protected abstract E newInstance();

    @Override
    @PostMapping(value = "/create", produces = MediaType.APPLICATION_JSON_VALUE)
    @SecurityRequirement(name = "Authorization")
    @Operation(summary = "Enregistrer une entité")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Entité enregistrée avec succès", content = @Content),
            @ApiResponse(responseCode = "400", description = "Requête invalide", content = @Content),
            @ApiResponse(responseCode = "404", description = "Entité non trouvée", content = @Content)})
    public ResponseEntity<RessourceResponse<R>> save(@Valid @RequestBody D dto) {
        E entity = newInstance();
        log.info("Enregistrement de {} avec les données : {}", entity.getEntityName(), dto);
        return new ResponseEntity<>(new RessourceResponse<>("Entité enregistrée avec succès !", service.save(dto)), HttpStatus.CREATED);
    }

    @Override
    @PostMapping(value = "/create/all", produces = MediaType.APPLICATION_JSON_VALUE)
    @SecurityRequirement(name = "Authorization")
    @Operation(summary = "Enregistrer une liste d'entités")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Liste d'entités enregistrées avec succès", content = @Content),
            @ApiResponse(responseCode = "400", description = "Requête invalide", content = @Content),
            @ApiResponse(responseCode = "404", description = "Entité non trouvée", content = @Content)})
    public ResponseEntity<RessourceResponse<List<R>>> saveAll(@NotEmpty @Valid @RequestBody List<D> dtos) {
        E entity = newInstance();
        log.info("Enregistrement des {} avec les données : {}", entity.getEntityName(), dtos);
        return new ResponseEntity<>(new RessourceResponse<>("Entités enregistrées avec succès !", service.saveAll(dtos)), HttpStatus.CREATED);
    }

    @Override
    @DeleteMapping(value = "/delete/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @SecurityRequirement(name = "Authorization")
    @Operation(summary = "Supprimer une entité par son identifiant")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Entité supprimée avec succès", content = @Content),
            @ApiResponse(responseCode = "400", description = "Identifiant invalide", content = @Content),
            @ApiResponse(responseCode = "404", description = "Entité non trouvée", content = @Content)})
    public ResponseEntity<RessourceResponse<R>> deleteById(@NotNull @PathVariable("id") Long id) {
        E entity = newInstance();
        log.info("Suppression de {} avec l'identifiant : {}", entity.getEntityName(), id);
        service.delete(false, id);
        return new ResponseEntity<>(new RessourceResponse<>("Entité supprimée avec succès !"), HttpStatus.OK);
    }

    @Override
    @DeleteMapping(value = "/delete/all", produces = MediaType.APPLICATION_JSON_VALUE)
    @SecurityRequirement(name = "Authorization")
    @Operation(summary = "Supprimer une liste d'entités par leurs identifiants")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Liste d'entités supprimées avec succès", content = @Content),
            @ApiResponse(responseCode = "400", description = "Identifiants invalides", content = @Content),
            @ApiResponse(responseCode = "404", description = "Entités non trouvées", content = @Content)})
    public ResponseEntity<RessourceResponse<R>> deleteAll(@NotEmpty @RequestParam(value = "ids") List<Long> ids) {
        E entity = newInstance();
        log.info("Suppression des {} avec les identifiants : {}", entity.getEntityName(), ids);
        service.deleteAll(false, ids);
        return new ResponseEntity<>(new RessourceResponse<>("Entités supprimées avec succès !"), HttpStatus.OK);
    }

    @Override
    @GetMapping(value = "/find/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @SecurityRequirement(name = "Authorization")
    @Operation(summary = "Récupérer une entité par son identifiant")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Entité trouvée avec succès", content = @Content),
            @ApiResponse(responseCode = "400", description = "Identifiant invalide", content = @Content),
            @ApiResponse(responseCode = "404", description = "Entité non trouvée", content = @Content)})
    public ResponseEntity<RessourceResponse<R>> getOne(@NotNull @PathVariable("id") Long id) {
        E entity = newInstance();
        log.info("Récupération de {} avec l'identifiant : {}", entity.getEntityName(), id);
        E response = service.getById(id);
        if (entity == null) {
            return new ResponseEntity<>(new RessourceResponse<>("La ressource " + entity.getEntityName() + " avec l'id " + id + " n'existe pas"), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new RessourceResponse<>("Entité trouvée avec succès !", service.getOne(response)), HttpStatus.OK);
        }
    }

    @Override
    @GetMapping(value = "/find/all", produces = MediaType.APPLICATION_JSON_VALUE)
    @SecurityRequirement(name = "Authorization")
    @Operation(summary = "Récupérer la liste de toutes les entités")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Liste d'entités trouvée avec succès", content = @Content),
            @ApiResponse(responseCode = "404", description = "Entités non trouvées", content = @Content)})
    public ResponseEntity<RessourceResponse<List<R>>> getAll() {
        E entity = newInstance();
        log.info("Récupération de toutes les {}", entity.getEntityName());
        return new ResponseEntity<>(new RessourceResponse<>("Entités trouvées avec succès !", service.getAll()), HttpStatus.OK);
    }

    @Override
    @GetMapping(value = "/find/by", produces = MediaType.APPLICATION_JSON_VALUE)
    @SecurityRequirement(name = "Authorization")
    @Operation(summary = "Récupérer une liste d'entités par leurs identifiants")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Liste d'entités trouvée avec succès", content = @Content),
            @ApiResponse(responseCode = "404", description = "Entités non trouvées", content = @Content)})
    public ResponseEntity<RessourceResponse<List<R>>> getAllByIds(@NotEmpty @RequestParam(value = "ids") List<Long> ids) {
        E entity = newInstance();
        log.info("Récupération des {} avec les identifiants : {}", entity.getEntityName(), ids);
        return new ResponseEntity<>(new RessourceResponse<>("Entités trouvées avec succès !", service.getAllByIds(ids)), HttpStatus.OK);
    }

    @Override
    @GetMapping(value = "/page", produces = MediaType.APPLICATION_JSON_VALUE)
    @SecurityRequirement(name = "Authorization")
    @Operation(summary = "Récupérer la liste d'entités par page")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Liste d'entités trouvée avec succès", content = @Content),
            @ApiResponse(responseCode = "404", description = "Entités non trouvées", content = @Content)})
    public ResponseEntity<RessourceResponse<PagedResponse<R>>> getByPage(
            @RequestParam(name = "page", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) Integer page,
            @RequestParam(name = "size", defaultValue = AppConstants.DEFAULT_PAGE_SIZE) Integer size) {
        E entity = newInstance();
        log.info("Récupération des {} avec pagination : page={}, taille={}", entity.getEntityName(), page, size);
        return new ResponseEntity<>(new RessourceResponse<>("Entités trouvées avec succès !", service.getAllByPage(page, size)), HttpStatus.OK);
    }

    @Override
    @PutMapping(value = "/update/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @SecurityRequirement(name = "Authorization")
    @Operation(summary = "Mettre à jour une entité par son identifiant")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Entité mise à jour avec succès", content = @Content),
            @ApiResponse(responseCode = "400", description = "Identifiant invalide", content = @Content),
            @ApiResponse(responseCode = "404", description = "Entité non trouvée", content = @Content)})
    public ResponseEntity<RessourceResponse<R>> update(@Valid @RequestBody D dto, @PathVariable("id") Long id) {
        E entity = newInstance();
        log.info("Mise à jour de {} avec les données : {}, identifiant : {}", entity.getEntityName(), dto, id);
        return new ResponseEntity<>(new RessourceResponse<>("Entité mise à jour avec succès !", service.update(dto, id)), HttpStatus.OK);
    }

    @PostMapping(value = "/sync", produces = MediaType.APPLICATION_JSON_VALUE)
    @SecurityRequirement(name = "Authorization")
    @Operation(summary = "Synchroniser les données locales avec le serveur")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Synchronisation réussie", content = @Content),
            @ApiResponse(responseCode = "400", description = "Requête invalide", content = @Content),
            @ApiResponse(responseCode = "500", description = "Erreur serveur", content = @Content)
    })
    public ResponseEntity<RessourceResponse<List<R>>> syncData(@RequestBody @Valid List<D> dtos) {
        try {
            List<R> responses = dtos.stream()
                    .map(dto -> {
                        if (dto.isAdd()) {
                            return service.save(dto);  // Ajouter une nouvelle entrée
                        } else if (dto.isUpdated()) {
                            return service.update(dto, dto.getId());  // Mettre à jour une entrée existante
                        } else if (dto.isDeleted()) {
                            service.delete(false, dto.getId());  // Supprimer une entrée
                            return null;
                        }
                        return null;
                    })
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());

            return new ResponseEntity<>(new RessourceResponse<>("Synchronisation réussie", responses), HttpStatus.OK);
        } catch (Exception e) {
            log.error("Erreur lors de la synchronisation :", e);
            return new ResponseEntity<>(new RessourceResponse<>("Échec de la synchronisation"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
