package com.savana.accounting.rest.config;

import com.savana.accounting.generic.controller.ControllerGeneric;
import com.savana.accounting.generic.dto.reponse.BaseResponse;
import com.savana.accounting.generic.dto.reponse.PagedResponse;
import com.savana.accounting.generic.dto.reponse.RessourceResponse;
import com.savana.accounting.generic.dto.request.BaseRequest;
import com.savana.accounting.generic.entity.audit.BaseEntity;
import com.savana.accounting.rest.client.UserService;
import com.savana.accounting.rest.model.UserResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

public class GenericFallback<D extends BaseRequest, R extends BaseResponse, E extends BaseEntity<E, D>> implements ControllerGeneric<D, R, E>, UserService {

    private static final Logger logger = LoggerFactory.getLogger(GenericFallback.class);
    private final Throwable cause;

    public GenericFallback(Throwable cause) {
        this.cause = cause;
    }

    @Override
    public Optional<String> getCurrentUserLogin() {
        logger.error("La méthode distante" + cause.getClass().getName() + " a échoué à cause de : {}", cause.getMessage());
        return Optional.empty();
    }

    @Override
    public Optional<String> getCurrentUserJWT() {
        logger.error("La méthode distante" + cause.getClass().getName() + " a échoué à cause de : {}", cause.getMessage());
        return Optional.empty();
    }

    @Override
    public UserResponse getUserById(Long id) {
        logger.error("La méthode distante" + cause.getClass().getName() + " a échoué à cause de : {}", cause.getMessage());
        return null;
    }

    @Override
    public ResponseEntity<RessourceResponse<R>> save(D dto) {
        logger.error("La méthode distante" + cause.getClass().getName() + " a échoué à cause de : {}", cause.getMessage());
        return new ResponseEntity<>(new RessourceResponse(false, "La méthode distante" + cause.getClass().getName() + " a échoué à cause de : {}" + cause.getMessage() + ". Veuillew essayer plutard!!!"), HttpStatus.SERVICE_UNAVAILABLE);
    }

    @Override
    public ResponseEntity<RessourceResponse<List<R>>> saveAll(List<D> dtos) {
        logger.error("La méthode distante" + cause.getClass().getName() + " a échoué à cause de : {}", cause.getMessage());
        return new ResponseEntity<>(new RessourceResponse(false, "La méthode distante" + cause.getClass().getName() + " a échoué à cause de : {}" + cause.getMessage() + ". Veuillew essayer plutard!!!"), HttpStatus.SERVICE_UNAVAILABLE);
    }

    @Override
    public ResponseEntity<RessourceResponse<R>> deleteById(Long id) {
        logger.error("La méthode distante" + cause.getClass().getName() + " a échoué à cause de : {}", cause.getMessage());
        return new ResponseEntity<>(new RessourceResponse(false, "La méthode distante" + cause.getClass().getName() + " a échoué à cause de : {}" + cause.getMessage() + ". Veuillew essayer plutard!!!"), HttpStatus.SERVICE_UNAVAILABLE);
    }

    @Override
    public ResponseEntity<RessourceResponse<R>> deleteAll(List<Long> ids) {
        logger.error("La méthode distante" + cause.getClass().getName() + " a échoué à cause de : {}", cause.getMessage());
        return new ResponseEntity<>(new RessourceResponse(false, "La méthode distante" + cause.getClass().getName() + " a échoué à cause de : {}" + cause.getMessage() + ". Veuillew essayer plutard!!!"), HttpStatus.SERVICE_UNAVAILABLE);
    }

    @Override
    public ResponseEntity<RessourceResponse<R>> getOne(Long id) {
        logger.error("La méthode distante" + cause.getClass().getName() + " a échoué à cause de : {}", cause.getMessage());
        return new ResponseEntity<>(new RessourceResponse(false, "La méthode distante" + cause.getClass().getName() + " a échoué à cause de : {}" + cause.getMessage() + ". Veuillew essayer plutard!!!"), HttpStatus.SERVICE_UNAVAILABLE);
    }

    @Override
    public ResponseEntity<RessourceResponse<List<R>>> getAll() {
        logger.error("La méthode distante" + cause.getClass().getName() + " a échoué à cause de : {}", cause.getMessage());
        return new ResponseEntity<>(new RessourceResponse(false, "La méthode distante" + cause.getClass().getName() + " a échoué à cause de : {}" + cause.getMessage() + ". Veuillew essayer plutard!!!"), HttpStatus.SERVICE_UNAVAILABLE);
    }

    @Override
    public ResponseEntity<RessourceResponse<List<R>>> getAllByIds(List<Long> ids) {
        logger.error("La méthode distante" + cause.getClass().getName() + " a échoué à cause de : {}", cause.getMessage());
        return new ResponseEntity<>(new RessourceResponse(false, "La méthode distante" + cause.getClass().getName() + " a échoué à cause de : {}" + cause.getMessage() + ". Veuillew essayer plutard!!!"), HttpStatus.SERVICE_UNAVAILABLE);
    }

    @Override
    public ResponseEntity<RessourceResponse<PagedResponse<R>>> getByPage(Integer page, Integer size) {
        logger.error("La méthode distante" + cause.getClass().getName() + " a échoué à cause de : {}", cause.getMessage());
        return new ResponseEntity<>(new RessourceResponse(false, "La méthode distante" + cause.getClass().getName() + " a échoué à cause de : {}" + cause.getMessage() + ". Veuillew essayer plutard!!!"), HttpStatus.SERVICE_UNAVAILABLE);
    }

    @Override
    public ResponseEntity<RessourceResponse<R>> update(D dto, Long id) {
        logger.error("La méthode distante" + cause.getClass().getName() + " a échoué à cause de : {}", cause.getMessage());
        return new ResponseEntity<>(new RessourceResponse(false, "La méthode distante" + cause.getClass().getName() + " a échoué à cause de : {}" + cause.getMessage() + ". Veuillew essayer plutard!!!"), HttpStatus.SERVICE_UNAVAILABLE);
    }

    @Override
    public ResponseEntity<RessourceResponse<List<R>>> syncData(List<D> dtos) {
        logger.error("La méthode distante" + cause.getClass().getName() + " a échoué à cause de : {}", cause.getMessage());
        return new ResponseEntity<>(new RessourceResponse(false, "La méthode distante" + cause.getClass().getName() + " a échoué à cause de : {}" + cause.getMessage() + ". Veuillew essayer plutard!!!"), HttpStatus.SERVICE_UNAVAILABLE);
    }
}