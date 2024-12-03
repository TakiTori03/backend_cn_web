package com.hust.Ecommerce.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableJpaRepositories({ "com.hust.Ecommerce.repositories" })
@EnableTransactionManagement
@EnableJpaAuditing
public class DatabaseConfiguration {

}
