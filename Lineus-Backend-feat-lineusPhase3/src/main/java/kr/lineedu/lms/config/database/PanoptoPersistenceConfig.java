package kr.lineedu.lms.config.database;

import jakarta.persistence.EntityManagerFactory;
import kr.lineedu.lms.global.annotation.ExtraRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateProperties;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateSettings;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import javax.sql.DataSource;
import java.util.Map;
import java.util.Set;

@Configuration
@EnableJpaRepositories(
        basePackages = {
                "kr.lineedu.lms.feature.user.domain",
                "kr.lineedu.lms.feature.userhistory.domain",
                "kr.lineedu.lms.feature.helpcenter.domain",
                "kr.lineedu.lms.feature.bbsctt.domain",
                "kr.lineedu.lms.feature.media.domain",
                "kr.lineedu.lms.feature.content",
                "kr.lineedu.lms.feature.school.domain",
                "kr.lineedu.lms.feature.panopto.domain.video",
                "kr.lineedu.lms.feature.panopto.domain.onlineAttendance"
        },
        entityManagerFactoryRef = PanoptoPersistenceConfig.EXTRA_ENTITY_MANAGER_FACTORY,
        transactionManagerRef = PanoptoPersistenceConfig.EXTRA_TRANSACTION_MANAGER,
        includeFilters = @ComponentScan.Filter(type = FilterType.ANNOTATION, classes = ExtraRepository.class)
)
@EntityScan(basePackages = {
        "kr.lineedu.lms.feature.panopto.domain.video",   // Video 엔티티 패키지 경로
        "kr.lineedu.lms.feature.panopto.domain.onlineAttendance" // Attendance 엔티티 패키지 경로
})
@RequiredArgsConstructor
public class PanoptoPersistenceConfig {

    public static final String EXTRA_DATASOURCE_CONFIG_PREFIX = "panopto";
    public static final String EXTRA_PERSISTENCE_UNIT = EXTRA_DATASOURCE_CONFIG_PREFIX + "PersistenceUnit";
    public static final String EXTRA_DATASOURCE = EXTRA_DATASOURCE_CONFIG_PREFIX + "DataSource";
    public static final String EXTRA_ENTITY_MANAGER_FACTORY = EXTRA_DATASOURCE_CONFIG_PREFIX + "EntityManagerFactory";
    public static final String EXTRA_TRANSACTION_MANAGER = EXTRA_DATASOURCE_CONFIG_PREFIX + "TransactionManager";

    private final JpaProperties jpaProperties;
    private final HibernateProperties hibernateProperties;
    private final EntitySearchHelper entitySearchHelper;
    private final Environment environment;

    /**
     * Extra 데이터 소스 생성
     *
     * @return DataSource
     */
    @Bean(name = EXTRA_DATASOURCE)
    public DataSource panoptoDataSource() {
        String url = environment.getProperty("spring.panopto-datasource.jdbc-url", 
            environment.getProperty("spring.panopto-datasource.url",
                "jdbc:mysql://127.0.0.1:3306/db_lineus?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC&useUnicode=true&characterEncoding=UTF-8"));
        String username = environment.getProperty("spring.panopto-datasource.username", "root");
        String password = environment.getProperty("spring.panopto-datasource.password", "root");
        String driverClassName = environment.getProperty("spring.panopto-datasource.driver-class-name", 
            "com.mysql.cj.jdbc.Driver");
        
        return DataSourceBuilder.create()
                .url(url)
                .username(username)
                .password(password)
                .driverClassName(driverClassName)
                .build();
    }

    /**
     * Extra 데이터 소스의 EntityManagerFactory 생성
     *
     * @param builder    EntityManagerFactoryBuilder
     * @param dataSource DataSource
     * @return LocalContainerEntityManagerFactoryBean
     */
    @Bean(name = EXTRA_ENTITY_MANAGER_FACTORY)
    public LocalContainerEntityManagerFactoryBean extraEntityManagerFactory(
            EntityManagerFactoryBuilder builder, @Qualifier("panoptoDataSource") DataSource dataSource) {

        // 엔티티 클래스 패키지 조회
        Set<String> entityPackages = entitySearchHelper.findEntityPackagesOfExtra(EXTRA_DATASOURCE_CONFIG_PREFIX);

        // 프로퍼티값 조회
        Map<String, Object> properties = hibernateProperties.determineHibernateProperties(
                jpaProperties.getProperties(), new HibernateSettings());

        return builder.dataSource(dataSource)
                .packages("kr.lineedu.lms.feature.panopto.domain.video", "kr.lineedu.lms.feature.panopto.domain.onlineAttendance")
                .persistenceUnit(EXTRA_PERSISTENCE_UNIT)
                .properties(properties)
                .build();
    }

    /**
     * Extra 데이터 소스의 TransactionManager 생성
     *
     * @param entityManagerFactory EntityManagerFactory
     * @return PlatformTransactionManager
     */
    @Bean(name = EXTRA_TRANSACTION_MANAGER)
    public PlatformTransactionManager extraTransactionManager(
            @Qualifier(EXTRA_ENTITY_MANAGER_FACTORY) EntityManagerFactory entityManagerFactory) {
        return new JpaTransactionManager(entityManagerFactory);
    }
}
