package com.savana.accounting.generic.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@Configuration
@EnableMongoRepositories(basePackages = {"com.savana.accounting.account.repositories", "com.savana.accounting.journal.repositories"})
public class DatabaseConfig {
}
