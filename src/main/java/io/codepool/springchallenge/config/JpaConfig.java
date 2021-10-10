package io.codepool.springchallenge.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import javax.sql.DataSource;


/**
 * The Jpa configuration.
 * Define the jdbc connection url and all other data source configuration properties
 * like the db connection pool in our case.
 * Hikari pool might be an overkill in this case but it's always a good practice to start with
 * a good foundation, rather than having to walk back when our app grows :)
 */
@Configuration
@EntityScan(basePackages = {"io.codepool.springchallenge.dao.model"})
@EnableJpaRepositories(basePackages = {"io.codepool.springchallenge.dao.repository"})
public class JpaConfig{

    /**
     * Data source bean.
     *
     * @return the data source
     */
    @Bean
    @Primary
    public DataSource dataSource() {
        return buildDataSource(
                "PrimaryHikariPool",
                getPrimaryDataSourceUrl(), getUserName(), getPassword());
    }


    /**
     * Primary data source connection string.
     */
    @Value("${spring.datasource.url}")
    private String primaryDataSourceUrl;

    /**
     * Datasource username.
     */
    @Value("${spring.datasource.username}")
    private  String userName;

    /**
     * Datasource password.
     */
    @Value("${spring.datasource.password}")
    private String password;

    /**
     * Hikari Maximum Pool Size.
     */
    @Value("${spring.datasource.hikari.maximumPoolSize}")
    private int hikariMaximumPoolSize;

    /**
     * Hikari Idle Timeout.
     */
    @Value("${spring.datasource.hikari.idleTimeout}")
    private int hikariIdleTimeOut;

    /**
     * Hikari Maximum Life Time.
     */
    @Value("${spring.datasource.hikari.maxLifetime}")
    private int hikariMaxLifeTime;


    /**
     * Hikari Connection Timeout.
     */
    @Value("${spring.datasource.hikari.connectionTimeout}")
    private int hikariConnectionTimeout;

    /**
     *Hikari based Data source.
     * @param poolName Hikari Connection pool name.
     * @param url Database connection string.
     * @param username Username
     * @param password Password
     * @return Datasource.
     */
    protected DataSource buildDataSource(String poolName,
                                         String url,
                                         String username,
                                         String password) {
        final HikariConfig hikariConfig = new HikariConfig();

        hikariConfig.setPoolName(poolName);
        hikariConfig.setJdbcUrl(url);
        hikariConfig.setUsername(username);
        hikariConfig.setPassword(password);
        hikariConfig.setMaximumPoolSize(hikariMaximumPoolSize);
        hikariConfig.setIdleTimeout(hikariIdleTimeOut);
        hikariConfig.setMaxLifetime(hikariMaxLifeTime);
        hikariConfig.setConnectionTimeout(hikariConnectionTimeout);

        return new HikariDataSource(hikariConfig);

    }


    public String getPrimaryDataSourceUrl() {
        return primaryDataSourceUrl;
    }

    public void setPrimaryDataSourceUrl(String primaryDataSourceUrl) {
        this.primaryDataSourceUrl = primaryDataSourceUrl;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}

