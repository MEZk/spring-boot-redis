package io.github.mezk.config;

import java.util.Properties;

import javax.sql.DataSource;

import org.hibernate.cfg.Environment;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

@Configuration
@EnableCaching
@EnableTransactionManagement
@EnableJpaRepositories(basePackages = "io.github.mezk.repository")
public class JpaConfig {

    @Bean
    public DataSource dataSource() {
        final HikariConfig cfg = new HikariConfig();
        cfg.setDriverClassName("org.postgresql.Driver");
        cfg.setJdbcUrl("jdbc:postgresql://localhost:5432/postgres");
        cfg.setUsername("postgres");
        cfg.setPassword("postgres");
        cfg.setConnectionTestQuery("select 1");
        cfg.setValidationTimeout(7000);
        cfg.setMinimumIdle(0);
        cfg.setIdleTimeout(15000);
        cfg.setMaximumPoolSize(100);
        cfg.setConnectionTimeout(30000);
        return new HikariDataSource(cfg);
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
        final LocalContainerEntityManagerFactoryBean entityManagerFactoryBean =
            new LocalContainerEntityManagerFactoryBean();
        entityManagerFactoryBean.setDataSource(dataSource());
        entityManagerFactoryBean.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
        entityManagerFactoryBean.setPackagesToScan("io.github.mezk.model");

        final Properties p = new Properties();
        p.put(Environment.DIALECT, "org.hibernate.dialect.PostgreSQL9Dialect");
        p.put(Environment.HBM2DDL_AUTO, "update");

        // Secondary Cache
        p.put(Environment.USE_SECOND_LEVEL_CACHE, true);
        p.put(Environment.USE_QUERY_CACHE, true);
        p.put(Environment.CACHE_REGION_FACTORY, org.hibernate.cache.redis.hibernate5.SingletonRedisRegionFactory.class.getName());
        p.put(Environment.CACHE_REGION_PREFIX, "hibernate");
        p.put(Environment.CACHE_PROVIDER_CONFIG, "hibernate-redis.properties");

        entityManagerFactoryBean.setJpaProperties(p);

        return entityManagerFactoryBean;
    }
}
