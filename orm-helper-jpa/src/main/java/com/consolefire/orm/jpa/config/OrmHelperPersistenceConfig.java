package com.consolefire.orm.jpa.config;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.annotation.Resource;
import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.dao.annotation.PersistenceExceptionTranslationPostProcessor;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.consolefire.orm.common.DataSourceTypeContext;
import com.consolefire.orm.jpa.config.OrmHelperPersistenceConfig.BeanConstants;
import com.consolefire.orm.jpa.helper.MasterSlaveRoutingDataSource;
import com.consolefire.orm.spring.support.ContextAutowireFactory;

/**
 * Created by sabuj.das on 14/04/16.
 */
@Configuration
@EnableTransactionManagement
public class OrmHelperPersistenceConfig implements ApplicationContextAware {

    public static Logger logger = LoggerFactory.getLogger(OrmHelperPersistenceConfig.class);

    public static final String PERSISTENCE_UNIT_NAME = "ORM_HELPER_MASTER_SLAVE_SWITCH_PERSISTENCE_UNIT";
    public interface BeanConstants {
        String ORM_HELPER_MASTER_DATA_SOURCE = "ormHelper.masterDataSource";
        String ORM_HELPER_SLAVE_DATA_SOURCE = "ormHelper.slaveDataSource";
        String ORM_HELPER_DEFAULT_DS_TYPE = "ormHelper.defaultDataSourceType";
        String ORM_HELPER_PACKAGES_TO_SCAN = "ormHelper.entityPackagesToScan";
        String ORM_HELPER_MAPPING_FILES = "ormHelper.mappingFiles";
        String ORM_HELPER_JPA_PROPERTIES = "ormHelper.jpaProperties";
    }
    
    private ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

//    @Component
//    public static class JpaProperties {
//        @Value("${orm.helper.persistence.properties.hbmToDDLAuto}")
//        private String hbmToDDLAuto;
//        @Value("${orm.helper.persistence.properties.showSql}")
//        private String showSql;
//        @Value("${orm.helper.persistence.properties.formatSql}")
//        private String formatSql;
//        @Value("${orm.helper.persistence.properties.dialect}")
//        private String dialect;
//
//        @Getter
//        private final Properties properties = new Properties();
//
//        @PostConstruct
//        public void print() {
//            if (logger.isDebugEnabled()) {
//                logger.debug("-------- PersistenceProperties.JpaProperties --------");
//                logger.debug("hibernate.hbm2ddl.auto= " + hbmToDDLAuto);
//                logger.debug("hibernate.show_sql= " + showSql);
//                logger.debug("hibernate.format_sql= " + formatSql);
//                logger.debug("hibernate.dialect= " + dialect);
//                logger.debug("-------- PersistenceProperties.JpaProperties --------");
//            }
//
//            properties.setProperty("hibernate.dialect", dialect);
//            properties.setProperty("hibernate.hbm2ddl.auto", hbmToDDLAuto);
//            properties.setProperty("hibernate.show_sql", showSql);
//            properties.setProperty("hibernate.format_sql", formatSql);
//        }
//    }


    @Resource(name = BeanConstants.ORM_HELPER_JPA_PROPERTIES)
    private Properties jpaProperties;
    
    @Autowired
    @Qualifier(BeanConstants.ORM_HELPER_MASTER_DATA_SOURCE)
    private DataSource masterDataSource;
    
    @Autowired
    @Qualifier(BeanConstants.ORM_HELPER_SLAVE_DATA_SOURCE)
    private DataSource slaveDataSource;
    
    @Autowired
    @Qualifier(BeanConstants.ORM_HELPER_DEFAULT_DS_TYPE)
    private DataSourceTypeContext.DataSourceType defaultDataSourceType = DataSourceTypeContext.DataSourceType.MASTER;

    @Resource(name = BeanConstants.ORM_HELPER_PACKAGES_TO_SCAN)
    private List<String> entityPackagesToScan;
    
    @Resource(name = BeanConstants.ORM_HELPER_MAPPING_FILES)
    private List<String> mappingFiles;


    @Bean
    @Primary
    public DataSource dataSource() {
        logger.info("Creating MasterSlaveRoutingDataSource");
        MasterSlaveRoutingDataSource dataSource = new MasterSlaveRoutingDataSource();
        if (DataSourceTypeContext.DataSourceType.SLAVE.equals(defaultDataSourceType)) {
            dataSource.setDefaultTargetDataSource(slaveDataSource);
        } else {
            dataSource.setDefaultTargetDataSource(masterDataSource);
        }
        logger.info("Default Target datasource: " + defaultDataSourceType);
        Map<Object, Object> targetDataSources = new HashMap<Object, Object>();
        targetDataSources.put(DataSourceTypeContext.DataSourceType.MASTER, masterDataSource);
        targetDataSources.put(DataSourceTypeContext.DataSourceType.SLAVE, slaveDataSource);
        dataSource.setTargetDataSources(targetDataSources);
        return dataSource;
    }

    @Bean
    @Primary
    public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
        LocalContainerEntityManagerFactoryBean entityManagerFactoryBean = new LocalContainerEntityManagerFactoryBean();
        entityManagerFactoryBean.setDataSource(applicationContext.getBean(DataSource.class));
        entityManagerFactoryBean.setPersistenceUnitName(PERSISTENCE_UNIT_NAME);
        if (null != entityPackagesToScan) {
            entityManagerFactoryBean.setPackagesToScan(entityPackagesToScan.toArray(new String[] {}));
        } else {
            logger.warn("No packages provided to scan for entities....");
        }
        if(null != mappingFiles){
            entityManagerFactoryBean.setMappingResources(mappingFiles.toArray(new String[] {}));
        } else {
            logger.warn("No Mapping files provided to scan for entities....");
        }
        JpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        entityManagerFactoryBean.setJpaVendorAdapter(vendorAdapter);
        if(null == jpaProperties) {
            logger.warn("No properties provided for JPA....");
            jpaProperties = new Properties();
        }
        entityManagerFactoryBean.setJpaProperties(jpaProperties);
        return entityManagerFactoryBean;
    }

    @Bean
    @Primary
    public PlatformTransactionManager transactionManager(EntityManagerFactory entityManagerFactory) {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(entityManagerFactory);
        return transactionManager;
    }

    @Bean
    @Primary
    public PersistenceExceptionTranslationPostProcessor exceptionTranslation() {
        return new PersistenceExceptionTranslationPostProcessor();
    }

    @Bean
    public ContextAutowireFactory contextAutowireFactory() {
        return ContextAutowireFactory.getInstance();
    }
}
