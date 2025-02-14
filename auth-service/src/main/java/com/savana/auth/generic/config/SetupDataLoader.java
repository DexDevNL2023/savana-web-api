package com.savana.auth.generic.config;

import com.savana.auth.authentification.entities.Account;
import com.savana.auth.authentification.services.AccountService;
import com.savana.auth.generic.logging.LogExecution;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class SetupDataLoader implements ApplicationListener<ContextRefreshedEvent> {

    private boolean alreadySetup = false;
    private final AccountService accountService;

    public SetupDataLoader(AccountService accountService) {
        this.accountService = accountService;
    }

    @Transactional
    @LogExecution
    @Override
    public void onApplicationEvent(final ContextRefreshedEvent event) {
        if (alreadySetup) {
            return;
        }

        alreadySetup = true;
        //addDefaultUsers();
    }

    private void addDefaultUsers() {
        Account account = new Account();
        account.setName("Dexter");
        account.setEmail("dexternlang@gmail.com");
        accountService.saveDefault(account);
    }
}