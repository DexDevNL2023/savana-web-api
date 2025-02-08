package com.savana.auth.generic.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@Configuration
@EnableMongoRepositories(basePackages = {"com.savana.auth.authentification.repositories"})
public class DatabaseConfig {
}
