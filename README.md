# orm-helper
A helper library for ORM layer with Spring and JPA. It also provides auto switch between Master - Slave Datasource

## Versions
- Final: `1.0-FINAL`
- Release: `1.2-SNAPSHOT`


## Usage in Maven
- Maven repository entry in pom.xml
```xml
<repositories>
    <repository>
        <id>orm-helper-mvn-repo</id>
        <url>https://raw.github.com/Consolefire/orm-helper/mvn-repo/</url>
        <snapshots>
            <enabled>true</enabled>
            <updatePolicy>always</updatePolicy>
        </snapshots>
    </repository>
</repositories>
```
- Maven dependency
```xml
<dependency>
    <groupId>com.consolefire.orm.helper</groupId>
    <artifactId>orm-helper</artifactId>
    <version>1.2-SNAPSHOT</version>
</dependency>
```

## Usages in Gradle
- Add repository url like 
```
maven { url "https://raw.github.com/Consolefire/orm-helper/mvn-repo" }
```
- Add Dependency 
```
compile(group: 'com.consolefire.orm.helper', name: 'orm-helper', version: '1.0-FINAL')
```

## Sample using Maven and Mysql
Complete example is available [here](https://github.com/Consolefire/sample-projects/tree/master/orm-jpa-sample "orm-jpa-sample").
- Add 2 databases: Master and Slave
- Insert records in both database

```sql
drop database if exists office_master;
create database office_master;
use office_master;
create table employees ( 
    id bigint not null auto_increment, 
    email varchar(150) not null, 
    name varchar(150) not null, 
    created_at timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by varchar(150) NOT NULL DEFAULT 'SOMEONE',
    updated_at timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
    updated_by varchar(150) DEFAULT NULL,
    primary key (id)
);

alter table employees add constraint UK_hxpym1ml3cufk46aws6660klf unique (name, email);
insert  into employees (email, name) values ('sabuj.das@gmail.com', 'Sabuj Das');

drop database if exists office_slave;
create database office_slave;
use office_slave;

create table employees ( 
    id bigint not null auto_increment, 
    email varchar(150) not null, 
    name varchar(150) not null, 
    created_at timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by varchar(150) NOT NULL DEFAULT 'SOMEONE',
    updated_at timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
    updated_by varchar(150) DEFAULT NULL,
    primary key (id)
);

alter table employees add constraint UK_hxpym1ml3cufk46aws6660klf unique (name, email);
insert  into employees (email, name) values ('sabuj.das@hotmail.com', 'Sabuj Das');
```

- Create properties to provide inputs
```properties
spring.jpa.generate-ddl=false
spring.jpa.hibernate.ddl-auto=none
spring.data.jpa.repositories.enabled=false
persistence.databaseName=MYSQL
persistence.driverClass=com.mysql.jdbc.Driver
persistence.slave.url=jdbc:mysql://slave_host:3306/office_slave
persistence.slave.userName=ro
persistence.slave.password=ro_pass
persistence.master.url=jdbc:mysql://master_host:3306/office_master
persistence.master.userName=rw
persistence.master.password=rw_pass
orm.helper.persistence.properties.hbmToDDLAuto=validate
orm.helper.persistence.properties.showSql=true
orm.helper.persistence.properties.formatSql=true
orm.helper.persistence.properties.dialect=org.hibernate.dialect.MySQL5Dialect
```
- Create bean configuration
```java
@Configuration
public class PersistenceConfig {

    @Value("${persistence.driverClass}")
    private String driverClass;
    @Value("${persistence.master.url}")
    private String masterUrl;
    @Value("${persistence.master.userName}")
    private String masterUser;
    @Value("${persistence.master.password}")
    private String masterPass;
    @Value("${persistence.slave.url}")
    private String slaveUrl;
    @Value("${persistence.slave.userName}")
    private String slaveUser;
    @Value("${persistence.slave.password}")
    private String slavePass;


    @Bean(name = "ormHelper.entityPackagesToScan")
    public List<String> entityPackagesToScan(){
        List<String> list = new ArrayList<>();
        list.add("com.consolefire.orm.sample.entity");
        return list;
    }

    @Bean(name = "ormHelper.masterDataSource")
    public DataSource masterDS(){
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(driverClass);
        dataSource.setUrl(masterUrl);
        dataSource.setUsername(masterUser);
        dataSource.setPassword(masterPass);
        return dataSource;
    }

    @Bean(name = "ormHelper.slaveDataSource")
    public DataSource slaveDS(){
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(driverClass);
        dataSource.setUrl(slaveUrl);
        dataSource.setUsername(slaveUser);
        dataSource.setPassword(slavePass);
        return dataSource;
    }

    @Bean(name = "ormHelper.defaultDataSourceType")
    public DataSourceTypeContext.DataSourceType defaultDsType(){
        return DataSourceTypeContext.DataSourceType.SLAVE;
    }
}
```
- Datasource Switch is done by the transactional property
```java
@Service
public class EmployeeServiceImpl implements EmployeeService {

    @Autowired
    private EmployeeDao employeeDao;

    @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = false)
    public Employee writeToMaster(Employee employee) {
        return employeeDao.save(employee);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = false)
    public Employee readFromMaster(Long id) {
        return employeeDao.find(id);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = true)
    public Employee readFromSlave(Long id) {
        return employeeDao.find(id);
    }
}

```
- Test config
```java
@Configuration
@ComponentScan(basePackages = {"com.consolefire.orm.sample", "com.consolefire.orm.helper"})
@EnableAspectJAutoProxy(proxyTargetClass = true)
public class TestConfig {
    @Bean
    public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
        return new PropertySourcesPlaceholderConfigurer();
    }
}
```


