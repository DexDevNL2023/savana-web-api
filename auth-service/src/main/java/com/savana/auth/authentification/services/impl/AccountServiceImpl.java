package com.savana.auth.authentification.services.impl;

import com.savana.auth.authentification.dto.reponse.AccountResponse;
import com.savana.auth.authentification.dto.reponse.JwtAuthenticationResponse;
import com.savana.auth.authentification.dto.request.*;
import com.savana.auth.authentification.entities.*;
import com.savana.auth.authentification.mapper.AccountMapper;
import com.savana.auth.authentification.repositories.AccountRepository;
import com.savana.auth.authentification.security.jwt.JwtUtils;
import com.savana.auth.authentification.services.AccountService;
import com.savana.auth.generic.exceptions.RessourceNotFoundException;
import com.savana.auth.generic.logging.LogExecution;
import com.savana.auth.generic.service.impl.ServiceGenericImpl;
import com.savana.auth.generic.utils.GenericUtils;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Slf4j
@Service
@Transactional
public class AccountServiceImpl extends ServiceGenericImpl<AccountRequest, AccountResponse, Account> implements AccountService, UserDetailsService {

    private final AccountRepository repository;
    private final PasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder(10);;
    private final JwtUtils jwtUtils;
    private final AccountMapper mapper;

    public AccountServiceImpl(AccountRepository repository, AccountMapper mapper, JwtUtils jwtUtils) {
        super(Account.class, repository, mapper);
        this.repository = repository;
        this.jwtUtils = jwtUtils;
        this.mapper = mapper;
    }

    @Transactional
    @LogExecution
    @Override
    public AccountResponse register(SignupRequest dto) {
        log.debug("Register avec "+dto.getEmail());
        if (dto.getEmail() == null || dto.getEmail().isEmpty() || !GenericUtils.isValidEmailAddress(dto.getEmail())) {
            throw new RessourceNotFoundException("L'email " + (dto.getEmail() == null ? "null" : dto.getEmail()) + " est invalide.");
        }

        log.debug("1");
        //Verifying whether account already exists
        if (repository.existsByEmail(dto.getEmail()))
            throw new RessourceNotFoundException("Un compte existe déjà avec cette e-mail " + dto.getEmail());
        log.debug("2");
        // Create new account's account
        // Mapper Dto
        Account newAccount = mapper.toEntity(dto);
        log.debug("3");
        String accountPassword = bCryptPasswordEncoder.encode(dto.getPassword());
        log.debug("4");
        // le mot de passe est vide, donc le compte a été crée par quelqu'un d'autre et c'est sa première connexion
        if (dto.getPassword().isEmpty() || dto.getPassword().equals(bCryptPasswordEncoder.encode(""))) {
            // on chiffre et enregistre le mot de passe envoyé
            newAccount.setPassword(bCryptPasswordEncoder
                    .encode(GenericUtils.generatedPassWord()));
            log.debug("5");
        } else {
            newAccount.setPassword(accountPassword);
            log.debug("6");
        }

        newAccount = repository.save(newAccount);
        log.debug("Account {}", newAccount);

        return getOne(newAccount);
    }

    @Transactional
    @LogExecution
    @Override
    public JwtAuthenticationResponse login(LoginRequest dto) {
        //Verifying whether account already exists
        if (!repository.existsByEmail(dto.getEmail()))
            throw new RessourceNotFoundException("Aucun compte n'existe avec le nom d'utilisateur : " + dto.getEmail());
        Account loginAccount = repository.findByEmail(dto.getEmail())
                .orElseThrow(() -> new RessourceNotFoundException("L'utilisateur" + dto.getEmail() + " est introuvable."));
        // le mot de passe est vide, donc le compte a été crée par quelqu'un d'autre et c'est sa première connexion
        if (loginAccount.getPassword() == null || loginAccount.getPassword().isEmpty() ||
                bCryptPasswordEncoder.matches("", loginAccount.getPassword())) {
            loginAccount.setPassword(bCryptPasswordEncoder.encode(dto.getPassword()));
        } else if (!bCryptPasswordEncoder.matches(dto.getPassword(), loginAccount.getPassword())) {
            throw new RessourceNotFoundException("Nom d'utilisateur ou mot de passe incorrect.");
        }
        // générer le JWT
        String jwt = jwtUtils.generateJwtTokens(loginAccount);
        loginAccount.setAccesToken(jwt);
        // Create an Authentication object manually
        Authentication authentication = new UsernamePasswordAuthenticationToken(
                loginAccount.getUsername(), loginAccount.getPassword(), loginAccount.getAuthorities());
        // Manually set the authentication in the SecurityContext
        SecurityContextHolder.getContext().setAuthentication(authentication);
        // Update account's account
        repository.save(loginAccount);
        // Mapper Dto avec l'attribut accesToken correctement rempli
        return new JwtAuthenticationResponse(jwt);
    }

    @Transactional
    @LogExecution
    @Override
    public Boolean logout() {
        //Verifying whether account already exists
        Account logoutAccount = loadCurrentUser();
        // Update account's account
        logoutAccount.setAccesToken("");
        repository.save(logoutAccount);
        SecurityContextHolder.clearContext();
        return true;
    }

    @Transactional
    @LogExecution
    @Override
    public AccountResponse save(AccountRequest dto) {
        if (dto.getEmail() == null || dto.getEmail().isEmpty() || !GenericUtils.isValidEmailAddress(dto.getEmail())) {
            throw new RessourceNotFoundException("L'email " + (dto.getEmail() == null ? "null" : dto.getEmail()) + " est invalide.");
        }
        log.debug("1");
        //Verifying whether account already exists
        if (repository.existsByEmail(dto.getEmail()))
            throw new RessourceNotFoundException("Un compte existe déjà avec cette e-mail " + dto.getEmail());
        log.debug("2");
        // Create new account's account
        // Mapper Dto
        Account newAccount = mapper.toEntity(dto);
        log.debug("4");
        newAccount.setPassword(bCryptPasswordEncoder.encode(""));
        log.debug("5");

        newAccount = repository.save(newAccount);
        log.debug("Account {}", newAccount);

        return getOne(newAccount);
    }

    @Transactional
    @LogExecution
    @Override
    public AccountResponse update(AccountRequest dto, Long id) {
        log.debug("updating account : {}", dto);
        //Verifying whether account already exists
        Account updatedAccount = getById(id);
        log.debug("getById account : {}", updatedAccount);
        // Mapper Dto
        if (updatedAccount.equalsToDto(dto))
            throw new RessourceNotFoundException("L'utilisateur avec les données suivante : " + dto.toString() + " existe déjà");
        log.debug("equalsToDto account : okay");
        updatedAccount.update(mapper.toEntity(dto));
        log.debug("update account : okay");
        // Update account's
        updatedAccount = repository.save(updatedAccount);
        log.debug("updatedAccount : {}", updatedAccount);
        // Mapper Dto
        AccountResponse response = getOne(updatedAccount);
        log.debug("Mapper Dto : {}", response);
        return response;
    }

    @Transactional
    @LogExecution
    @Override
    public AccountResponse changePassword(ChangePasswordRequest dto) {
        //Verifying whether account already exists
        Account updatedAccount = loadCurrentUser();
        if (!bCryptPasswordEncoder.matches(dto.getCurrentPassword(), updatedAccount.getPassword()))
            throw new RessourceNotFoundException("Votre mot de passe actuel est différent de celui que vous avez renseigner!");
        // Update account's account with new password
        updatedAccount.setPassword(bCryptPasswordEncoder.encode(dto.getNewPassword()));
        updatedAccount = repository.save(updatedAccount);
        // Mapper Dto
        return getOne(updatedAccount);
    }

    @Transactional
    @LogExecution
    @Override
    public Account loadCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated() || authentication instanceof AnonymousAuthenticationToken) {
            throw new RessourceNotFoundException("Impossible de retouver l'utilisateur courant!");
        }
        UserDetails userPrincipal = (UserDetails) authentication.getPrincipal();
        return repository.findByEmail(userPrincipal.getUsername())
                .orElseThrow(() -> new RessourceNotFoundException("Aucun utilisateur n'existe avec le nom utilisateur " + userPrincipal.getUsername()));
    }

    @Transactional
    @LogExecution
    @Override
    public AccountResponse getCurrentUser() {
        //Verifying whether account already exists
        Account currentAccount = loadCurrentUser();
        // Mapper Dto
        return getOne(currentAccount);
    }

    @Transactional
    @LogExecution
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Account accountUser = repository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Nom utilisateur invalide"));
        return new User(accountUser.getUsername(), accountUser.getPassword(), accountUser.getAuthorities());
    }

    @Transactional
    @LogExecution
    @Override
    public Boolean forgotPassword(String email) {
        Account accountAccount = repository.findByEmail(email)
                .orElseThrow(() -> new RessourceNotFoundException("Impossible de trouver un compte utilisateur avec l'e-mail  " + email));
        // Genered token
        String token = GenericUtils.generateTokenNumber();
        accountAccount.setResetPasswordToken(token);
        accountAccount = repository.save(accountAccount);
        log.debug("Account {}", accountAccount);
        return true;
    }

    @Transactional
    @LogExecution
    @Override
    public Boolean resetPassword(ResetPasswordRequest resetPasswordRequest) {
        Account accountAccount = repository.findByResetPasswordToken(resetPasswordRequest.getToken())
                .orElseThrow(() -> new RessourceNotFoundException("Impossible de trouver un compte utilisateur avec le jeton " + resetPasswordRequest.getToken()));
        String encodedPassword = bCryptPasswordEncoder.encode(resetPasswordRequest.getNewPassword());
        accountAccount.setPassword(encodedPassword);
        accountAccount.setResetPasswordToken(null);
        accountAccount = repository.save(accountAccount);
        log.debug("Account {}", accountAccount);
        return true;
    }

    @Transactional
    @LogExecution
    @Override
    public Account saveDefault(Account newAccount) {
        if (newAccount.getEmail() == null || newAccount.getEmail().isEmpty() || !GenericUtils.isValidEmailAddress(newAccount.getEmail())) {
            throw new RessourceNotFoundException("L'email " + (newAccount.getEmail() == null ? "null" : newAccount.getEmail()) + " est invalide.");
        }

        //Verifying whether account already exists
        if (repository.existsByEmail(newAccount.getEmail()))
            throw new RessourceNotFoundException("Un compte existe déjà avec cette e-mail " + newAccount.getEmail());

        newAccount = repository.save(newAccount);
        log.debug("Account {}", newAccount);
        return newAccount;
    }
}