package in.co.sa.inventory.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;
import java.util.Properties;

@Configuration
public class RCAConfig {

    @Value("${spring.datasource.driver-class-name}")
    public String driver;

    @Value("${spring.datasource.url}")
    public String url;

    @Value("${spring.datasource.username}")
    public String username;

    @Value("${spring.datasource.password}")
    public String password;

    @Value("${spring.jpa.properties.hibernate.dialect}")
    public String dialect;

    @Value("${spring.jpa.hibernate.ddl-auto}")
    public String ddl;

    @Primary
    @Bean(name = "dataSource")
    public DriverManagerDataSource dataSource() {

        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(driver);
        dataSource.setUrl(url);
        dataSource.setUsername(username);
        dataSource.setPassword(password);

        return dataSource;
    }

    @Bean
    public JdbcTemplate jdbcTemplate(@Qualifier("dataSource") DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }


    Properties hibernateProperties() {
        return new Properties() {
            {
                setProperty("hibernate.hbm2ddl.auto", ddl);
                setProperty("hibernate.connection.useUnicode", "true");
                setProperty("spring.jpa.hibernate.ddl-auto", ddl);
                setProperty("hibernate.dialect", dialect);
                setProperty("spring.jpa.properties.hibernate.dialect", dialect);
                setProperty("hibernate.globally_quoted_identifiers", "true");
                setProperty("hibernate.connection.CharSet", "utf8mb4");
                setProperty("hibernate.connection.characterEncoding", "utf8");

            }
        };
    }
}
