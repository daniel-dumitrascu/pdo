package org.projects.centralpoint.Database.Main;

import org.hibernate.SessionFactory;
import org.projects.centralpoint.Settings.AppSettings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.*;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import java.io.File;
import java.util.Properties;

@Configuration
@EnableTransactionManagement
@ComponentScan({ "org.projects.centralpoint" })
@PropertySource(value = { "classpath:application.properties" })
public class PersistenceConfigMain
{
    @Autowired
    private Environment environment;

    @Autowired
    private AppSettings appSettings;

    @Bean
    public LocalSessionFactoryBean sessionFactory(DataSource dataSource)
    {
        LocalSessionFactoryBean sessionFactory = new LocalSessionFactoryBean();
        sessionFactory.setDataSource(dataSource);
        sessionFactory.setPackagesToScan(new String[] { "org.projects.centralpoint" });
        sessionFactory.setHibernateProperties(hibernateProperties());
        return sessionFactory;
    }

    @Bean
    public DataSource dataSource()
    {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(environment.getRequiredProperty("jdbc.driverClassName"));
        dataSource.setUrl(environment.getRequiredProperty("jdbc.main.url"));
        dataSource.setUsername(environment.getRequiredProperty("jdbc.username"));
        dataSource.setPassword(environment.getRequiredProperty("jdbc.password"));
        return dataSource;
    }

    private Properties hibernateProperties()
    {
        Properties properties = new Properties();
        properties.put("hibernate.dialect", environment.getRequiredProperty("hibernate.dialect"));
        properties.put("hibernate.show_sql", environment.getRequiredProperty("hibernate.show_sql"));
        properties.put("hibernate.format_sql", environment.getRequiredProperty("hibernate.format_sql"));
        return properties;
    }

    @Bean
    @Autowired
    public HibernateTransactionManager transactionManager(SessionFactory s)
    {
        HibernateTransactionManager txManager = new HibernateTransactionManager();
        txManager.setSessionFactory(s);
        return txManager;
    }

    @PostConstruct
    public void init()
    {
        appSettings.setDbResourcesPath(environment.getRequiredProperty("imageResourceMain"));
        createResourceDirStruct();
    }

    private void createResourceDirStruct()
    {
        String rootPath = environment.getRequiredProperty("imageResourceMain");
        File rootDir = new File(rootPath);
        File moviesDir = new File(rootPath + "/movies/");
        File actorsDir = new File(rootPath + "/actors/");
        File moviesPosterDir = new File(rootPath + "/movies/posters/");
        File actorsPosterDir = new File(rootPath + "/actors/posters/");

        if(!rootDir.exists()) { rootDir.mkdir(); }
        if(!moviesDir.exists()) { moviesDir.mkdir(); }
        if(!actorsDir.exists()) { actorsDir.mkdir(); }
        if(!moviesPosterDir.exists()) { moviesPosterDir.mkdir(); }
        if(!actorsPosterDir.exists()) { actorsPosterDir.mkdir(); }
    }
}